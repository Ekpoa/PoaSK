package poa.poask.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.papermc.paper.event.player.PlayerTrackEntityEvent;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.units.qual.N;
import poa.poask.util.packetListener.ClientBlockChangeEvent;

import javax.annotation.Nullable;

public class ClientBlockChangeSkriptEvent extends SimpleEvent {

    static {
        Skript.registerEvent("Client Block Change", ClientBlockChangeSkriptEvent.class, ClientBlockChangeEvent.class, "client block change");
        EventValues.registerEventValue(ClientBlockChangeEvent.class, Block.class, new Getter<>() {
            @Override
            public @Nullable Block get(ClientBlockChangeEvent arg) {
                return arg.getBlock();
            }
        }, 0);
        EventValues.registerEventValue(ClientBlockChangeEvent.class, BlockData.class, new Getter<>() {

            @Override
            public @Nullable BlockData get(ClientBlockChangeEvent arg) {
                return arg.getNewState();
            }
        }, 0);
    }



}
