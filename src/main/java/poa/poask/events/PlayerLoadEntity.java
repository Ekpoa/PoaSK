package poa.poask.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.papermc.paper.event.player.PlayerTrackEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class PlayerLoadEntity extends SimpleEvent {

    static {
        Skript.registerEvent("Player Load Entity", PlayerLoadEntity.class, PlayerTrackEntityEvent.class, "player load entity");
        EventValues.registerEventValue(PlayerTrackEntityEvent.class, Entity.class, new Getter<>() {
            @Override
            public @Nullable Entity get(PlayerTrackEntityEvent arg) {
                return arg.getEntity();
            }
        }, 0);
    }



}
