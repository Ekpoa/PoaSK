package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.PoaSK;
import poa.poask.util.reflection.ClientBoundInfoUpdatePacketUsername;
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;


public class Username extends Effect implements Listener {

    static {
        Skript.registerEffect(Username.class, "set username of %player% to %string% for %players%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        Player player = playerExpression.getSingle(e);
        String username = stringExpression.getSingle(e).replace("&", "§");

        if(username.length() > 16)
            return;

        for (Player p : playersExpression.getArray(e)) {
            if (p == player)
                continue;
            p.hidePlayer(PoaSK.INSTANCE, player);
            SendPacket.sendPacket(p, ClientBoundInfoUpdatePacketUsername.usernamePacket(player, username));
            Bukkit.getScheduler().runTaskLater(PoaSK.INSTANCE, () -> p.showPlayer(PoaSK.INSTANCE, player), 2L);
        }


    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set username using packets";
    }

    private Expression<Player> playerExpression;
    private Expression<String> stringExpression;
    private Expression<Player> playersExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        stringExpression = (Expression<String>) exprs[1];
        playersExpression = (Expression<Player>) exprs[2];
        return true;
    }
}
