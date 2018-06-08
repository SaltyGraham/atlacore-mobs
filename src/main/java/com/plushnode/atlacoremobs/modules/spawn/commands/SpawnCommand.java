package com.plushnode.atlacoremobs.modules.spawn.commands;

import com.plushnode.atlacore.game.element.Element;
import com.plushnode.atlacore.game.element.Elements;
import com.plushnode.atlacoremobs.AtlaCoreMobsPlugin;
import com.plushnode.atlacoremobs.ScriptedUser;
import com.plushnode.atlacoremobs.commands.MultiplexableCommand;
import com.plushnode.atlacoremobs.generator.ScriptedAirbenderGenerator;
import com.plushnode.atlacoremobs.generator.ScriptedFirebenderGenerator;
import com.plushnode.atlacoremobs.generator.ScriptedUserGenerator;
import com.plushnode.atlacoremobs.modules.spawn.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnCommand implements MultiplexableCommand {
    private AtlaCoreMobsPlugin plugin;
    private String[] aliases = { "spawn", "s" };

    public SpawnCommand(AtlaCoreMobsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be ran as a player.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.GREEN + "/acmobs spawn [[element:]type] <amount>");
            return true;
        }

        Player player = (Player)sender;

        SpawnManager spawnManager = plugin.getSpawnManager();

        ScriptedUserGenerator userGenerator = new ScriptedFirebenderGenerator();
        EntityType type;

        String typeString = args[1];

        if (typeString.contains(":")) {
            String[] typeTokens = typeString.split(":", 2);

            String elementStr = typeTokens[0];

            if (!elementStr.isEmpty()) {
                switch (elementStr.toLowerCase().charAt(0)) {
                    case 'f':
                        userGenerator = new ScriptedFirebenderGenerator();
                    break;
                    case 'a':
                        userGenerator = new ScriptedAirbenderGenerator();
                    break;
                    default:
                        userGenerator = new ScriptedFirebenderGenerator();
                }
            }

            typeString = typeTokens[1];
        }

        try {
            type = EntityType.valueOf(typeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Failed to spawn mob. Illegal type specified.");
            return true;
        }

        if (!type.isSpawnable() || !type.isAlive()) {
            sender.sendMessage(ChatColor.RED + "Failed to spawn mob. Type must be a LivingEntity.");
            return true;
        }

        int amount = 1;
        if (args.length > 2) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                // pass
            }
        }

        int numSpawned = 0;

        for (int i = 0; i < amount; ++i) {
            ScriptedUser user = spawnManager.spawn(player, type, userGenerator);
            if (user != null) {
                ++numSpawned;
            }
        }

        if (numSpawned > 0) {
            sender.sendMessage(ChatColor.GOLD + Integer.toString(numSpawned) + " mobs spawned at your location.");
        } else {
            sender.sendMessage(ChatColor.RED + "Failed to spawn mobs.");
        }

        return true;
    }

    @Override
    public String getDescription() {
        return "Spawns bending mobs at your location.";
    }

    @Override
    public String getPermission() {
        return "acmobs.command.spawn";
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }
}