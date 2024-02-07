package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poask.util.reflection.common.Letters;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.List;

public class EffChangeIP extends Effect {

    public static final Class<?> serverPlayerClass;
    public static final Class<?> connectClass;
    public static final Class<?> connectionClass;
    public static Field connectionField;
    public static Field connection2;
    public static Field addressField;

    static {
        Skript.registerEffect(EffChangeIP.class, "set ip of %player% to %string% with port %number%");
        serverPlayerClass = Reflection.getNMSClass("EntityPlayer", "net.minecraft.server.level");

        if (List.of("1202", "1203", "1204").contains(Letters.getBukkitVersion()))
            connectClass = Reflection.getNMSClass("ServerCommonPacketListenerImpl", "net.minecraft.server.network");
        else
            connectClass = Reflection.getNMSClass("PlayerConnection", "net.minecraft.server.network");
        connectionClass = Reflection.getNMSClass("NetworkManager", "net.minecraft.network");

        try {
            connectionField = serverPlayerClass.getField(Letters.connectionField);
            connectionField.setAccessible(true);
            connection2 = connectClass.getDeclaredField(Letters.connectionField2);
            connection2.setAccessible(true);
            addressField = connectionClass.getDeclaredField(Letters.addressField);
            addressField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private Expression<Player> playerExpression;
    private Expression<String> stringExpression;
    private Expression<Number> numberExpression;
    //private Expression<Player> playerListExpression;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        stringExpression = (Expression<String>) exprs[1];
        numberExpression = (Expression<Number>) exprs[2];
        //playerListExpression = (Expression<Player>) exprs[2];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        Player player = playerExpression.getSingle(event);
        String ipString = stringExpression.getSingle(event);
        Number portNum = numberExpression.getSingle(event);
        if (player == null || ipString == null || portNum == null) return;

        int port = portNum.intValue();
        try {
            Object p = Reflection.getHandle(player);
            Object connectionObject = connectionField.get(p);
            Object connectionField2 = connection2.get(connectionObject);
//            Object methodInvoke = connectionField2.invoke(connectionObject);

            addressField.set(connectionField2, new InetSocketAddress(ipString, port));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String player = playerExpression.toString(event, debug);
        String string = stringExpression.toString(event, debug);
        String number = numberExpression.toString(event, debug);
        return "set ip of " + player + " to " + string + " with port " + number;
    }

}
