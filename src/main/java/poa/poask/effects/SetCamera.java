package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.util.reflection.CameraPacket;
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;


public class SetCamera extends Effect implements Listener {

    static  {
        Skript.registerEffect(SetCamera.class, "[set] camera [packet] of %player% to entity [with] id %number%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        SendPacket.sendPacket(playerExpression.getSingle(e), CameraPacket.packet(idExpression.getSingle(e).intValue()));
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "camera";
    }

    private Expression<Number> idExpression;
    private Expression<Player> playerExpression;


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        idExpression = (Expression<Number>) exprs[1];
        playerExpression = (Expression<Player>) exprs[0];
        return true;
    }
}
