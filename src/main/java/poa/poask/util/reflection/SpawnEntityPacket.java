package poa.poask.util.reflection;

import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import poa.poask.util.StringConversion;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Reflection;

import java.util.UUID;

public class SpawnEntityPacket {

    @SneakyThrows
    public static Object spawnEntityPacket(Location location, EntityType entityType, int id){
        String l = StringConversion.toPascalCase(entityType.toString().toLowerCase());
        l = l.replace("_", "");


        String letter = (String) Reflection.getField(l, CommonClassMethodFields.letterClass, null);
        return CommonClassMethodFields.spawnPacketConstructor.newInstance(id, UUID.randomUUID(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), Reflection.getField(letter, CommonClassMethodFields.entityTypeClass, null), 0, CommonClassMethodFields.vec3Class.getDeclaredConstructor(double.class, double.class, double.class).newInstance(0, 0, 0), (double) location.getYaw());
    }


}
