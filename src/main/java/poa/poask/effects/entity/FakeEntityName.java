package poa.poask.effects.entity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.util.reflection.EntityMetadata;
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;


public class FakeEntityName extends Effect implements Listener {

    static {
        Skript.registerEffect(FakeEntityName.class, "fake %entity%'s name to %string% for %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        for (Player p : playerExpression.getArray(e)) {
            Entity entity = entityExpression.getSingle(e);
            boolean invisible = !entity.isVisibleByDefault();


            if(entity instanceof LivingEntity li)
                invisible = li.isInvisible();

            SendPacket.sendPacket(p, EntityMetadata.basePacketForEntity(entity.getEntityId(), entity.isVisualFire(), invisible, entity.isGlowing(), stringExpression.getSingle(e), entity.isCustomNameVisible(), entity.isSilent(), entity.hasGravity(), "STANDING"));
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "metadata";
    }

    private Expression<Entity> entityExpression;
    private Expression<String> stringExpression;
    private Expression<Player> playerExpression;


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        stringExpression = (Expression<String>) exprs[1];
        playerExpression = (Expression<Player>) exprs[2];
        return true;
    }
}
