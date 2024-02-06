package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skytasul.guardianbeam.Laser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import poa.poask.PoaSK;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GuardianLaser extends Effect {

    static {
        Skript.registerEffect(GuardianLaser.class, "create a guardian laser from %location% to %location% with id %string%");
    }

    public static Map<String, Laser> laserIDMap = new HashMap<>();

    @Override
    protected void execute(Event e) {
        Laser laser;
        String laserID = id.getSingle(e);
        Location location1 = loc1.getSingle(e);
        Location location2 = loc2.getSingle(e);
        if (location1 == null || location2 == null) {
            Bukkit.getLogger().log(Level.WARNING, "Location 1 = " + location1 + " Location 2 = " + location2 + " One is missing. For guardian laser");
            return;
        }
        if (laserIDMap.containsKey(laserID))
            return;
        try {
            laser = new Laser.GuardianLaser(loc1.getSingle(e), loc2.getSingle(e), Integer.MAX_VALUE, 90);
            laserIDMap.put(laserID, laser);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        laser.start(PoaSK.INSTANCE);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "guardian laser beam";
    }

    private Expression<Location> loc1;
    private Expression<Location> loc2;
    private Expression<String> id;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        loc1 = (Expression<Location>) exprs[0];
        loc2 = (Expression<Location>) exprs[1];
        id = (Expression<String>) exprs[2];
        return true;
    }
}
