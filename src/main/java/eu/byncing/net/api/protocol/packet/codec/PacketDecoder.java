package eu.byncing.net.api.protocol.packet.codec;

import eu.byncing.net.api.protocol.packet.buffer.IPacketBuffer;
import eu.byncing.net.api.protocol.packet.EmptyPacket;
import eu.byncing.net.api.protocol.packet.buffer.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        if (!ctx.channel().isActive()) return;
        ByteBuf slice = msg.copy();
        try {
            IPacketBuffer buffer = new PacketBuffer(slice);
            EmptyPacket packet = EmptyPacket.newInstance(buffer);
            if (packet != null) {
                packet.read(buffer);
                out.add(packet);
            } else msg.skipBytes(msg.readableBytes());
            slice = null;
        } finally {
            if (slice != null) slice.release();
        }
    }
}