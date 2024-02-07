package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import fr.skytasul.guardianbeam.Laser;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffGuardianLaserRemove extends Effect {

    static {
        Skript.registerEffect(EffGuardianLaserRemove.class, "remove guardian laser with id %string%");
    }

    private Expression<String> id;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        id = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String laserID = id.getSingle(e);
        if (!EffGuardianLaserCreate.laserIDMap.containsKey(laserID))
            return;
        Laser laser = EffGuardianLaserCreate.laserIDMap.get(laserID);
        laser.stop();
        EffGuardianLaserCreate.laserIDMap.remove(laserID);

    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "remove guardian laser beam with id " + this.id.toString(event, debug);
    }

}
