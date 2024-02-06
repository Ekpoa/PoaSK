package poa.poask.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AllLivingEntities extends SimpleExpression {

    static {
        Skript.registerExpression(AllLivingEntities.class, PotionEffectType.class, ExpressionType.SIMPLE, "all living entity types");
    }

    @Override
    protected EntityType[] get(Event e) {
        List<EntityType> tr = new ArrayList<>();
        for (EntityType et : EntityType.values())
            if (et.isAlive() && et.isSpawnable())
                tr.add(et);
        return tr.toArray(new EntityType[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class getReturnType() {
        return EntityType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all living entity types";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
