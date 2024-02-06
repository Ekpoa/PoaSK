package poa.poask.util.reflection.common;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class FriendlyByteBuf {

    public static final Class<?> friendlyByteBufClass = Reflection.getNMSClass("PacketDataSerializer", "net.minecraft.network");
    public static final Constructor<?> friendlyByteBufConstructor;


    public static final Method writeByte;
    public static final Method writeVarInt;
    public static final Method writeVarIntArray;
    public static final Method writeComponent;
    public static final Method writeUTF;
    public static final Method writeComponentLitteral;
    public static final Method writeEnum;

    public static final Class<?> chatBaseComponent = Reflection.getNMSClass("IChatBaseComponent", "net.minecraft.network.chat");

    static {
        try {
            friendlyByteBufConstructor = friendlyByteBufClass.getDeclaredConstructor(ByteBuf.class);
            writeByte = friendlyByteBufClass.getDeclaredMethod("writeByte", int.class);
            writeVarInt = friendlyByteBufClass.getDeclaredMethod(Letters.getFriendlyByteBufferWriteVarInt, int.class);
            writeComponent = friendlyByteBufClass.getDeclaredMethod(Letters.friendlyByteBufferWriteComponent, chatBaseComponent);
            writeUTF = friendlyByteBufClass.getDeclaredMethod(Letters.friendlyByteBufferWriteUtf, String.class);
            writeComponentLitteral = chatBaseComponent.getDeclaredMethod(Letters.chatComponentLiteral, String.class);
            writeEnum = friendlyByteBufClass.getDeclaredMethod(Letters.friendlyByteBufferWriteEnum, Enum.class);
            writeVarIntArray = friendlyByteBufClass.getDeclaredMethod(Letters.friendlyByteBuffWriteVarIntArray, int[].class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }


}
