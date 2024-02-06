package poa.poask.util.reflection;

import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.FriendlyByteBuf;

public class SetPassengerPacket {


    @SneakyThrows
    public static Object setPassengerPacket(int vehicle, int[] passengers){
        Object friendlyByteBuf = FriendlyByteBuf.friendlyByteBufConstructor.newInstance(Unpooled.buffer());
        FriendlyByteBuf.writeVarInt.invoke(friendlyByteBuf, vehicle);
        FriendlyByteBuf.writeVarIntArray.invoke(friendlyByteBuf, passengers);
        return CommonClassMethodFields.setPassengerConstructor.newInstance(friendlyByteBuf);
    }


}
