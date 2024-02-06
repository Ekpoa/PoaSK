package poa.poask.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityID extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(EntityID.class, Integer.class, ExpressionType.SIMPLE, "entity id of %entity%");
    }

    @Override
    protected @Nullable Integer[] get(Event e) {
        return new Integer[]{entityExpression.getSingle(e).getEntityId()};
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "entity id";
    }

    private Expression<Entity> entityExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];

        return true;
    }
}
