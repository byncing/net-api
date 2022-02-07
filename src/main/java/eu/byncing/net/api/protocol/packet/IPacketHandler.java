package eu.byncing.net.api.protocol.packet;

import eu.byncing.net.api.channel.INetChannel;

public interface IPacketHandler<P extends EmptyPacket> {

    void handle(INetChannel channel, P packet);

    Class<?>[] getClasses();
}