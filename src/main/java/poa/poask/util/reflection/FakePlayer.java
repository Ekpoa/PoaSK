package poa.poask.util.reflection;

import lombok.SneakyThrows;
import org.bukkit.Location;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FakePlayer {
        //NOT REAL
    public static Class<?> infoUpdatePacketClass = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket", "net.minecraft.network.protocol.game");
    public static Class<?> infoUpdatePacketActionClass = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket$a", "net.minecraft.network.protocol.game");
    public static Class<?> serverPlayerClass = Reflection.getNMSClass("EntityPlayer", "net.minecraft.server.level");
    public static Class<?> playOutNamedEntitySpawnPacketClass = Reflection.getNMSClass("PacketPlayOutNamedEntitySpawn", "net.minecraft.network.protocol.game");
    public static Class<?> playerClass = Reflection.getNMSClass("EntityHuman", "net.minecraft.world.entity.player");
    public static Class<?> gameProfileClass = Reflection.getNMSClass("GameProfile", "com.mojang.authlib");
    public static Class<?> entityPlayerClass = Reflection.getNMSClass("EntityPlayer", "net.minecraft.server.level");
    public static Class<?> worldClass = Reflection.getNMSClass("WorldServer", "net.minecraft.server.level");
    public static Class<?> serverClass = Reflection.getNMSClass("MinecraftServer", "net.minecraft.server");
    public static Class<?> entityClass = Reflection.getNMSClass("Entity", "net.minecraft.world.entity");

    public static Constructor<?> infoPacketConstructor;
    public static Constructor<?> playOutEntitySpawnConstructor;
    public static Constructor<?> gameProfileConstructor;
    public static Constructor<?> entityPlayerConstructor;

    public static Method setPosMethod;

    public static List<?> infoUpdateActionEnums = Arrays.stream(infoUpdatePacketActionClass.getEnumConstants()).toList();

    public static Object infoUpdateActionADD_PLAYER;
    public static Object infoUpdateActionUPDATE_LISTED;


    static {
        try {
            infoPacketConstructor = infoUpdatePacketClass.getDeclaredConstructor(infoUpdatePacketActionClass, serverPlayerClass);
            playOutEntitySpawnConstructor = playOutNamedEntitySpawnPacketClass.getDeclaredConstructor(playerClass);
            gameProfileConstructor = gameProfileClass.getDeclaredConstructor(UUID.class, String.class);
            entityPlayerConstructor = entityPlayerClass.getDeclaredConstructor(serverClass, worldClass, gameProfileClass);

            setPosMethod = entityClass.getDeclaredMethod("e", double.class, double.class, double.class);

            for (Object object : infoUpdateActionEnums)
                if(object.toString().equalsIgnoreCase("ADD_PLAYER")){
                    infoUpdateActionADD_PLAYER = object;
                    break;
                }
            for (Object object : infoUpdateActionEnums)
                if(object.toString().equalsIgnoreCase("UPDATE_LISTED")){
                    infoUpdateActionUPDATE_LISTED = object;
                    break;
                }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    
    @SneakyThrows
    public static Object gameProfile(UUID uuid, String name){
        return gameProfileConstructor.newInstance(uuid, name);
    }

    @SneakyThrows
    public static Object entityPlayer(Object nmsServer, Object nmsWorld, Object gameProfile){
        return entityPlayerConstructor.newInstance(nmsServer, nmsWorld, gameProfile);
    }

    @SneakyThrows
    public static void setEntityPlayerPos(Object entityPlayer, Location location){
        setPosMethod.invoke(entityPlayer, location.getX(), location.getY(), location.getZ());
    }

    @SneakyThrows
    public static Object addPacket(Object fakePlayer) {
        return infoPacketConstructor.newInstance(infoUpdateActionADD_PLAYER, fakePlayer);
    }

    @SneakyThrows
    public static Object updatePacket(Object fakePlayer) {
        return infoPacketConstructor.newInstance(infoUpdateActionUPDATE_LISTED, fakePlayer);
    }

    @SneakyThrows
    public static Object spawn(Object fakePlayer) {
        return  playOutEntitySpawnConstructor.newInstance(fakePlayer);
    }


}
