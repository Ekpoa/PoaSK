package poa.poask.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poask.PoaSK;
import poa.poask.util.reflection.FakePlayer;

import java.util.*;
import java.util.logging.Level;

public class ExprEntityIDFromFakePlayer extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprEntityIDFromFakePlayer.class, Integer.class, ExpressionType.COMBINED,
                "entity id of fake player named %string% with player %player%");
    }

    private Expression<String> name;
    private Expression<Player> player;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        player = (Expression<Player>) exprs[1];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable Integer[] get(Event event) {
        Player player = this.player.getSingle(event);
        String name = this.name.getSingle(event);

        if(!FakePlayer.playerMapMap.containsKey(player)){
            PoaSK.getInstance().getLogger().log(Level.WARNING, "Could not find player in the map");
            return null;
        }
        Map<UUID, Integer> innerMap = FakePlayer.playerMapMap.get(player);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

        UUID uuid = offlinePlayer.getUniqueId();
        if(!innerMap.containsKey(uuid)){
            PoaSK.getInstance().getLogger().log(Level.WARNING, "Could not find fake player uuid in the map");
            return null;
        }

        return new Integer[]{innerMap.get(uuid)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {

        return "entity id of fake player";
    }

}
