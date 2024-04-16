package poa.poask.util.reflection;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.SneakyThrows;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Letters;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlowPacket {




    @SneakyThrows
    @SuppressWarnings("ConstantConditions")
    public static Object glowPacket(Entity entity, boolean glow) {
        Object nmsEntity = Reflection.getNMSHandle(entity);

        Object clonedWatcher = CommonClassMethodFields.getEntityDataMethod.invoke(nmsEntity);

        Object newWatcher = CommonClassMethodFields.dataWatcherConstructor.newInstance(nmsEntity);

        Int2ObjectMap<?> newMap = (Int2ObjectMap<?>) FieldUtils.readDeclaredField(clonedWatcher, Letters.dataWatcherInt2ObjectMap, true);

        Object item = newMap.get(0);

        byte initialMask = (byte) CommonClassMethodFields.initialMethod.invoke(item);
        byte bitIndex = (byte) 6;

        if (glow)
            CommonClassMethodFields.itemWatcherClass.getDeclaredMethod(Letters.dataWatcher$ItemAccessor, Object.class).invoke(item, (byte) (initialMask | 1 << bitIndex));
        else
            CommonClassMethodFields.itemWatcherClass.getDeclaredMethod(Letters.dataWatcher$ItemAccessor, Object.class).invoke(item, (byte) (initialMask & ~ (1 << bitIndex)));

        FieldUtils.writeDeclaredField(newWatcher, Letters.dataWatcherInt2ObjectMap, newMap, true);

        if (List.of("1193", "1194", "1200", "1201", "1202", "1203", "1204").contains(Bukkit.getMinecraftVersion().replaceAll("[.]", ""))) {
            Method unpackedMethod = CommonClassMethodFields.dataWatcherClass.getDeclaredMethod("c"); //for 1.19.3/4 didn't bother to write in the Letter class since 1 letter :D
            List<?> unpacked = (List<?>) unpackedMethod.invoke(newWatcher);
            return CommonClassMethodFields.metadataPacketClass.getDeclaredConstructor(int.class, List.class).newInstance(entity.getEntityId(), unpacked);
        } else {
            return CommonClassMethodFields.metadataPacketClass.getDeclaredConstructor(int.class, CommonClassMethodFields.dataWatcherClass, boolean.class).newInstance(entity.getEntityId(), newWatcher, true);
        }

    }
}
