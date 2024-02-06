package poa.poask.util.reflection.common;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;

public class SendPacket {

    @SuppressWarnings("ConstantConditions")
    @SneakyThrows
    public static void sendPacket(Player player, Object packet){
        Object entityPlayer = Reflection.getHandle(player);
        Object connectionObject = CommonClassMethodFields.connectionField.get(entityPlayer);
        CommonClassMethodFields.connectionMethod.invoke(connectionObject, packet);
    }

}
