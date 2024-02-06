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
import poa.poask.util.reflection.PosRotPackets;
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;


public class MoveEntity extends Effect implements Listener {

    static  {
        Skript.registerEffect(MoveEntity.class, "[send] move entity [packet] [with] id %number% by %number%[,] %number%[,] %number% for %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        for(Player p : playerExpression.getArray(e)){
            SendPacket.sendPacket(p, PosRotPackets.posPacket(idExpression.getSingle(e).intValue(),
                    integerExpression1.getSingle(e).shortValue(),
                    integerExpression2.getSingle(e).shortValue(),
                    integerExpression3.getSingle(e).shortValue()
            ));
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "move entity";
    }

    private Expression<Number> idExpression;
    private Expression<Number> integerExpression1;
    private Expression<Number> integerExpression2;
    private Expression<Number> integerExpression3;

    private Expression<Player> playerExpression;


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        idExpression = (Expression<Number>) exprs[0];
        integerExpression1 = (Expression<Number>) exprs[1];
        integerExpression2 = (Expression<Number>) exprs[2];
        integerExpression3 = (Expression<Number>) exprs[3];
        playerExpression = (Expression<Player>) exprs[4];
        return true;
    }
}
