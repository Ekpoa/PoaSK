package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.util.reflection.GlowPacket;
import poa.poask.util.reflection.common.SendPacket;
import poa.poask.util.reflection.TeamPacket;

import javax.annotation.Nullable;
import java.util.*;


public class GlowEffect extends Effect implements Listener {

    static  {
        Skript.registerEffect(GlowEffect.class, "make %entities% glow %color% for %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        Color color = colorExpression.getSingle(e);
        Entity[] entities = entityExpression.getArray(e);
        if (color == null) return;
        String chatColor = ((SkriptColor) color).asChatColor().name();

        for(Entity entity : entities) {
            String uuid = entity.getUniqueId().toString();
            if (entity instanceof Player player)
                uuid = player.getName();

            for (Player p : playerExpression.getArray(e)) {
                SendPacket.sendPacket(p, GlowPacket.glowPacket(entity, true));
                SendPacket.sendPacket(p, TeamPacket.teamPacketForGlow(uuid, chatColor, List.of(uuid)));
            }
        }
    }




//    public static PacketContainer glowPacket(Entity entity, boolean on){
//        PacketContainer packet = PoaSk.protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
//        packet.getIntegers().write(0, entity.getEntityId());
//        WrappedDataWatcher watcher = new WrappedDataWatcher();
//        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
//        watcher.setEntity(entity);
//        if (on)
//            watcher.setObject(0, serializer, (byte) 0x40);
//        else
//            watcher.setObject(0, serializer, (byte) 0);
//        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
//        return packet;
//    }

//    public static PacketContainer teamPacket(Entity entity, String chatColor){
//        PacketContainer packet = PoaSk.protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
//        packet.getIntegers().write(0, 0);
//
//        packet.getStrings().write(0, entity.getUniqueId().toString());
//        String members;
//        if(entity instanceof Player player)
//            members = player.getName();
//        else
//            members = entity.getUniqueId().toString();
//        packet.getSpecificModifier(Collection.class).write(0, List.of(members));
//        packet.getSpecificModifier(Optional.class).write(0, Optional.of(new WrappedTeamParameters(ChatFormatting.valueOf(chatColor)).asNMS()));
//        return packet;
//    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set glow using packets";
    }

    private Expression<Entity> entityExpression;
    private Expression<Color> colorExpression;
    private Expression<Player> playerExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        colorExpression = (Expression<Color>) exprs[1];
        playerExpression = (Expression<Player>) exprs[2];
        return true;
    }
}
