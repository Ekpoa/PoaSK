package poa.poask.util.reflection;

import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.FriendlyByteBuf;

public class CameraPacket {





    @SneakyThrows
    public static Object packet(int id){
        Object friendlyByteBuf = FriendlyByteBuf.friendlyByteBufConstructor.newInstance(Unpooled.buffer());
        FriendlyByteBuf.writeVarInt.invoke(friendlyByteBuf, id);
        return CommonClassMethodFields.cameraPacketConstructor.newInstance(friendlyByteBuf);
    }


}
