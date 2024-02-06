package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class FakeMultiBlockChange extends Effect {

    static{
        Skript.registerEffect(FakeMultiBlockChange.class, "fake multi block set %blocks% as %blockdata% for %players%");
    }


    @Override
    protected void execute(Event e) {
        BlockData blockData = block.getSingle(e);
        if(blockData == null) return;

        Map<Location, BlockData> map = new HashMap<>();
        for(Block b : blocks.getArray(e))
            map.put(b.getLocation(), blockData);

        for(Player p : players.getArray(e))
            p.sendMultiBlockChange(map, false);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "fake multi block set";
    }

    private Expression<Block> blocks;
    private Expression<BlockData> block;
    private Expression<Player> players;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        blocks = (Expression<Block>) exprs[0];
        block = (Expression<BlockData>) exprs[1];
        players = (Expression<Player>) exprs[2];
        return true;
    }
}
