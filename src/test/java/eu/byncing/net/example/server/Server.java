package eu.byncing.net.example.server;

import eu.byncing.net.api.INetListener;
import eu.byncing.net.api.NetServer;
import eu.byncing.net.api.channel.INetChannel;
import eu.byncing.net.api.protocol.packet.EmptyPacket;
import eu.byncing.net.example.PacketExample;

public class Server {

    public static void main(String[] args) {
        NetServer server = new NetServer();
        server.addListener(new INetListener() {
            @Override
            public void handleConnected(INetChannel channel) {
                System.out.println("[Server] Channel" + channel.getSocket().remoteAddress() + " has connected.");
            }

            @Override
            public void handleDisconnected(INetChannel channel) {
                System.out.println("[Server] Channel" + channel.getSocket().remoteAddress() + " has disconnected.");
            }

            @Override
            public void handlePacket(INetChannel channel, EmptyPacket packet) {
                if (packet instanceof PacketExample) {
                    PacketExample example = (PacketExample) packet;

                    System.out.println("[Server] Channel" + channel.getSocket().remoteAddress() + " data(name: " +
                            example.getName() + ", country: " +
                            example.getCountry() + ", age: " +
                            example.getAge() + ")");
                }
            }
        }).bind(3000);
    }
}