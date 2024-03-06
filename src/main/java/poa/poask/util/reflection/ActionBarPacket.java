package poa.poask.util.reflection;

import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Constructor;

public class ActionBarPacket {
    public static Class<?> actionBarPacketClass = Reflection.getNMSClass("ClientboundSetActionBarTextPacket", "net.minecraft.network.protocol.game");
    public static Constructor<?> actionBarConstructor;

    static {
        try {
            actionBarConstructor = actionBarPacketClass.getDeclaredConstructor(CommonClassMethodFields.chatBaseComponent);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static Object actionBar(String miniMessageString){
        miniMessageString = miniMessageString.replace("§", "&");

        return actionBarConstructor.newInstance(ChatComponents.nmsComponent(MiniMessage.miniMessage().deserialize(miniMessageString)));
    }


}
