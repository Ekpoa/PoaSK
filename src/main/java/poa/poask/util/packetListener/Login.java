package poa.poask.util.packetListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import poa.poask.effects.EffGlowEffect;
import poa.poask.util.reflection.FakePlayer;
import poa.poask.util.reflection.common.Letters;

import java.util.*;

public class Login implements Listener {

    private static final Map<Player, PacketInjector> playerPacketStuffMap = new HashMap<>();


    @EventHandler
    public void connect(PlayerLoginEvent e) {
        if (!List.of("1202", "1203", "1204").contains(Letters.getBukkitVersion()))
            return;

        PacketInjector packetStuff = new PacketInjector(e.getPlayer(), e.getAddress());
        packetStuff.injectPlayer();
        playerPacketStuffMap.put(e.getPlayer(), packetStuff);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        FakePlayer.playerMapMap.remove(player);

        EffGlowEffect.glowMap.remove(player);


        EffGlowEffect.glowMap.entrySet().removeIf(entry -> entry.getValue().contains(player.getEntityId()));


        if (playerPacketStuffMap.containsKey(player)) {
            playerPacketStuffMap.get(player).uninjectPlayer();
            playerPacketStuffMap.remove(player);
        }
    }


}
