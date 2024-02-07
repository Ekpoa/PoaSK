package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import poa.poask.PoaSK;
import poa.poask.util.reflection.common.Reflection;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class EffBlockMarker extends Effect {

    static {
        Skript.registerEffect(
                EffBlockMarker.class,
                "create debug marker at %location% green amount %number% and %number% alpha with text %string% for %players%");
        Bukkit.getMessenger().registerOutgoingPluginChannel(PoaSK.getInstance(), "minecraft:debug/game_test_add_marker");
    }

    private Expression<Location> locationExpression;
    private Expression<Number> greenExpression;
    private Expression<Number> alphaExpression;
    private Expression<String> idExpression;
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int i, @NotNull Kleenean kleenean, @NotNull ParseResult parseResult) {
        locationExpression = (Expression<Location>) exprs[0];
        greenExpression = (Expression<Number>) exprs[1];
        alphaExpression = (Expression<Number>) exprs[2];
        idExpression = (Expression<String>) exprs[3];
        playerExpression = (Expression<Player>) exprs[4];
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        if (playerExpression == null) return;
        Player[] players = playerExpression.getArray(event);
        if (players.length == 0) return;
        Color color = new Color(0, greenExpression.getSingle(event).intValue(), 0, alphaExpression.getSingle(event).intValue());
        String name = idExpression.getSingle(event);
        for (Player player : players) {
            ByteBuf buf = Unpooled.buffer();
            writeLocation(buf, locationExpression.getSingle(event));
            buf.writeInt(color.getRGB());
            writeString(buf, name);
            int number = Integer.MAX_VALUE;
            buf.writeInt(number);

            Class<?> craftPlayer = Reflection.getOBCClass("entity.CraftPlayer");

            try {
                Method channelMethod = craftPlayer.getDeclaredMethod("addChannel", String.class);
                channelMethod.invoke(player, "minecraft:debug/game_test_add_marker");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }


            //((CraftPlayer) player).addChannel("minecraft:debug/game_test_add_marker");


            player.sendPluginMessage(PoaSK.getInstance(), "minecraft:debug/game_test_add_marker", buf.array());
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
