package poa.poask.util.reflection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import poa.poask.util.reflection.common.FriendlyByteBuf;
import poa.poask.util.reflection.common.Letters;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

public class TeamPacket {

    private static final Class<?> bufferClass = Reflection.getNMSClass("PacketDataSerializer", "net.minecraft.network");
    private static Constructor<?> bufferConstructor;

    private static Object nuller;

    private static final Class<?> chatFormattingClass = Reflection.getNMSClass("EnumChatFormat", "net.minecraft");

    private static final Class<?> parameterClass = Reflection.getNMSClass("PacketPlayOutScoreboardTeam$b", "net.minecraft.network.protocol.game");
    private static final Class<?> teamPacketClass = Reflection.getNMSClass("PacketPlayOutScoreboardTeam", "net.minecraft.network.protocol.game");
    private static Constructor<?> packetConstruct;

    static {
        try {
            bufferConstructor = bufferClass.getDeclaredConstructor(ByteBuf.class);

            nuller = FriendlyByteBuf.writeComponentLitteral.invoke(FriendlyByteBuf.chatBaseComponent, ""); //null


            packetConstruct = teamPacketClass.getDeclaredConstructor(String.class, int.class, Optional.class, Collection.class);
            packetConstruct.setAccessible(true);
        } catch (Exception ignored) {
        }
    }


    @SuppressWarnings("ConstantConditions")
    @SneakyThrows
    public static Object teamPacket(String teamName, String displayName, String nameTagVisibility, String collision, String color, String prefix, String suffix, Collection<String> players) {
        Object buffer = bufferConstructor.newInstance(Unpooled.buffer());
        Method writeMethod = FriendlyByteBuf.chatBaseComponent.getDeclaredMethod(Letters.chatComponentLiteral, String.class);

        FriendlyByteBuf.writeComponent.invoke(buffer, writeMethod.invoke(FriendlyByteBuf.chatBaseComponent, displayName));

        FriendlyByteBuf.writeByte.invoke(buffer, 0);


        FriendlyByteBuf.writeUTF.invoke(buffer, nameTagVisibility);


        FriendlyByteBuf.writeUTF.invoke(buffer, collision);

        Method getByName = chatFormattingClass.getDeclaredMethod(Letters.chatFormattingGetByName, String.class);

        if(color != null && !color.equalsIgnoreCase("")) {
            Enum<?> chatFormattingColor = (Enum<?>) getByName.invoke(chatFormattingClass, color);
            FriendlyByteBuf.writeEnum.invoke(buffer, chatFormattingColor);
        }

        FriendlyByteBuf.writeComponent.invoke(buffer, writeMethod.invoke(FriendlyByteBuf.chatBaseComponent, prefix));

        FriendlyByteBuf.writeComponent.invoke(buffer, writeMethod.invoke(FriendlyByteBuf.chatBaseComponent, suffix));

        Object packetParameter = parameterClass.getDeclaredConstructor(bufferClass).newInstance(buffer);

        return packetConstruct.newInstance(teamName, 0, Optional.of(packetParameter), players);
    }

    public static Object teamPacketForGlow(String teamName, String color, Collection<String> players) {
        return teamPacket(teamName, "", "always", "always", color, "", "", players);
    }


}
