package eu.byncing.net.example.server;

import eu.byncing.net.api.INetListener;
import eu.byncing.net.api.NetServer;
import eu.byncing.net.api.channel.INetChannel;

public class Server {

    public static void main(String[] args) {
        NetServer server = new NetServer();
        server.getPacketRegistry().addHandlers(new ExampleHandler());
        server.addListener(new INetListener() {
            @Override
            public void handleConnected(INetChannel channel) {
                System.out.println("[Server] Channel" + channel.getSocket().remoteAddress() + " has connected.");
            }

            @Override
            public void handleDisconnected(INetChannel channel) {
                System.out.println("[Server] Channel" + channel.getSocket().remoteAddress() + " has disconnected.");
            }
        }).bind(3000);
    }
}