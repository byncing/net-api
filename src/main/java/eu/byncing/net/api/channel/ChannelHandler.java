package eu.byncing.net.api.channel;

import eu.byncing.net.api.protocol.Packet;

import java.io.IOException;

public class ChannelHandler {

    public void handleConnected(IChannel channel) throws IOException {}

    public void handlePacket(IChannel channel, Packet packet) throws IOException {}

    public void handleDisconnected(IChannel channel) throws IOException {}

    public void handleException(Exception exception) {
        exception.printStackTrace();
    }
}