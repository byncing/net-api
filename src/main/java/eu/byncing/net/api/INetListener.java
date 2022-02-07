package eu.byncing.net.api;

import eu.byncing.net.api.channel.INetChannel;
import eu.byncing.net.api.protocol.packet.EmptyPacket;

public interface INetListener {

    void handleConnected(INetChannel channel);

    void handleDisconnected(INetChannel channel);

    void handlePacket(INetChannel channel, EmptyPacket packet);
}