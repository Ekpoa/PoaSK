package poa.poask.effects.entity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import poa.poask.util.reflection.RemoveEntityPacket;
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RemoveFakeEntity extends Effect {

    static {
        Skript.registerEffect(RemoveFakeEntity.class, "remove fake entity with id %numbers% for %players%");
    }

    @Override
    protected void execute(Event e) {
        Player[] players = playerExpression.getArray(e);
        List<Integer> list = new ArrayList<>();
        for(Number n : integerExpression.getArray(e))
            list.add(n.intValue());

        Object packet = RemoveEntityPacket.removeEntityPacket(list.stream().mapToInt(Integer::intValue).toArray());

        for(Player p : players)
            SendPacket.sendPacket(p, packet);

    }



    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "remove fake entity";
    }
    private Expression<Number> integerExpression;
    private Expression<Player> playerExpression;


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        integerExpression = (Expression<Number>) exprs[0];
        playerExpression = (Expression<Player>) exprs[1];
        return true;
    }
}
