package eu.byncing.net.example.client;

import eu.byncing.net.api.NetClient;
import eu.byncing.net.api.channel.ChannelPipeline;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Client {

    public static void main(String[] args) {
        try {
            NetClient client = new NetClient();
            client.init(channel -> {
                System.out.println("[Client] Channel" + channel.getRemoteAddress() + " has initialized.");

                ChannelPipeline pipeline = channel.getPipeline();
                pipeline.handle(new ClientHandler());
            }).connect(new InetSocketAddress("127.0.0.1", 25565));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}