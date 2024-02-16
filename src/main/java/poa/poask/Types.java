package poa.poask;


import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.kyori.adventure.text.Component;

public class Types {

    static {
        if (Classes.getExactClassInfo(Component.class) == null) {
            Classes.registerClass(new ClassInfo(Component.class, "adventurecomponent")
                    .user("component")
                    .name("AdventureComponent")
                    .description("Adventure Component")
                    .since("3.2")
                    .parser(new Parser<>() {
                        public boolean canParse(ParseContext context) {
                            return false;
                        }

                        @Override
                        public String toString(Object o, int flags) {
                            return "Component";
                        }

                        @Override
                        public String toVariableNameString(Object o) {
                            return o.toString();
                        }
                    }));
        }
    }

    public static void call(){}

}
