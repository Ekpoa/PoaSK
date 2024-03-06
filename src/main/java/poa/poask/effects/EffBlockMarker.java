package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import poa.poask.PoaSK;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Reflection;
import poa.poask.util.reflection.common.SendPacket;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class EffBlockMarker extends Effect {

    static {
        Skript.registerEffect(
                EffBlockMarker.class,
                "create debug marker at %location% with %number%[,] %number%[,] %number% and alpha %number% with text %string% for %players%");
        Bukkit.getMessenger().registerOutgoingPluginChannel(PoaSK.getInstance(), "minecraft:debug/game_test_add_marker");
    }

    private static final Class<?> markerClass = Reflection.getNMSClass("GameTestAddMarkerDebugPayload", "net.minecraft.network.protocol.common.custom");
    public static final Class<?> blockPosClass = Reflection.getNMSClass("BlockPosition", "net.minecraft.core");
    public static final Constructor<?> blockPosConstructor;
    private static final Constructor<?> markerConstructor;

    static {
        try {
            markerConstructor = markerClass.getDeclaredConstructor(blockPosClass, int.class, String.class, int.class);
            blockPosConstructor = blockPosClass.getDeclaredConstructor(int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    private Expression<Location> locationExpression;
    private Expression<Number> redExpression;
    private Expression<Number> greenExpression;
    private Expression<Number> blueExpression;
    private Expression<Number> alphaExpression;
    private Expression<String> idExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int i, @NotNull Kleenean kleenean, @NotNull ParseResult parseResult) {
        locationExpression = (Expression<Location>) exprs[0];
        redExpression = (Expression<Number>) exprs[1];
        greenExpression = (Expression<Number>) exprs[2];
        blueExpression = (Expression<Number>) exprs[3];
        alphaExpression = (Expression<Number>) exprs[4];
        idExpression = (Expression<String>) exprs[5];
        playerExpression = (Expression<Player>) exprs[6];
        return true;
    }

    @SneakyThrows
    @Override
    protected void execute(@NotNull Event event) {
        if (playerExpression == null) return;
        Player[] players = playerExpression.getArray(event);
        if (players.length == 0) return;
        org.bukkit.Color color = Color.fromARGB(alphaExpression.getSingle(event).intValue(), redExpression.getSingle(event).intValue(), greenExpression.getSingle(event).intValue(), blueExpression.getSingle(event).intValue());
        String name = idExpression.getSingle(event);

        Location location = locationExpression.getSingle(event);
        Object blockPos = blockPosConstructor.newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        Object payload = markerConstructor.newInstance(blockPos, color.asARGB(), name, Integer.MAX_VALUE);

        Object packet = EffBrand.packetConstructor.newInstance(payload);

        for (Player player : players) {
            SendPacket.sendPacket(player, packet);
        }
    }

    @Override
    public @NotNull String toString(Event event, boolean debug) {
        String loc = this.locationExpression.toString(event,debug);
        String green = this.greenExpression.toString(event,debug);
        String alpha = this.alphaExpression.toString(event, debug);
        String id = this.idExpression.toString(event,debug);
        String player = this.playerExpression.toString(event,debug);
        return "create debug marker at " + loc + " green amount " + green + " and " + alpha + " alpha wiht text " + id + " for " + player;
    }

    private void writeLocation(ByteBuf buffer, Location location) {
        buffer.writeLong(
                (((long) Math.floor(location.getX())) & 67108863L) << 38 |
                        ((long) Math.floor(location.getY())) & 4095L |
                        (((long) Math.floor(location.getZ())) & 67108863L) << 12
        );
    }

    private void writeString(ByteBuf buffer, String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        buffer.writeByte(bytes.length);
        buffer.writeBytes(bytes);
    }

}
