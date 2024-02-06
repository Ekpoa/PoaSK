package poa.poask.effects.entity;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import poa.poask.util.reflection.EntityMetadata;
import poa.poask.util.reflection.common.SendPacket;
import poa.poask.util.reflection.SpawnEntityPacket;

import javax.annotation.Nullable;

public class SpawnFakeDroppedItem extends Effect {

    static {
        Skript.registerEffect(SpawnFakeDroppedItem.class, "spawn fake [dropped] item at %location% for %players% with id %number% with [item] %itemtype%");
    }

    @Override
    protected void execute(Event e) {
        Player[] players = playerExpression.getArray(e);
        Location location = locationExpression.getSingle(e);
        int id = integerExpression.getSingle(e).intValue();

        Object packet = SpawnEntityPacket.spawnEntityPacket(location, EntityType.DROPPED_ITEM, id);
        EntityMetadata entityMetadata = new EntityMetadata(id);
        entityMetadata.setItem(typeExpression.getSingle(e).getRandom());
        Object metadataPacket = entityMetadata.build();

        for(Player p : players) {
            SendPacket.sendPacket(p, packet);
            SendPacket.sendPacket(p, metadataPacket);
        }
    }



    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "spawn fake entity";
    }

    private Expression<Location> locationExpression;
    private Expression<Player> playerExpression;
    private Expression<Number> integerExpression;
    private Expression<ItemType> typeExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        locationExpression = (Expression<Location>) exprs[0];
        playerExpression = (Expression<Player>) exprs[1];
        integerExpression = (Expression<Number>) exprs[2];
        typeExpression = (Expression<ItemType>) exprs[3];

        return true;
    }
}
