package poa.poask.util.reflection;

import lombok.SneakyThrows;
import poa.poask.util.reflection.common.CommonClassMethodFields;

public class RemoveEntityPacket {
    @SneakyThrows
    public static Object removeEntityPacket(int[] id){
        return CommonClassMethodFields.removeEntityPacketConstructor.newInstance(id);
    }


}
