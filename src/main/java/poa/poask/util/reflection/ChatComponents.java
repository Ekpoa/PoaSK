package poa.poask.util.reflection;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import poa.poask.util.reflection.common.CommonClassMethodFields;

public class ChatComponents {





    @SneakyThrows
    public static Object nmsComponent(Component component){
        return CommonClassMethodFields.serializeMethod.invoke(CommonClassMethodFields.serializeObject, component);
    }

}
