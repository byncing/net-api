package eu.byncing.net.example.client;

import eu.byncing.net.api.channel.ChannelHandler;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;
import eu.byncing.net.example.PacketExample;

public class ClientHandler extends ChannelHandler {

    @Override
    public void handleConnected(IChannel channel) {
        System.out.println("[Client] Channel" + channel.getRemoteAddress() + " has connected.");
    }

    @Override
    public void handlePacket(IChannel channel, Packet packet) {
        if (packet instanceof PacketExample example) {
            System.out.println("[Client] Channel" + channel.getRemoteAddress() + " Channel data: name " + example.getName() + ", country " + example.getCountry() + ", age " + example.getAge());
        }
    }

    @Override
    public void handleDisconnected(IChannel channel) {
        System.out.println("[Client] Channel" + channel.getRemoteAddress() + " has disconnected.");
    }
}