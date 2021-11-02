package eu.byncing.net.api.protocol.codec;

import eu.byncing.net.api.NetUtil;
import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.io.DataOutputStream;
import java.io.IOException;

public interface IPacketEncoder {

    void encode(DataOutputStream output, Packet packet, IPacketBuffer buffer) throws IOException;

    class NetPacketEncoder implements IPacketEncoder {
        @Override
        public void encode(DataOutputStream output, Packet packet, IPacketBuffer buffer) throws IOException {
            buffer.write("clazz", packet.getClass().getName());
            packet.write(buffer);
            output.write(NetUtil.nullChar(buffer.array()));
            output.flush();
        }
    }
}