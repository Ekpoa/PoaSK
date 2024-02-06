package poa.poask.util.packetListener;

import io.netty.channel.Channel;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PacketInjector {




    Object playerHandle;
    InetAddress address;

    Player player;

    String id;


    @SneakyThrows
    public PacketInjector(Player player, InetAddress address){
        playerHandle = Reflection.getHandle(player);
        this.address = address;
        this.player = player;
        id = player.getName() + "-PoaSK-";
    }

    @SneakyThrows
    public void injectPlayer(){
        Object playerConnection = CommonClassMethodFields.serverConnections.stream()
                .filter(connection -> {
                    try {
                        return CommonClassMethodFields.getRemoteAddressMethod.invoke(connection) instanceof InetSocketAddress;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(connection -> {
                    try {
                        return ((InetSocketAddress) CommonClassMethodFields.getRemoteAddressMethod.invoke(connection)).getAddress() == address;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findAny().orElseThrow(IllegalArgumentException::new);

        Channel channel = (Channel) CommonClassMethodFields.minecraftServerConnectionChannel.get(playerConnection);
        channel.pipeline().addBefore("packet_handler", id , new PacketHandler());
    }

    @SneakyThrows
    public void uninjectPlayer(){
        Object connectionObject = CommonClassMethodFields.connectionField.get(this.playerHandle);
        Object connection2 = CommonClassMethodFields.playerConnection2.get(connectionObject);
        Channel channel = (Channel) CommonClassMethodFields.minecraftServerConnectionChannel.get(connection2);

        if(channel.pipeline().get(id) != null)
            channel.pipeline().remove(id);


    }

}
