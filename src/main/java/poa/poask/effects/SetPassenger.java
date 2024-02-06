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
import poa.poask.util.reflection.common.SendPacket;
import poa.poask.util.reflection.SetPassengerPacket;

import javax.annotation.Nullable;
import java.util.Arrays;


public class SetPassenger extends Effect implements Listener {

    static  {
        Skript.registerEffect(SetPassenger.class, "make entit[ies|y] with id[s] %numbers% ride entity with id %number% for %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        int[] passengers = Arrays.stream(passengersExpression.getArray(e)).mapToInt(i -> i.intValue()).toArray();
        int vehicle = vehicleExpression.getSingle(e).intValue();
        for(Player p : playerExpression.getArray(e)) {
            SendPacket.sendPacket(p, SetPassengerPacket.setPassengerPacket(vehicle, passengers));
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set passenger";
    }

    private Expression<Number> passengersExpression;
    private Expression<Number> vehicleExpression;
    private Expression<Player> playerExpression;


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        passengersExpression = (Expression<Number>) exprs[0];
        vehicleExpression = (Expression<Number>) exprs[1];
        playerExpression = (Expression<Player>) exprs[2];
        return true;
    }
}
