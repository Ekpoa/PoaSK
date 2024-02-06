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
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;


public class FakeHeadRotation extends Effect implements Listener {

    static  {
        Skript.registerEffect(FakeHeadRotation.class, "[send] head rotation [packet] [with] id %number% with yaw %number% for %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        for(Player p : playerExpression.getArray(e)){
            SendPacket.sendPacket(p, HeadRotPacket.headRotPacket(idExpression.getSingle(e).intValue(), integerExpression1.getSingle(e).intValue()));
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "head rot packet";
    }

    private Expression<Number> idExpression;
    private Expression<Number> integerExpression1;
    private Expression<Player> playerExpression;


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        idExpression = (Expression<Number>) exprs[0];
        integerExpression1 = (Expression<Number>) exprs[1];
        playerExpression = (Expression<Player>) exprs[2];
        return true;
    }
}
