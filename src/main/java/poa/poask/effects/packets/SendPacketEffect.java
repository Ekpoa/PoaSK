package poa.poask.effects.packets;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.util.reflection.EntityMetadata;
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;


public class SendPacketEffect extends Effect implements Listener {

    static {
        Skript.registerEffect(SendPacketEffect.class, "send packet %object% to %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        Object packet = packetExpression.getSingle(e);
        if(packet instanceof EntityMetadata metadata) {
            Object built = metadata.build();
            for (Player p : playerExpression.getArray(e))
                SendPacket.sendPacket(p, built);
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "send packet";
    }

    private Expression<Object> packetExpression;
    private Expression<Player> playerExpression;

    //Thanks Fusezion for fixing the console errors
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        packetExpression = LiteralUtils.defendExpression(exprs[0]); // might need to cast or you can change 'object' to '?' they'll be the same
        playerExpression = (Expression<Player>) exprs[1];
        return LiteralUtils.canInitSafely(packetExpression); // Once you defend it's been parsed and you can run this to ensure it's safe to continue
    }
}
