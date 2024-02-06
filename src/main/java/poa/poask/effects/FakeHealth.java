package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class FakeHealth extends Effect {

    static {
        Skript.registerEffect(FakeHealth.class, "fake %player%'s health to %number%");
    }

    @Override
    protected void execute(Event e) {
        Player p = player.getSingle(e);
        p.sendHealthUpdate(number.getSingle(e).intValue(), p.getFoodLevel(), p.getSaturation());
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Fake Health";
    }

    private Expression<Player> player;
    private Expression<Number> number;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        number = (Expression<Number>) exprs[1];
        return true;
    }
}
