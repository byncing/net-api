package eu.byncing.net.api;

import eu.byncing.net.api.channel.ChannelPipeline;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.channel.IChannelInitializer;
import eu.byncing.net.api.channel.NetChannel;
import eu.byncing.net.api.protocol.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class NetWorker implements Runnable, INetStructure {

    private final Thread thread = new Thread(this);

    private ServerSocket socket;

    private boolean connected;

    private final List<IChannel> channels = new ArrayList<>();

    private final ChannelPipeline pipeline = new ChannelPipeline();

    private IChannelInitializer initializer;

    public NetWorker() {
        try {
            this.socket = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bind(InetSocketAddress address) throws IOException {
        socket.bind(address);
        if (getOption(NetOption.TIMEOUT) > 0) socket.setSoTimeout(getOption(NetOption.TIMEOUT));
        connected = true;
        thread.start();
    }

    @Override
    public void run() {
        while (connected) {
            try {
                NetChannel channel = new NetChannel(this, socket.accept());
                channels.add(channel);
                if (initializer != null) initializer.initChannel(channel);
                channel.getPipeline().getHandler().handleConnected(channel);
                channel.start();
            } catch (IOException e) {
                pipeline.getHandler().handleException(e);
                close();
            }
        }
    }

    @Override
    public void close() {
        try {
            connected = false;
            for (int i = channels.size() - 1; i >= 0; i--) channels.get(i).close();
            socket.close();
        } catch (IOException e) {
            pipeline.getHandler().handleException(e);
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        for (int i = channels.size() - 1; i >= 0; i--) channels.get(i).sendPacket(packet);
    }
    
    @Override
    public NetWorker init(IChannelInitializer initializer) {
        this.initializer = initializer;
        return this;
    }

    @Override
    public NetWorker option(NetOption<?> option, Object value) {
        option.setValue(value);
        return this;
    }

    @Override
    public <T> T getOption(NetOption<T> option) {
        return option.value;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public SocketAddress getLocalSocketAddress() {
        return socket.getLocalSocketAddress();
    }

    public int getPort() {
        return socket.getLocalPort();
    }

    public List<IChannel> getChannels() {
        return channels;
    }
}