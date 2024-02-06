package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nullable;

public class FakeItem extends Effect {

    static {
        Skript.registerEffect(FakeItem.class, "fake %livingentity%'s equipment slot %string% to %itemtype% for %players%");
    }

    @Override
    protected void execute(Event e) {
        if(entity == null || players == null) return;
        for (Player p : players.getAll(e))
            p.sendEquipmentChange(entity.getSingle(e), EquipmentSlot.valueOf(slot.getSingle(e).toUpperCase()), item.getSingle(e).getRandom());
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "fakes inventory";
    }

    private Expression<LivingEntity> entity;
    private Expression<String> slot;
    private Expression<ItemType> item;
    private Expression<Player> players;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entity = (Expression<LivingEntity>) exprs[0];
        slot = (Expression<String>) exprs[1];
        item = (Expression<ItemType>) exprs[2];
        players = (Expression<Player>) exprs[3];

        return true;
    }
}
