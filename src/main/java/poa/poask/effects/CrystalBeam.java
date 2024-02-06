package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skytasul.guardianbeam.Laser;
import org.bukkit.Location;
import org.bukkit.event.Event;
import poa.poask.PoaSK;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CrystalBeam extends Effect {

    static {
        Skript.registerEffect(CrystalBeam.class, "create a crystal beam from %location% to %location% for %number% with id %string%");
    }

    public static Map<String, Laser> crystalIDMap = new HashMap<>();

    @Override
    protected void execute(Event e) {
        Laser laser;
        String laserID = id.getSingle(e);
        if (crystalIDMap.containsKey(laserID))
            return;
        try {
            laser = new Laser.CrystalLaser(loc1.getSingle(e), loc2.getSingle(e), seconds.getSingle(e).intValue(), 140);
            crystalIDMap.put(laserID, laser);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        laser.start(PoaSK.INSTANCE);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "crystal laser beam";
    }

    private Expression<Location> loc1;
    private Expression<Location> loc2;
    private Expression<Number> seconds;
    private Expression<String> id;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        loc1 = (Expression<Location>) exprs[0];
        loc2 = (Expression<Location>) exprs[1];
        seconds = (Expression<Number>) exprs[2];
        id = (Expression<String>) exprs[3];
        return true;
    }
}
