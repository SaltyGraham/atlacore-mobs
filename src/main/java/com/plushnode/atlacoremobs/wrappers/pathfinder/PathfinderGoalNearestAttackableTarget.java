package com.plushnode.atlacoremobs.wrappers.pathfinder;

import com.google.common.base.Predicate;
import com.plushnode.atlacoremobs.util.ReflectionUtil;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PathfinderGoalNearestAttackableTarget implements PathfinderGoal {
    private static Class<?> InternalClass;
    private static Constructor<?> constructor;
    private Object handle = null;

    static {
        InternalClass = ReflectionUtil.getInternalClass("net.minecraft.server.%s.PathfinderGoalNearestAttackableTarget");

        try {
            constructor = InternalClass.getConstructor(ReflectionUtil.EntityCreature, Class.class, int.class, boolean.class, boolean.class, Predicate.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public PathfinderGoalNearestAttackableTarget(Entity entity, Class<?> targetClazz, int i, boolean f1, boolean f2, Predicate<?> predicate) {
        if (!ReflectionUtil.isCreature(entity)) return;

        try {
            Object entityHandle = ReflectionUtil.getEntityHandle.invoke(entity);
            this.handle = constructor.newInstance(entityHandle, targetClazz, i, f1, f2, predicate);
        } catch (IllegalAccessException|InvocationTargetException|InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getHandle() {
        return this.handle;
    }
}
