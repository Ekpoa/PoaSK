package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skytasul.guardianbeam.Laser;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class RemoveGuardianLaser extends Effect {

    static {
        Skript.registerEffect(RemoveGuardianLaser.class, "remove guardian laser with id %string%");
    }


    @Override
    protected void execute(Event e) {
        String laserID = id.getSingle(e);
        if(!GuardianLaser.laserIDMap.containsKey(laserID))
            return;
        Laser laser = GuardianLaser.laserIDMap.get(laserID);
        laser.stop();
        GuardianLaser.laserIDMap.remove(laserID);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "remove guardian laser beam";
    }

    private Expression<String> id;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        id = (Expression<String>) exprs[0];
        return true;
    }
}
