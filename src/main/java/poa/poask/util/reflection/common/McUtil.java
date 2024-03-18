package poa.poask.util.reflection.common;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Utility class with Minecraft shortcuts
 */
public class McUtil {

    /**
     * Get a Minecraft BlockPos from a Bukkit Location
     *
     * @param location Location to convert
     * @return BlockPos from location
     */
    public static Object getBlockPos(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        try {
            return CommonClassMethodFields.BLOCK_POS_CONSTRUCT.newInstance(x, y, z);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a Minecraft Level from a Bukkit World
     *
     * @param world World to convert
     * @return Level from world
     */
    public static Object getLevel(World world) {
        try {
            return CommonClassMethodFields.CRAFT_WORLD_GET_HANDLE_METHOD.invoke(world);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send a GameTestMarker to player
     * <p>NOTE: This method was added in 1.20.2</p>
     *
     * @param player     Player to send packet to
     * @param location   Location to show marker
     * @param text       Text to appear above marker
     * @param color      Color of marker
     * @param durationMs Duration to show marker
     */
    public static void sendGameTestMarker(Player player, Location location, String text, Color color, int durationMs) {
        Object pos = getBlockPos(location);
        try {
            Object payload = CommonClassMethodFields.GAME_TEST_ADD_MARKER_DEBUG_PAYLOAD_CONSTRUCT.newInstance(pos, color.asARGB(), text, durationMs);
            Object packet = CommonClassMethodFields.CLIENTBOUND_CUSTOM_PAYLOAD_PACKET_CONSTRUCT.newInstance(payload);
            SendPacket.sendPacket(player, packet);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
