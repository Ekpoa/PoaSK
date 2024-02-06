package poa.poask.util;

import io.netty.util.internal.StringUtil;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Field;

public class StringConversion {

    public static String essentialsToMinimessage(String string) {
        String x = string.replaceAll("&#([a-fA-F0-9]{6})", "<#$1><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("§", "&");
        x = x.replace("&l", "<bold>");
        x = x.replace("&n", "<underlined>");
        x = x.replace("&m", "<strikethrough>");
        x = x.replace("&o", "<italic>");
        x = x.replace("&k", "<obfuscated>");
        x = x.replace("&r", "<reset>");
        x = x.replace("&0", "<black><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&1", "<dark_blue><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&2", "<dark_green><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&3", "<dark_aqua><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&4", "<dark_red><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&5", "<dark_purple><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&6", "<gold><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&7", "<gray><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&8", "<dark_gray><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&9", "<blue><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&a", "<green><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&b", "<aqua><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&c", "<red><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&d", "<light_purple><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&e", "<yellow><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");
        x = x.replace("&f", "<white><bold:false><underlined:false><strikethrough:false><italic:false><obfuscated:false>");

        return x;
    }

    public static String bukkitToEssentials(String string){
        return string.replaceAll("§x§(.)§(.)§(.)§(.)§(.)§(.)", "&#$1$2$3$4$5$6");
    }

    public static String toPascalCase(String name) {
        if (StringUtil.isNullOrEmpty(name)) {
            return name;
        }

        StringBuilder pascalCase = new StringBuilder();
        char newChar;
        boolean toUpper = false;
        char[] charArray = name.toCharArray();
        for (int ctr = 0; ctr <= charArray.length - 1; ctr++) {
            if (ctr == 0) {
                newChar = Character.toUpperCase(charArray[ctr]);
                pascalCase = new StringBuilder(Character.toString(newChar));
                continue;
            }

            if (charArray[ctr] == '_') {
                toUpper = true;
                continue;
            }

            if (toUpper) {
                newChar = Character.toUpperCase(charArray[ctr]);
                pascalCase.append(newChar);
                toUpper = false;
                continue;
            }

            pascalCase.append(charArray[ctr]);
        }

        return pascalCase.toString();
    }


    private static final Class<?> paperAdventureClass = Reflection.getPluginClass("PaperAdventure", "io.papermc.paper.adventure");
    private static final Class<?> serializerClass = Reflection.getPluginClass("ComponentSerializer", "net.kyori.adventure.text.serializer");
    private static final Field wrapperAwareSerializerField;
    private static Object wrapperAwareSerializerObject;

    static {
        try {
            wrapperAwareSerializerField = paperAdventureClass.getDeclaredField("WRAPPER_AWARE_SERIALIZER");
            wrapperAwareSerializerObject = wrapperAwareSerializerField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static Object nmsComponent(Component adventure) {
        Object serialize = serializerClass.getDeclaredMethod("serialize", Component.class).invoke(wrapperAwareSerializerObject, adventure);

        return serialize;
    }

}
