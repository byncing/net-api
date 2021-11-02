package eu.byncing.net.example.server;

import eu.byncing.net.api.channel.ChannelHandler;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;
import eu.byncing.net.example.PacketExample;

import java.io.IOException;

public class ServerHandler extends ChannelHandler {

    @Override
    public void handleConnected(IChannel channel) {
        System.out.println("[Server] Channel" + channel.getRemoteAddress() + " has connected.");
        channel.sendPacket(new PacketExample("byncing", "Germany", 19));
    }

    @Override
    public void handlePacket(IChannel channel, Packet packet) throws IOException {
        super.handlePacket(channel, packet);
    }

    @Override
    public void handleDisconnected(IChannel channel) {
        System.out.println("[Server] Channel" + channel.getRemoteAddress() + " has disconnected.");
    }
}