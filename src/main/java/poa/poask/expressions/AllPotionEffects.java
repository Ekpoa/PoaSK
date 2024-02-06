package poa.poask.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllPotionEffects extends SimpleExpression {

    static {
        Skript.registerExpression(AllPotionEffects.class, PotionEffectType.class, ExpressionType.SIMPLE, "all potion types");
    }

    @Override
    protected PotionEffectType[] get(Event e) {
        List<PotionEffectType> tr = new ArrayList<>(Arrays.asList(PotionEffectType.values()));
        return tr.toArray(new PotionEffectType[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class getReturnType() {
        return PotionEffectType.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all potion effect types";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
