package eu.byncing.net.api.channel;

import eu.byncing.net.api.protocol.packet.IPacketSender;
import eu.byncing.net.api.protocol.packet.EmptyPacket;
import io.netty.channel.Channel;

import java.io.Closeable;

public interface INetChannel extends Closeable, IPacketSender {

    @Override
    void close();

    @Override
    void sendPacket(EmptyPacket packet);

    boolean isConnected();

    Channel getSocket();
}