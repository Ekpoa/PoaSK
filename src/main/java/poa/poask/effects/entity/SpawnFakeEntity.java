package poa.poask.effects.entity;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import poa.poask.util.reflection.common.SendPacket;
import poa.poask.util.reflection.SpawnEntityPacket;

import javax.annotation.Nullable;

public class SpawnFakeEntity extends Effect {

    static {
        Skript.registerEffect(SpawnFakeEntity.class, "spawn fake %entitytype% at %location% for %players% with id %number%");
    }

    @Override
    protected void execute(Event e) {
        Player[] players = playerExpression.getArray(e);
        Location location = locationExpression.getSingle(e);
        EntityType entityType = EntityUtils.toBukkitEntityType(entityTypeExpression.getSingle(e).data);
        int id = integerExpression.getSingle(e).intValue();

        Object packet = SpawnEntityPacket.spawnEntityPacket(location, entityType, id);

        for(Player p : players)
            SendPacket.sendPacket(p, packet);
    }



    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "spawn fake entity";
    }

    private Expression<ch.njol.skript.entity.EntityType> entityTypeExpression;
    private Expression<Location> locationExpression;
    private Expression<Player> playerExpression;
    private Expression<Number> integerExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityTypeExpression = (Expression<ch.njol.skript.entity.EntityType>) exprs[0];
        locationExpression = (Expression<Location>) exprs[1];
        playerExpression = (Expression<Player>) exprs[2];
        integerExpression = (Expression<Number>) exprs[3];

        return true;
    }
}
