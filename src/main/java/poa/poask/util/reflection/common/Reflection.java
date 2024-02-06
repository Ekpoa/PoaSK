package poa.poask.util.reflection.common;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
    private static final boolean NEW_NMS = true;


    public static Class<?> getOBCClass(String obcClassString) {
        String name = "org.bukkit.craftbukkit." + VERSION + obcClassString;
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?> getPluginClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getNMSClass(String nmsClass, String nmsPackage) {
        try {
            if (NEW_NMS) {
                return Class.forName(nmsPackage + "." + nmsClass);
            } else {
                return Class.forName("net.minecraft.server." + VERSION + nmsClass);
            }
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static Object getNMSHandle(Entity entity) {
        try {
            Method getHandle = entity.getClass().getMethod("getHandle");
            return getHandle.invoke(entity);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

            return null;
        }
    }

    public static Object getField(String field, Class<?> clazz, Object object) {
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(object);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            return null;
        }
    }

    public static void setField(String field, Class<?> clazz, Object object, Object toSet) {
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            f.set(object, toSet);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
        }
    }

    public static void setField(String field, Object object, Object toSet) {
        try {
            Field f = object.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(object, toSet);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException ignored) {
        }
    }

    public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Field conField = getHandle(player).getClass().getField("playerConnection");
        return conField.get(getHandle(player));
    }

    public static Object getHandle(Player player) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        return getHandle.invoke(player);
    }

    public static Object getHandle(World world) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getHandle = world.getClass().getMethod("getHandle");
        return getHandle.invoke(world);
    }

    public static Object getServer(Server server) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getHandle = server.getClass().getMethod("getServer");
        return getHandle.invoke(server);
    }
}
