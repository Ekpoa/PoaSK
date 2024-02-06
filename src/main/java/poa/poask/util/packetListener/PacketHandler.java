package poa.poask.util.packetListener;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Method;

public class PacketHandler extends ChannelDuplexHandler {


    public static Class<?> packetClass = Reflection.getNMSClass("PacketPlayOutBlockChange", "net.minecraft.network.protocol.game");
    public static Class<?> vec3Class = CommonClassMethodFields.vec3Class;
    public static Method getBlockState;
    public static Method getPos;
    public static Method getCenter;
    public static Method getX;
    public static Method getY;
    public static Method getZ;

    static {
        try {
            getBlockState = packetClass.getDeclaredMethod("a");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutBlockChange")){
            try {
                String name = ctx.name().replace("-PoaSK-", "");
                Player player = Bukkit.getPlayer(name);

                if (player != null) {
                    if (getPos == null)
                        getPos = msg.getClass().getDeclaredMethod("d");

                    Object blockPos = getPos.invoke(msg);

                    if (getCenter == null)
                        getCenter = blockPos.getClass().getDeclaredMethod("b");

                    Object vec3i = getCenter.invoke(blockPos);

                    if (getX == null) {
                        Class<?> vecClass = vec3i.getClass();
                        getX = vecClass.getDeclaredMethod("a");
                        getY = vecClass.getDeclaredMethod("b");
                        getZ = vecClass.getDeclaredMethod("c");
                    }

                    Location location = new Location(player.getWorld(), (double) getX.invoke(vec3i), (double) getY.invoke(vec3i), (double) getZ.invoke(vec3i));

                    Block block = location.getBlock();


                    String input = getBlockState.invoke(msg).toString();

                    int blockNameStart = input.indexOf('{') + 1;
                    int blockNameEnd = input.indexOf('}');
                    String blockName = input.substring(blockNameStart, blockNameEnd);

                    String blockData = "";

                    if (input.contains("[") && input.contains("]")) {
                        int blockDataStart = input.indexOf('[') + 1;
                        int blockDataEnd = input.indexOf(']');
                        blockData = input.substring(blockDataStart, blockDataEnd);
                    }
                    BlockData newBlockData = Bukkit.createBlockData(blockName + "[" + blockData + "]");


                    ClientBlockChangeEvent clientBlockChangeEvent = new ClientBlockChangeEvent(player, block, newBlockData);
                    Bukkit.getServer().getPluginManager().callEvent(clientBlockChangeEvent);

                    if (clientBlockChangeEvent.isCancelled())
                        return;

                }
            }
            catch (Exception ignored){
                super.write(ctx, msg, promise);
            }
        }
        super.write(ctx, msg, promise);
    }

}
