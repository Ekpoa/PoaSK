package poa.poask.util.reflection;

import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.FriendlyByteBuf;

public class HeadRotPacket {




    @SneakyThrows
    public static Object headRotPacket(int id, int yRot) {
        Object buffer = FriendlyByteBuf.friendlyByteBufConstructor.newInstance(Unpooled.buffer());

        FriendlyByteBuf.writeVarInt.invoke(buffer, id);
        FriendlyByteBuf.writeByte.invoke(buffer, yRot * 255 / 360);

        return CommonClassMethodFields.headRotPacketConstructor.newInstance(buffer);
    }


}
