package eu.byncing.net.api;

import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.channel.IChannelInitializer;
import eu.byncing.net.api.channel.NetChannel;
import eu.byncing.net.api.protocol.Packet;

import java.io.IOException;
import java.net.SocketAddress;

public class NetClient implements INetStructure {

    private final NetChannel channel = new NetChannel(this);

    public void connect(SocketAddress address) throws IOException {
        channel.connect(address);
        channel.start();
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void sendPacket(Packet packet) {
        channel.sendPacket(packet);
    }

    @Override
    public NetClient init(IChannelInitializer initializer) {
        channel.init(initializer);
        return this;
    }

    @Override
    public NetClient option(NetOption<?> option, Object value) {
        option.setValue(value);
        return this;
    }

    @Override
    public <T> T getOption(NetOption<T> option) {
        return option.value;
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    public IChannel getChannel() {
        return channel;
    }
}