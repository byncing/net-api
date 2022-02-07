package eu.byncing.net.api;

import eu.byncing.net.api.channel.INetChannel;

public interface INetListener {

    void handleConnected(INetChannel channel);

    void handleDisconnected(INetChannel channel);
}