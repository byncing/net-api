package eu.byncing.net.api.protocol.packet.codec;

import eu.byncing.net.api.protocol.packet.buffer.IPacketBuffer;
import eu.byncing.net.api.protocol.packet.EmptyPacket;
import eu.byncing.net.api.protocol.packet.buffer.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<EmptyPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, EmptyPacket msg, ByteBuf out) {
        IPacketBuffer buffer = new PacketBuffer(out);
        buffer.writeString(msg.getClass().getName());
        msg.write(buffer);
    }
}