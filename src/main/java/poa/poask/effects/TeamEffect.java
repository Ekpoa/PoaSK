package poa.poask.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import poa.poask.util.reflection.common.SendPacket;
import poa.poask.util.reflection.TeamPacket;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;


public class TeamEffect extends Effect implements Listener {

    static  {
        Skript.registerEffect(TeamEffect.class, "send team packet to %players% with name %string% [displayname %-string%] [nametag visibility %-string%] [collision %-string%] [color %-color%] [prefix %-string%] [and] [suffix %-string%] with entity uuid %strings%");
    }

    @SneakyThrows
    @Override

    protected void execute(Event e) {
        Expression<Color> color = colorExpression;

        String s1 = stringExpression1.getSingle(e);
        Expression<String> s2 = stringExpression2;
        Expression<String> s3 = stringExpression3;
        Expression<String> s4 = stringExpression4;
        Expression<String> s5 = stringExpression5;
        Expression<String> s6 = stringExpression6;
        List<String> list = Arrays.stream(stringExpression7.getArray(e)).toList();

        //String chatColor = ((SkriptColor) color).asChatColor().name();

        String displayname = "";
        String visibility = "always";
        String collision = "always";
        String chatColor = "";
        String prefix = "";
        String suffix = "";

        if(s2 != null)
            displayname = s2.getSingle(e);
        if(s3 != null)
            visibility = s3.getSingle(e);
        if(s4 != null)
            collision = s4.getSingle(e);
        if(s5 != null)
            prefix = s5.getSingle(e);
        if(s6 != null)
            suffix = s6.getSingle(e);

        if(color != null)
            chatColor = ((SkriptColor) color.getSingle(e)).asChatColor().name();

        for(Player p : playerExpression.getArray(e)){
            SendPacket.sendPacket(p, TeamPacket.teamPacket(s1, displayname, visibility, collision, chatColor, prefix, suffix, list));
        }
    }




    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Team packets";
    }

    private Expression<Player> playerExpression;
    private Expression<String> stringExpression1;
    private Expression<String> stringExpression2;
    private Expression<String> stringExpression3;
    private Expression<String> stringExpression4;
    private Expression<Color> colorExpression;
    private Expression<String> stringExpression5;
    private Expression<String> stringExpression6;
    private Expression<String> stringExpression7;


    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        stringExpression1 = (Expression<String>) exprs[1];
        stringExpression2 = (Expression<String>) exprs[2];
        stringExpression3 = (Expression<String>) exprs[3];
        stringExpression4 = (Expression<String>) exprs[4];
        colorExpression = (Expression<Color>) exprs[5];
        stringExpression5 = (Expression<String>) exprs[6];
        stringExpression6 = (Expression<String>) exprs[7];
        stringExpression7 = (Expression<String>) exprs[8];
        return true;
    }
}
