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
import poa.poask.effects.EffGlowEffect;
import poa.poask.util.reflection.ChatComponents;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Letters;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import static poa.poask.util.reflection.common.CommonClassMethodFields.dataSerializersClass;

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


    public static Class<?> setEntityDataPacketClass = CommonClassMethodFields.metadataPacketClass;
    public static Class<?> synchedEntityDataClass = CommonClassMethodFields.dataValueClass;
    public static Method packedItemsMethod;
    public static Method getValueMethod;
    public static Method getIdMethod;

    public static Method metadataPacketID;




    static {
        try {
            getBlockState = blockChangePacketClass.getDeclaredMethod("a");

            if(Letters.getBukkitVersion().equalsIgnoreCase("1204"))
                contentsMethod = systemChatPacketClass.getDeclaredMethod("a"); //https://nms.screamingsandals.org/1.20.4/net/minecraft/network/protocol/game/ClientboundSystemChatPacket.html
            else
                contentsMethod = systemChatPacketClass.getDeclaredMethod("adventure$content");

            overlayMethod = systemChatPacketClass.getDeclaredMethod("d"); //^



            packedItemsMethod = setEntityDataPacketClass.getDeclaredMethod("d"); //https://nms.screamingsandals.org/1.20.4/net/minecraft/network/protocol/game/ClientboundSetEntityDataPacket.html
            getIdMethod = synchedEntityDataClass.getDeclaredMethod("a"); //https://nms.screamingsandals.org/1.20.4/net/minecraft/network/syncher/SynchedEntityData$DataValue.html
            getValueMethod = synchedEntityDataClass.getDeclaredMethod("c"); //^


            metadataPacketID = setEntityDataPacketClass.getDeclaredMethod("a"); //https://nms.screamingsandals.org/1.20.4/net/minecraft/network/protocol/game/ClientboundSystemChatPacket.html


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


                    if (actionBarEvent.isCancelled())
                        return;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            case "PacketPlayOutEntityMetadata" -> {
                try {
                    int entityId = (int) metadataPacketID.invoke(msg);

                    Object serverLevel = CommonClassMethodFields.getServerLevel(player.getWorld());
                    if(serverLevel == null){
                        super.write(ctx, msg, promise);
                        return;
                    }

                    if(!(CommonClassMethodFields.getEntityFromId(entityId, serverLevel, true) instanceof Player target)){
                        super.write(ctx, msg, promise);
                        return;
                    }

                    if(target.isGlowing()){
                        super.write(ctx, msg, promise);
                        return;
                    }


                    boolean glow = true;


                    List<Integer> ids = EffGlowEffect.glowMap.get(player);

                    //Bukkit.broadcastMessage(entityId + " : " + Bukkit.getPlayer("Ekpoa").getEntityId());

                    if(entityId != target.getEntityId()){
                        super.write(ctx, msg, promise);
                        return;
                    }

                    if (ids == null) {
                       // Bukkit.broadcastMessage("a");
                        glow = false;
                    } else if (ids.isEmpty()) {
                        //Bukkit.broadcastMessage("b");
                        glow = false;
                    } else if (!ids.contains(entityId)) {
                       // Bukkit.broadcastMessage("c : " + entityId + " ~ Poa: " + Bukkit.getPlayer("Ekpoa").getEntityId());
                        glow = false;
                    }

                    //Bukkit.broadcastMessage("§cFinal Glow state: " + glow + " : " + player.getName());


                    List<Object> packedItems = (List<Object>) packedItemsMethod.invoke(msg);

                    for (int i = 0; i < packedItems.size(); i++) {
                        Object dataValue = packedItems.get(i);

                        if ((int) getIdMethod.invoke(dataValue) != 0)
                            continue;

                        byte value = (byte) getValueMethod.invoke(dataValue);

                        if (glow) {
                          //  Bukkit.broadcastMessage("checking to add :" + player.getName());
                            if (((value & 0x40) == 0)) {
                             //   Bukkit.broadcastMessage("added :" + player.getName());
                                value |= 0x40;
                            }
                        } else {
                            //Bukkit.broadcastMessage("checking to remove :" + player.getName());
                            if (((value & 0x40) != 0)) {
                            //    Bukkit.broadcastMessage("removed :" + player.getName());
                                value &= ~0x40;
                            }
                        }

                        packedItems.set(i, CommonClassMethodFields.dataValueConstructor.newInstance(0, dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), value));
                        msg = CommonClassMethodFields.metadataPacketConstructor.newInstance(entityId, packedItems);

                        break;

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.write(ctx, msg, promise);
    }

}
