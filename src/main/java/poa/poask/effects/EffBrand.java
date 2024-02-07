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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poask.PoaSK;
import poa.poask.util.reflection.common.Reflection;
import poa.poask.util.reflection.common.SendPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EffBrand extends Effect {

    public static Class<?> brandPayloadClass = Reflection.getNMSClass("BrandPayload", "net.minecraft.network.protocol.common.custom");
    public static Class<?> payloadPacketClass = Reflection.getNMSClass("ClientboundCustomPayloadPacket", "net.minecraft.network.protocol.common");
    public static Class<?> customPacketPayloadClass = Reflection.getNMSClass("CustomPacketPayload", "net.minecraft.network.protocol.common.custom");
    public static Constructor<?> brandConstructor;
    public static Constructor<?> packetConstructor;

    static {
        Skript.registerEffect(EffBrand.class, "set server brand to %string% for %players%");
        try {
            brandConstructor = brandPayloadClass.getDeclaredConstructor(String.class);
            packetConstructor = payloadPacketClass.getDeclaredConstructor(customPacketPayloadClass);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    protected void execute(Event event) {
        if (List.of("1193", "1194", "1200", "1201", "1202").contains(Bukkit.getMinecraftVersion().replaceAll("[.]", ""))) {
            setBrandOld(event);
            return;
        }
        setBrandNew(event);
    }

    @SneakyThrows
    public void setBrandNew(Event event) {
        String brand = string.getSingle(event);

        Object brandPayload = brandConstructor.newInstance(brand);
        Object packet = packetConstructor.newInstance(brandPayload);

        for (Player p : players.getArray(event)) {
            SendPacket.sendPacket(p, packet);
        }

    }

    public void setBrandOld(Event event) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(PoaSK.getInstance(), "minecraft:brand");
        if (string.getSingle(event) == null) return;
        String s = string.getSingle(event).replace("&", "§");
        ByteBuf buffer = Unpooled.buffer();
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        buffer.writeByte(bytes.length);
        buffer.writeBytes(bytes);
        for (Player p : players.getArray(event)) {
            Class<?> craftPlayer = Reflection.getOBCClass("entity.CraftPlayer");

            try {
                Method channelMethod = craftPlayer.getDeclaredMethod("addChannel", String.class);
                channelMethod.invoke(p, "minecraft:brand");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }

            p.sendPluginMessage(PoaSK.getInstance(), "minecraft:brand", buffer.array());
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String string = this.string.toString(event, debug);
        String player = this.players.toString(event, debug);
        return "set server brand to " + string + " for " + player;
    }

}
