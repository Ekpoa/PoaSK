package poa.poask.util.packetListener;

import lombok.Getter;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerEvent;

public class ClientBlockChangeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @Getter
    private final Block block;
    @Getter
    private final BlockData newState;

    public ClientBlockChangeEvent(Player player, Block block, BlockData newState) {
        super(player, true);
        this.block = block;
        this.newState = newState;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}