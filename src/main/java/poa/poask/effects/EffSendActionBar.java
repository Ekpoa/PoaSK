package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poask.util.packetListener.ActionBarEvent;
import poa.poask.util.reflection.ActionBarPacket;
import poa.poask.util.reflection.CameraPacket;
import poa.poask.util.reflection.common.SendPacket;

public class EffSendActionBar extends Effect implements Listener {

    static {
        Skript.registerEffect(EffSendActionBar.class, "send action bar packet %string% to %players%");
    }

    private Expression<String> string;
    private Expression<Player> players;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        string = (Expression<String>) exprs[0];
        players = (Expression<Player>) exprs[1];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @SneakyThrows
    @Override
    protected void execute(Event event) {
        String string = this.string.getSingle(event);
        if (string == null) return;

        Object packet = ActionBarPacket.actionBar(string);

        for (Player player : this.players.getArray(event))
            SendPacket.sendPacket(player, packet);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String players = this.players.toString(event, debug);
        String string = this.string.toString(event, debug);
        return "send packet action bar" + string + " to " + players;
    }

}
