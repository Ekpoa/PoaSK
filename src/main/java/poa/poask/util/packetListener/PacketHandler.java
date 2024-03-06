package poa.poask.util.packetListener;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import poa.poask.util.reflection.ChatComponents;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class PacketHandler extends ChannelDuplexHandler {


    public static Class<?> blockChangePacketClass = Reflection.getNMSClass("PacketPlayOutBlockChange", "net.minecraft.network.protocol.game");
    public static Class<?> systemChatPacketClass = Reflection.getNMSClass("ClientboundSystemChatPacket", "net.minecraft.network.protocol.game");


    public static Method contentsMethod;
    public static Method overlayMethod;
    public static Method getBlockState;
    public static Method getPos;
    public static Method getCenter;
    public static Method getX;
    public static Method getY;
    public static Method getZ;


    static {
        try {
            getBlockState = blockChangePacketClass.getDeclaredMethod("a");

            contentsMethod = systemChatPacketClass.getDeclaredMethod("a"); //https://nms.screamingsandals.org/1.20.4/net/minecraft/network/protocol/game/ClientboundSystemChatPacket.html
            overlayMethod = systemChatPacketClass.getDeclaredMethod("d"); //^
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
        String name = ctx.name().replace("-PoaSK-", "");
        Player player = Bukkit.getPlayer(name);

        switch (msg.getClass().getSimpleName()) {
            case "PacketPlayOutBlockChange" -> {
                try {
                    if (player != null) {
                        if (getPos == null)
                            getPos = msg.getClass().getDeclaredMethod("d"); //https://nms.screamingsandals.org/1.20.4/net/minecraft/network/protocol/game/ClientboundBlockUpdatePacket.html

                        Object blockPos = getPos.invoke(msg);

                        if (getCenter == null)
                            getCenter = blockPos.getClass().getDeclaredMethod("b"); //https://nms.screamingsandals.org/1.20.4/net/minecraft/core/BlockPos.html

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
                } catch (Exception ignored) {
                    super.write(ctx, msg, promise);
                }
            }

            case "ClientboundSystemChatPacket" -> {
                if (!((boolean) overlayMethod.invoke(msg))) {
                    super.write(ctx, msg, promise);
                    return;
                }

                try {
                    ActionBarEvent actionBarEvent = new ActionBarEvent(player,
                            ChatComponents.adventureComponent(contentsMethod.invoke(msg)));
                    Bukkit.getServer().getPluginManager().callEvent(actionBarEvent);

                    //todo set actionbar

                    if(actionBarEvent.isCancelled())
                        return;

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        super.write(ctx, msg, promise);
    }

}
