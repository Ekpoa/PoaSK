package poa.poask.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class Hostname extends SimpleExpression implements @NotNull Listener {



    static {
        Skript.registerExpression(Hostname.class, PotionEffectType.class, ExpressionType.SIMPLE, "%player%'s hostname");
    }

    @Override
    protected String[] get(Event e) {
        return new String[]{hostnameMap.get(playerExpression.getSingle(e).getUniqueId())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "connection hostname";
    }

    private Expression<Player> playerExpression;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        return true;
    }

    public static Map<UUID, String> hostnameMap = new HashMap<>();
    @EventHandler
    public void onConnect(AsyncPlayerPreLoginEvent e){
        hostnameMap.put(e.getUniqueId(), e.getHostname());
    }

    @EventHandler
    public void connectClose(PlayerConnectionCloseEvent e){
        hostnameMap.remove(e.getPlayerUniqueId());
    }


}
