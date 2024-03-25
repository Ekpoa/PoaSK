package poa.poask.util.reflection.common;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import poa.poask.PoaSK;

import java.lang.reflect.InvocationTargetException;

public class SendPacket {

    @SuppressWarnings("ConstantConditions")
    @SneakyThrows
    public static void sendPacket(Player player, Object packet){
        Bukkit.getScheduler().runTaskAsynchronously(PoaSK.getInstance(), () -> {
            Object entityPlayer;
            try {
                entityPlayer = Reflection.getHandle(player);
                Object connectionObject = CommonClassMethodFields.connectionField.get(entityPlayer);
                CommonClassMethodFields.connectionMethod.invoke(connectionObject, packet);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
