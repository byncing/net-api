package eu.byncing.net.example.server;

import eu.byncing.net.api.NetOption;
import eu.byncing.net.api.NetServer;
import eu.byncing.net.api.channel.ChannelPipeline;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) {
        try {
            NetServer server = new NetServer();
            server.option(NetOption.TIMEOUT, 15000).init(channel -> {
                System.out.println("[Server] Channel" + channel.getRemoteAddress() + " has initialized.");

                ChannelPipeline pipeline = channel.getPipeline();
                pipeline.handle(new ServerHandler());
            }).bind(new InetSocketAddress(25565));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}