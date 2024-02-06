package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import poa.poask.util.reflection.common.Letters;
import poa.poask.util.reflection.common.Reflection;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.List;

public class ChangeIP extends Effect {

    public static final Class<?> serverPlayerClass;
    public static final Class<?> connectClass;
    public static final Class<?> connectionClass;
    public static Field connectionField;
    public static Field connection2;
    public static Field addressField;

    static {
        Skript.registerEffect(ChangeIP.class, "set ip of %player% to %string% with port %number%");
        serverPlayerClass = Reflection.getNMSClass("EntityPlayer", "net.minecraft.server.level");



        if(List.of("1202", "1203", "1204").contains(Letters.getBukkitVersion()))
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

    @Override
    protected void execute(Event e) {

        Player player = playerExpression.getSingle(e);
        String s = stringExpression.getSingle(e);
        int i = numberExpression.getSingle(e).intValue();
            try {

                Object p = Reflection.getHandle(player);


                Object connectionObject = connectionField.get(p);

                Object connectionField2 = connection2.get(connectionObject);
//            Object methodInvoke = connectionField2.invoke(connectionObject);

                addressField.set(connectionField2, new InetSocketAddress(s, i));
            } catch (NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "change ip";
    }

    private Expression<Player> playerExpression;
    private Expression<String> stringExpression;
    private Expression<Number> numberExpression;
    //private Expression<Player> playerListExpression;
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        stringExpression = (Expression<String>) exprs[1];
        numberExpression = (Expression<Number>) exprs[2];
        //playerListExpression = (Expression<Player>) exprs[2];
        return true;
    }
}
