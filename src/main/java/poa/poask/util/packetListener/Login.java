package poa.poask.util.packetListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import poa.poask.util.reflection.common.Letters;

import java.util.HashMap;
import java.util.Map;

public class Login implements Listener {

    private static final Map<Player, PacketInjector> playerPacketStuffMap = new HashMap<>();


    @EventHandler
    public void connect(PlayerLoginEvent e) {
        if (!Letters.getBukkitVersion().equalsIgnoreCase("1202"))
            return;
        PacketInjector packetStuff = new PacketInjector(e.getPlayer(), e.getAddress());
        packetStuff.injectPlayer();
        playerPacketStuffMap.put(e.getPlayer(), packetStuff);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (playerPacketStuffMap.containsKey(player))
            playerPacketStuffMap.get(player).uninjectPlayer();
    }


}
