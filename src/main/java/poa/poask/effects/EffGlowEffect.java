package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poask.util.reflection.GlowPacket;
import poa.poask.util.reflection.TeamPacket;
import poa.poask.util.reflection.common.SendPacket;

import java.util.List;

public class EffGlowEffect extends Effect implements Listener {

    static {
        Skript.registerEffect(EffGlowEffect.class,
                "make %entities% glow %color% for %players%",
                "make %entities% not glow for %players%");
    }

    private int pattern;
    private Expression<Entity> entities;
    private Expression<Color> color;
    private Expression<Player> players;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.pattern = matchedPattern;
        entities = (Expression<Entity>) exprs[0];
        if (matchedPattern == 0) {
            color = (Expression<Color>) exprs[1];
            players = (Expression<Player>) exprs[2];
        } else {
            players = (Expression<Player>) exprs[1];
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @SneakyThrows
    @Override

    protected void execute(Event event) {
        Color color = this.color.getSingle(event);
        String chatColor = "white";
        if (color != null)
            chatColor = ((SkriptColor) color).asChatColor().name();



        for (Entity entity : this.entities.getArray(event)) {
            String uuid = entity.getUniqueId().toString();
            if (entity instanceof Player player)
                uuid = player.getName();

            for (Player player : players.getArray(event)) {
                SendPacket.sendPacket(player, GlowPacket.glowPacket(entity, pattern == 0));
                SendPacket.sendPacket(player, TeamPacket.teamPacketForGlow(uuid, chatColor, List.of(uuid)));
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String entity = this.entities.toString(event, debug);
        String player = this.players.toString(event, debug);
        if (pattern == 0) {
            String color = this.color.toString(event, debug);
            return "make " + entity + " glow " + color + " for " + player;
        }
        return "make " + entity + " not glow for " + player;
    }

}
