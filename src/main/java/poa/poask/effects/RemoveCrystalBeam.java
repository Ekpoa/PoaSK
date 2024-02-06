package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skytasul.guardianbeam.Laser;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class RemoveCrystalBeam extends Effect {

    static {
        Skript.registerEffect(RemoveCrystalBeam.class, "remove crystal beam with id %string%");
    }


    @Override
    protected void execute(Event e) {
        String laserID = id.getSingle(e);
        if(!CrystalBeam.crystalIDMap.containsKey(laserID))
            return;
        Laser laser = CrystalBeam.crystalIDMap.get(laserID);
        laser.stop();
        CrystalBeam.crystalIDMap.remove(laserID);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "remove crystal laser beam";
    }

    private Expression<String> id;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        id = (Expression<String>) exprs[0];
        return true;
    }
}
