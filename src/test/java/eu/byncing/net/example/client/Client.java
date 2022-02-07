package eu.byncing.net.example.client;

import eu.byncing.net.api.INetListener;
import eu.byncing.net.api.NetClient;
import eu.byncing.net.api.channel.INetChannel;
import eu.byncing.net.example.PacketExample;

public class Client {

    public static void main(String[] args) {
        NetClient client = new NetClient();
        client.addListener(new INetListener() {
            @Override
            public void handleConnected(INetChannel channel) {
                System.out.println("[Client] Channel" + channel.getSocket().remoteAddress() + " has connected.");

                channel.sendPacket(new PacketExample("byncing", "Germany", 19));
            }

            @Override
            public void handleDisconnected(INetChannel channel) {
                System.out.println("[Client] Channel" + channel.getSocket().remoteAddress() + " has disconnected.");
            }
        }).connect("127.0.0.1", 3000);
    }
}