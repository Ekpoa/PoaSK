package poa.poask.util.reflection.common;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class CommonClassMethodFields {

    //CAMERA
    public static final Class<?> cameraPacketClass = Reflection.getNMSClass("PacketPlayOutCamera", "net.minecraft.network.protocol.game");
    public static final Constructor<?> cameraPacketConstructor;


    //CHAT COMPONENTS
    public static Class<?> paperAdventureClass;
    public static Field wrapperAwareSerializerField;
    public static Method adventureSerializeMethod;
    public static Object wrapperAwareObject;

    public static Method adventureDeserializeMethod;
    public static Object adventureDeserializeObject;


    //METADATA
    public static final Class<?> chatBaseComponent = Reflection.getNMSClass("IChatBaseComponent", "net.minecraft.network.chat");
    public static final Class<?> dataValueClass = Reflection.getNMSClass("DataWatcher$b", "net.minecraft.network.syncher");
    public static final Class<?> dataSerializersClass = Reflection.getNMSClass("DataWatcherRegistry", "net.minecraft.network.syncher");
    public static final Class<?> poseClass = Reflection.getNMSClass("EntityPose", "net.minecraft.world.entity");
    public static final Class<?> dataSerializerClass = Reflection.getNMSClass("DataWatcherSerializer", "net.minecraft.network.syncher");
    public static final Class<?> metadataPacketClass = Reflection.getNMSClass("PacketPlayOutEntityMetadata", "net.minecraft.network.protocol.game");
    public static final Class<?> itemStackClass = Reflection.getNMSClass("ItemStack", "net.minecraft.world.item");
    public static final Class<?> rotationsClass = Reflection.getNMSClass("Vector3f", "net.minecraft.core");
    public static final Method writeMethod;
    public static final Method fromBukkitCopy;
    public static final Constructor<?> dataValueConstructor;
    public static final Constructor<?> metadataPacketConstructor;
    public static final Constructor<?> rotationConstructor;


    //GLOW PACKET
    public static final Class<?> nmsEntityClass = Reflection.getNMSClass("Entity", "net.minecraft.world.entity");
    public static final Class<?> dataWatcherClass = Reflection.getNMSClass("DataWatcher", "net.minecraft.network.syncher");
    public static final Class<?> itemWatcherClass = Reflection.getNMSClass("DataWatcher$Item", "net.minecraft.network.syncher");
    public static final Method initialMethod;
    public static final Method getEntityDataMethod;
    public static final Constructor<?> dataWatcherConstructor;


    //HEAD ROT
    public static final Class<?> headRotPacketClass = Reflection.getNMSClass("PacketPlayOutEntityHeadRotation", "net.minecraft.network.protocol.game");
    public static final Constructor<?> headRotPacketConstructor;


    //POS ROT
    public static final Class<?> posPacketClass = Reflection.getNMSClass("PacketPlayOutEntity$PacketPlayOutRelEntityMove", "net.minecraft.network.protocol.game");
    public static final Class<?> rotPacketClass = Reflection.getNMSClass("PacketPlayOutEntity$PacketPlayOutEntityLook", "net.minecraft.network.protocol.game");
    public static final Constructor<?> posPacketConstructor;
    public static final Constructor<?> rotPacketConstructor;


    //REMOVE ENTITY
    public static final Class<?> removeEntityPacketClass = Reflection.getNMSClass("PacketPlayOutEntityDestroy", "net.minecraft.network.protocol.game");
    public static final Constructor<?> removeEntityPacketConstructor;


    //SEND PACKET
    public static final Class<?> serverPlayerClass = Reflection.getNMSClass("EntityPlayer", "net.minecraft.server.level");
    public static final Class<?> connectClass;
    public static final Class<?> packetSendClass = Reflection.getNMSClass("Packet", "net.minecraft.network.protocol");
    ;
    public static Field connectionField;
    public static Method connectionMethod;


    //PASSENGER PACKET
    public static Class<?> setPassengerPacket = Reflection.getNMSClass("PacketPlayOutMount", "net.minecraft.network.protocol.game");
    public static Constructor setPassengerConstructor;



    //SPAWN ENTITY
    public static final Class<?> spawnPacketClass = Reflection.getNMSClass("PacketPlayOutSpawnEntity", "net.minecraft.network.protocol.game");
    public static final Class<?> entityTypeClass = Reflection.getNMSClass("EntityTypes", "net.minecraft.world.entity");
    public static final Class<?> vec3Class = Reflection.getNMSClass("Vec3D", "net.minecraft.world.phys");
    public static final Class<?> letterClass = Reflection.getPluginClass("Letters", "poa.poask.util.reflection.common");
    public static Constructor<?> spawnPacketConstructor;



    //MINECRAFT SERVER
    public static final Class<?> minecraftServerClass = Reflection.getNMSClass("MinecraftServer", "net.minecraft.server");
    public static final Object minecraftServerGetServer;
    public static final Method getMinecraftServerConnection;
    public static final Object minecraftServerConnection;
    public static final Class<?> minecraftServerConnectionClass = Reflection.getNMSClass("ServerConnection", "net.minecraft.server.network");
    public static final Method getConnections;
    public static List<?> serverConnections = null;
    public static final Method getRemoteAddressMethod;
    public static Class<?> networkManagerClass = Reflection.getNMSClass("NetworkManager", "net.minecraft.network");




    //PACKET INJECTOR
    public static final Field minecraftServerConnectionChannel;
    public static final Field playerConnection2;


    static {
        try {
            cameraPacketConstructor = cameraPacketClass.getDeclaredConstructor(FriendlyByteBuf.friendlyByteBufClass);



            paperAdventureClass = Class.forName("io.papermc.paper.adventure.PaperAdventure");
            wrapperAwareSerializerField = paperAdventureClass.getDeclaredField("WRAPPER_AWARE_SERIALIZER");
            wrapperAwareSerializerField.setAccessible(true);
            wrapperAwareObject = wrapperAwareSerializerField.get(null);
            adventureSerializeMethod = ComponentSerializer.class.getMethod("serialize", Component.class);
            adventureDeserializeMethod = ComponentSerializer.class.getMethod("deserialize", Object.class);




            writeMethod = chatBaseComponent.getDeclaredMethod(Letters.chatComponentLiteral, String.class);
            dataValueConstructor = dataValueClass.getDeclaredConstructor(int.class, dataSerializerClass, Object.class);
            metadataPacketConstructor = metadataPacketClass.getDeclaredConstructor(int.class, List.class);
            fromBukkitCopy = itemStackClass.getDeclaredMethod("fromBukkitCopy", ItemStack.class);
            rotationConstructor = rotationsClass.getDeclaredConstructor(float.class, float.class, float.class);



            initialMethod = itemWatcherClass.getDeclaredMethod(Letters.dataWatcher$ItemValue);

            getEntityDataMethod = nmsEntityClass.getDeclaredMethod(Letters.getEntityData);
            dataWatcherConstructor = dataWatcherClass.getDeclaredConstructor(nmsEntityClass);



            headRotPacketConstructor = headRotPacketClass.getDeclaredConstructor(FriendlyByteBuf.friendlyByteBufClass);



            posPacketConstructor = posPacketClass.getDeclaredConstructor(int.class, short.class, short.class, short.class, boolean.class);
            rotPacketConstructor = rotPacketClass.getDeclaredConstructor(int.class, byte.class, byte.class, boolean.class);



            removeEntityPacketConstructor = removeEntityPacketClass.getDeclaredConstructor(int[].class);



            if (List.of("1202", "1203", "1204").contains(Letters.getBukkitVersion()))
                connectClass = Reflection.getNMSClass("ServerCommonPacketListenerImpl", "net.minecraft.server.network");
            else
                connectClass = Reflection.getNMSClass("PlayerConnection", "net.minecraft.server.network");
            connectionField = serverPlayerClass.getDeclaredField(Letters.getEntityPlayerConnectionField);
            connectionMethod = connectClass.getDeclaredMethod(Letters.connectionSendPacket, packetSendClass);



            setPassengerConstructor = setPassengerPacket.getDeclaredConstructor(FriendlyByteBuf.friendlyByteBufClass);



            spawnPacketConstructor = spawnPacketClass.getDeclaredConstructor(int.class, UUID.class, double.class, double.class, double.class, float.class, float.class, entityTypeClass, int.class, vec3Class, double.class);

            //MINECRAFT SERVER
            minecraftServerGetServer = minecraftServerClass.getDeclaredMethod("getServer").invoke(CommonClassMethodFields.minecraftServerClass);

            getMinecraftServerConnection = minecraftServerClass.getDeclaredMethod(Letters.getMinecraftServerConnection);

            minecraftServerConnection = getMinecraftServerConnection.invoke(minecraftServerGetServer);
            getConnections = minecraftServerConnectionClass.getDeclaredMethod(Letters.getConnections);
            if(List.of("1202", "1203", "1204").contains(Letters.getBukkitVersion()))
                serverConnections = (List<?>) getConnections.invoke(minecraftServerConnection);
            getRemoteAddressMethod = networkManagerClass.getDeclaredMethod(Letters.getRemoteAddress);
            //Continues for packet injector
            minecraftServerConnectionChannel = networkManagerClass.getDeclaredField(Letters.minecraftServerConnectionAddress);
            playerConnection2 = connectClass.getDeclaredField(Letters.connectionField2);
        } catch (NoSuchMethodException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static Object itemAsBukkitCopy(ItemStack item){
       return CommonClassMethodFields.fromBukkitCopy.invoke(CommonClassMethodFields.itemStackClass, item);
    }


}
