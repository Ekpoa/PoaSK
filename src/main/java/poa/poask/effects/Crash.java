//package poa.poask.effects;
//
//import ch.njol.skript.Skript;
//import ch.njol.skript.lang.Effect;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.SkriptParser;
//import ch.njol.util.Kleenean;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.Particle;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
//import org.bukkit.util.Vector;
//import poa.poask.PoaSK;
//import poa.poask.util.reflection.common.SendPacket;
//import poa.poask.util.reflection.SpawnEntityPacket;
//
//import javax.annotation.Nullable;
//import java.util.concurrent.ThreadLocalRandom;
//
//public class Crash extends Effect {
//
//    static {
//        Skript.registerEffect(Crash.class, "(close launcher of|crash) %player%");
//    }
//
//    @Override
//    protected void execute(Event e) {
//        Player player = playerExpression.getSingle(e);
//        if (player == null)
//            return;
//
//        Bukkit.getScheduler().runTaskAsynchronously(PoaSK.INSTANCE, () -> {
//            Location location = getBlockBehindPlayer(player);
//            SendPacket.sendPacket(player, SpawnEntityPacket.spawnEntityPacket(location, EntityType.ARMOR_STAND, player.getEntityId()));
//            ThreadLocalRandom random = ThreadLocalRandom.current();
//            for (int i = 0; i < 100; i++) {
//                player.spawnParticle(Particle.FLAME, player.getLocation(), Integer.MAX_VALUE);
//                player.spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), Integer.MAX_VALUE);
//
//            }
//            for (int i = 0; i < PoaSK.INSTANCE.getConfig().getInt("Crash.EntityCount"); i++) {
//                SendPacket.sendPacket(player, SpawnEntityPacket.spawnEntityPacket(location, EntityType.BAT, random.nextInt(0, 9999999)));
//            }
//        });
//    }
//
//    private Location getBlockBehindPlayer(Player player) {
//        Vector inverseDirectionVec = player.getLocation().getDirection().normalize().multiply(-5);
//        return player.getLocation().add(inverseDirectionVec);
//    }
//
//    @Override
//    public String toString(@Nullable Event e, boolean debug) {
//        return "crash player";
//    }
//
//    private Expression<Player> playerExpression;
//    @Override
//    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
//        playerExpression = (Expression<Player>) exprs[0];
//
//        return true;
//    }
//}
