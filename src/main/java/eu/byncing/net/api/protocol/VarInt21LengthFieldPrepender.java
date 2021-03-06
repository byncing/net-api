package eu.byncing.net.api.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class VarInt21LengthFieldPrepender extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        int bodyLen = msg.readableBytes();
        int headerLen = varIntSize(bodyLen);
        out.ensureWritable(headerLen + bodyLen);

        ProtocolUtil.writeVarInt(bodyLen, out);
        out.writeBytes(msg);
    }

    private static int varIntSize(int paramInt) {
        if ((paramInt & 0xFFFFFF80) == 0) return 1;
        if ((paramInt & 0xFFFFC000) == 0) return 2;
        if ((paramInt & 0xFFE00000) == 0) return 3;
        if ((paramInt & 0xF0000000) == 0) return 4;
        return 5;
    }
}