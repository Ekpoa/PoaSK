package poa.poask.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import poa.poask.util.reflection.EntityMetadata;

import javax.annotation.Nullable;

public class MetadataVar extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(MetadataVar.class, Object.class, ExpressionType.SIMPLE, "[raw] metadata packet with id %number%");
    }

    @Override
    protected @Nullable Object[] get(Event e) {
        return new Object[]{new EntityMetadata(numberExpression.getSingle(e).intValue())};
    }



    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "raw metadata";
    }

    private Expression<Number> numberExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        numberExpression = (Expression<Number>) exprs[0];
        return true;
    }
}
