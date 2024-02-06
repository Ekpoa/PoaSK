package poa.poask.effects.entity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.util.reflection.HeadRotPacket;
import poa.poask.util.reflection.PosRotPackets;
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;


public class RotateEntity extends Effect implements Listener {

    static {
        Skript.registerEffect(RotateEntity.class, "[send] rotate entity [packet] [with] id %number% with yaw %number% [and] pitch %number% for %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        int id = idExpression.getSingle(e).intValue();
        int yaw = integerExpression1.getSingle(e).intValue();
        int pitch = integerExpression2.getSingle(e).intValue();
        for (Player p : playerExpression.getArray(e)) {
            SendPacket.sendPacket(p, PosRotPackets.rotPacket(id,
                    yaw,
                    pitch
            ));
            SendPacket.sendPacket(p, HeadRotPacket.headRotPacket(id, yaw));
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "rotate entity";
    }

    private Expression<Number> idExpression;
    private Expression<Number> integerExpression1;
    private Expression<Number> integerExpression2;

    private Expression<Player> playerExpression;


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        idExpression = (Expression<Number>) exprs[0];
        integerExpression1 = (Expression<Number>) exprs[1];
        integerExpression2 = (Expression<Number>) exprs[2];
        playerExpression = (Expression<Player>) exprs[3];
        return true;
    }
}
