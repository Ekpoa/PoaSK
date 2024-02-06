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
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockInChunk extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(BlockInChunk.class, Location.class, ExpressionType.COMBINED, "locations of %itemtypes% in %chunk%");
    }

    @Override
    protected @Nullable Location[] get(Event e) {
        ItemType[] type = itemTypeExpression.getArray(e);
        List<Material> materials = Arrays.stream(type).map(ItemType::getMaterial).toList();


        Chunk chunk = chunkExpression.getSingle(e);
        

        List<Location> locList = new ArrayList<>();


        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;

        World world = chunk.getWorld();

        int minHeight = world.getMinHeight();
        int maxHeight = world.getMaxHeight();

        for (int xx = x; xx < (x + 16); xx++)
            for (int zz = z; zz < (z + 16); zz++)
                for (int yy = minHeight; yy < maxHeight; yy++) {
                    Block block = world.getBlockAt(xx, yy, zz);
                    Material material = block.getType();
                    if (materials.contains(material))
                        locList.add(block.getLocation());
                }

        return locList.toArray(new Location[0]);
    }


    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "location of block";
    }

    private Expression<ItemType> itemTypeExpression;
    private Expression<Chunk> chunkExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        itemTypeExpression = (Expression<ItemType>) exprs[0];
        chunkExpression = (Expression<Chunk>) exprs[1];

        return true;
    }
}
