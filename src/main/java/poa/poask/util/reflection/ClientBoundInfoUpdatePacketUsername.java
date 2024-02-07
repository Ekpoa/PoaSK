package poa.poask.util.reflection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import poa.poask.PoaSK;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class ClientBoundInfoUpdatePacketUsername {

    //OUTDATED
    private static final Class<?> serverPlayerClass = Reflection.getNMSClass("EntityPlayer", "net.minecraft.server.level");
    private static final Class<?> gameProfileClass = Reflection.getNMSClass("GameProfile", "com.mojang.authlib");
    private static final Class<?> entryClass = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket$b", "net.minecraft.network.protocol.game");
    private static final Class<?> gameTypeClass = Reflection.getNMSClass("EnumGamemode", "net.minecraft.world.level");
    private static final Class<?> componentClass = Reflection.getNMSClass("IChatBaseComponent", "net.minecraft.network.chat");
    private static final Class<?> remoteSessionDataClass = Reflection.getNMSClass("RemoteChatSession$a", "net.minecraft.network.chat");
    private static final Constructor<?> entryConstructor;
    private static final Field gamemodeField;
    private static final Class<?> gameModeClass = Reflection.getNMSClass("PlayerInteractManager", "net.minecraft.server.level");
    private static final Class<?> friendlyByteBuffClass = Reflection.getNMSClass("PacketDataSerializer", "net.minecraft.network");
    private static final Class<?> clientBoundPacketClassAction = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket$a", "net.minecraft.network.protocol.game");
    private static final Method writeEnumSet;
    private static final Field writerField;
    private static final Method writeUUID;
    private static final Method profileID;
    private static final Class<?> writeClass = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket$a$b", "net.minecraft.network.protocol.game");
    private static final Method write;
    private static final Class<?> buffWriter = Reflection.getNMSClass("PacketDataSerializer$b", "net.minecraft.network");
    private static final Method writeCollection;
    private static final Class<?> packetClass = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket", "net.minecraft.network.protocol.game");


    static {
        try {
            entryConstructor = entryClass.getDeclaredConstructor(
                    UUID.class,
                    gameProfileClass,
                    boolean.class,
                    int.class,
                    gameTypeClass,
                    componentClass,
                    remoteSessionDataClass
            );

            gamemodeField = serverPlayerClass.getDeclaredField("d");
            writeEnumSet = friendlyByteBuffClass.getDeclaredMethod("a", EnumSet.class, Class.class);
            writerField = clientBoundPacketClassAction.getDeclaredField("h");
            writerField.setAccessible(true);
            writeUUID = friendlyByteBuffClass.getDeclaredMethod("a", UUID.class);
            profileID = entryClass.getDeclaredMethod("a");
            write = writeClass.getDeclaredMethod("write", friendlyByteBuffClass, entryClass);
            writeCollection = friendlyByteBuffClass.getDeclaredMethod("a", Collection.class, buffWriter);

        } catch (NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    public static Object usernamePacket(Player player, String username) {
        Object serverPlayer = Reflection.getHandle(player);

        Object newProfile = gameProfileClass.getDeclaredConstructor(UUID.class, String.class).newInstance(player.getUniqueId(), username);

        List<Object> entries = new ArrayList<>();

        Object gamemode = gamemodeField.get(serverPlayer);

        Method gameTypeMethod = gameModeClass.getDeclaredMethod("b");
        Object gameType = gameTypeMethod.invoke(gamemode);

        Object newEntry = entryConstructor.newInstance(player.getUniqueId(), newProfile, true, player.getPing(), gameType, null, null);
        entries.add(newEntry);

        Object newFBB = friendlyByteBuffClass.getDeclaredConstructor(ByteBuf.class).newInstance(Unpooled.buffer());
        Object addPlayer = clientBoundPacketClassAction.getField("a").get(null);

        EnumSet<?> enumSet = EnumSet.of((Enum) addPlayer);

        writeEnumSet.invoke(newFBB, enumSet, clientBoundPacketClassAction);

        Object writer = writerField.get(addPlayer);

        Object proxi = Proxy.newProxyInstance(PoaSK.getInstance().getClass().getClassLoader(), new Class[]{buffWriter}, (proxy, method, argss) -> {
            if (!method.getName().equalsIgnoreCase("accept"))
                return null;

            Object buf2 = argss[0];
            Object entry = argss[1];

            writeUUID.invoke(buf2, profileID.invoke(entry));

            for (int i = 0; i < (enumSet).size(); i++) {
                write.invoke(writer, buf2, entry);
            }
            return null;
        });

        writeCollection.invoke(newFBB, entries, proxi);

        Object packet = packetClass.getDeclaredConstructor(friendlyByteBuffClass).newInstance(newFBB);



        return packet;
    }
}
