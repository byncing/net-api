package eu.byncing.net.api;

import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.channel.IChannelInitializer;
import eu.byncing.net.api.protocol.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public class NetServer implements INetStructure {

    private final NetWorker worker;

    public NetServer() {
        this.worker = new NetWorker();
    }

    public void bind(InetSocketAddress address) throws IOException {
        worker.bind(address);
    }

    @Override
    public void close() {
        worker.close();
    }

    @Override
    public void sendPacket(Packet packet) {
        worker.sendPacket(packet);
    }

    @Override
    public NetServer init(IChannelInitializer initializer) {
        worker.init(initializer);
        return this;
    }

    @Override
    public NetServer option(NetOption<?> option, Object value) {
        worker.option(option, value);
        return this;
    }

    @Override
    public <T> T getOption(NetOption<T> option) {
        return worker.getOption(option);
    }

    @Override
    public boolean isConnected() {
        return worker.isConnected();
    }

    public InetAddress getInetAddress() {
        return worker.getInetAddress();
    }

    public SocketAddress getLocalSocketAddress() {
        return worker.getLocalSocketAddress();
    }

    public int getPort() {
        return worker.getPort();
    }

    public List<IChannel> getChannels() {
        return worker.getChannels();
    }
}