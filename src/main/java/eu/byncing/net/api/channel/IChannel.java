package eu.byncing.net.api.channel;

import eu.byncing.net.api.protocol.IPacketSender;
import eu.byncing.net.api.protocol.Packet;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;

public interface IChannel extends Closeable, IPacketSender {

    @Override
    void close() throws IOException;

    @Override
    void sendPacket(Packet packet);

    boolean isConnected();

    ChannelPipeline getPipeline();

    SocketAddress getRemoteAddress();

    InetAddress getLocalAddress();
}