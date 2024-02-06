package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.util.reflection.EntityMetadata;
import poa.poask.util.reflection.GlowPacket;
import poa.poask.util.reflection.common.SendPacket;
import poa.poask.util.reflection.TeamPacket;

import javax.annotation.Nullable;
import java.util.List;


public class GlowEffectOff extends Effect implements Listener {

    static  {
        Skript.registerEffect(GlowEffectOff.class, "make %entity% not glow for %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        Entity entity = entityExpression.getSingle(e);
        String uuid = entity.getUniqueId().toString();

        if(entity instanceof Player player)
            uuid = player.getName();

        EntityMetadata packet = new EntityMetadata(entity.getEntityId());
        packet.setGlow(false);
        packet.build();

        for(Player p : playerExpression.getArray(e)){

            SendPacket.sendPacket(p, packet);
            SendPacket.sendPacket(p, TeamPacket.teamPacketForGlow(uuid, "white", List.of(uuid)));
        }
    }



    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set glow off using packets";
    }

    private Expression<Entity> entityExpression;
    private Expression<Player> playerExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        playerExpression = (Expression<Player>) exprs[1];
        return true;
    }
}
