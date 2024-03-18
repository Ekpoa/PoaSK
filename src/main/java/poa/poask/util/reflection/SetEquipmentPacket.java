package poa.poask.util.reflection;

import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Reflection;

import java.lang.reflect.Constructor;
import java.util.List;

public class SetEquipmentPacket {


    public static final Class<?> packetClass = Reflection.getNMSClass("PacketPlayOutEntityEquipment", "net.minecraft.network.protocol.game");
    public static final Class<?> pairClass = Reflection.getNMSClass("Pair","com.mojang.datafixers.util");
    public static final Class<?> equipmentClass = Reflection.getNMSClass("EnumItemSlot", "net.minecraft.world.entity");
    public static final Constructor pairConstructor;
    public static Constructor packetConstructor;

    public static Object[] slots;

    static {
        try {
            packetConstructor = packetClass.getDeclaredConstructor(int.class, List.class);
            pairConstructor = pairClass.getDeclaredConstructor(Object.class, Object.class);
            slots = equipmentClass.getEnumConstants();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static Object packet(int id, String slot, ItemStack item){
        Object equipmentSlot = null;
        slot = slot.replace("off_hand", "offhand")
                .replace("off hand", "offhand");


        for (Object o : slots) {
            if(o.toString().equalsIgnoreCase(slot)) {
                equipmentSlot = o;
                break;
            }
        }

        if(equipmentSlot == null)
            return null;


        Object nmsItemStack = CommonClassMethodFields.itemAsBukkitCopy(item);

        Object pair = pairConstructor.newInstance(equipmentSlot, nmsItemStack);

        return packetConstructor.newInstance(id, List.of(pair));
    }



}
