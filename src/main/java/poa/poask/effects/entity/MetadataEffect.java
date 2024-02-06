package poa.poask.effects.entity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.VariableString;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.PoaSK;
import poa.poask.util.reflection.EntityMetadata;
import poa.poask.util.reflection.common.SendPacket;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.logging.Level;


public class MetadataEffect extends Effect implements Listener {

    static {
        Skript.registerEffect(MetadataEffect.class, "send metadata packet with (entity id %-number%|1:uuid %-string%) [on fire %-boolean%][,] [invisible %-boolean%][,] [glowing %-boolean%][,] [name %-string%][,] [name visible %-boolean%][,] [silent %-boolean%][,] [has gravity %-boolean%][,] [pose %-string%] to %players%");
    }

    //only for 1.19.3+

    @SneakyThrows
    @Override

    protected void execute(Event e) {
//        Expression<Number> idE = numberExpression;
//        Expression<String> uuidE = uuidExpression;
        Object single = firstExpression.getSingle(e);
        String stringUUID = null;
        int id = 0;
        if(single instanceof String s)
             stringUUID = s;
        else if (single instanceof Number n)
            id = n.intValue();

        Expression<Boolean> b1E = booleanExpression1;
        Expression<Boolean> b2E = booleanExpression2;
        Expression<Boolean> b3E = booleanExpression3;
        Expression<String> sE = stringExpression;
        Expression<Boolean> b4E = booleanExpression4;
        Expression<Boolean> b5E= booleanExpression5;
        Expression<String> pE = poseExpression;
        Expression<Boolean> b6E= booleanExpression6;

        Entity entity = null;
        if(stringUUID != null){
            UUID uuid = UUID.fromString(stringUUID);
            entity = Bukkit.getEntity(uuid);
            if(entity == null){
                PoaSK.INSTANCE.getLogger().log(Level.SEVERE, "No entity exists with that UUID. If it is a fake entity use the entity id");
                return;
            }
            id = entity.getEntityId();
        }

        boolean fire = false;
        boolean invisible = false;
        boolean glowing = false;
        String name = null;
        boolean nameVisible = false;
        boolean silent = false;
        boolean gravity = true;
        String pose = "STANDING";

        if(b1E == null){
            if(entity != null)
                fire = entity.isVisualFire();
        }
        else
            fire = b1E.getSingle(e);

        if(b2E == null){
            if(entity instanceof LivingEntity li)
                invisible = li.isInvisible();
        }
        else
            invisible = b2E.getSingle(e);

        if(b3E == null){
            if(entity != null)
                glowing = entity.isGlowing();
        }
        else
            glowing = b3E.getSingle(e);

        if(sE == null){
            if(entity != null && entity.customName() != null)
                name = MiniMessage.miniMessage().serialize(entity.customName());
        }
        else {
            name = sE.getSingle(e);
            if(sE instanceof VariableString vs)
                name = vs.toUnformattedString(e);
        }

        if(b4E == null){
            if(entity != null)
                nameVisible = entity.isCustomNameVisible();
        }
        else
            nameVisible = b4E.getSingle(e);

        if(b5E == null){
            if(entity != null)
                silent = entity.isSilent();
        }
        else
            silent = b5E.getSingle(e);

        if(b6E == null){
            if(entity != null)
                gravity = entity.hasGravity();
        }
        else
            gravity = b6E.getSingle(e);

        if(pE == null){
            if(entity != null)
                pose = entity.getPose().name();
        }
        else
            pose = pE.getSingle(e);


        for (Player p : playerExpression.getArray(e)) {
            SendPacket.sendPacket(p, EntityMetadata.basePacketForEntity(id, fire, invisible, glowing, name, nameVisible, silent, gravity, pose));
        }
    }


    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "metadata";
    }

//    private Expression<Number> numberExpression;
//    private Expression<String> uuidExpression;
    private Expression<?> firstExpression;
    private Expression<Boolean> booleanExpression1;
    private Expression<Boolean> booleanExpression2;
    private Expression<Boolean> booleanExpression3;
    private Expression<String> stringExpression;
    private Expression<Boolean> booleanExpression4;
    private Expression<Boolean> booleanExpression5;
    private Expression<Boolean> booleanExpression6;
    private Expression<String> poseExpression;
    private Expression<Player> playerExpression;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
//        numberExpression = parseResult.hasTag("1") ? (Expression<Number>) exprs[0] : null;
//        uuidExpression = parseResult.hasTag("1") ?  null : (Expression<String>) exprs[0];
        firstExpression = parseResult.hasTag("1") ? (Expression<String>) exprs[1] : (Expression<Number>) exprs[0];
        booleanExpression1 = (Expression<Boolean>) exprs[2];
        booleanExpression2 = (Expression<Boolean>) exprs[3];
        booleanExpression3 = (Expression<Boolean>) exprs[4];
        stringExpression = (Expression<String>) exprs[5];
        booleanExpression4 = (Expression<Boolean>) exprs[6];
        booleanExpression5 = (Expression<Boolean>) exprs[7];
        booleanExpression6 = (Expression<Boolean>) exprs[8];
        poseExpression = (Expression<String>) exprs[9];
        playerExpression = (Expression<Player>) exprs[10];
        return true;
    }
}
