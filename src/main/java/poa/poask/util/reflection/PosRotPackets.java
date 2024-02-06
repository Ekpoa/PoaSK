package poa.poask.util.reflection;

import lombok.SneakyThrows;
import poa.poask.util.reflection.common.CommonClassMethodFields;

public class PosRotPackets {




    @SneakyThrows
    public static Object posPacket(int id, short deltaX, short deltaY, short deltaZ){
        return CommonClassMethodFields.posPacketConstructor.newInstance(id, deltaX, deltaY, deltaZ, true);
    }

    @SneakyThrows
    public static Object rotPacket(int id, int yaw, int pitch){
        return CommonClassMethodFields.rotPacketConstructor.newInstance(id, (byte) (yaw * 255F / 360F), (byte) (pitch * 255F / 360F), true);
    }




}
