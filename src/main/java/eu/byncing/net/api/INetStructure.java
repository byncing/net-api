package eu.byncing.net.api;

import eu.byncing.net.api.channel.IChannelInitializer;
import eu.byncing.net.api.protocol.IPacketSender;
import eu.byncing.net.api.protocol.Packet;

import java.io.Closeable;
import java.io.IOException;

public interface INetStructure extends Closeable, IPacketSender {

    @Override
    void close() throws IOException;

    @Override
    void sendPacket(Packet packet);

    INetStructure init(IChannelInitializer initializer);

    INetStructure option(NetOption<?> option, Object value);

    <T> T getOption(NetOption<T> option);

    boolean isConnected();
}