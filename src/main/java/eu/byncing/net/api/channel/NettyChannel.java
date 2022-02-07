package eu.byncing.net.api.channel;

import eu.byncing.net.api.protocol.packet.EmptyPacket;
import io.netty.channel.Channel;

public class NettyChannel implements INetChannel {

    private final Channel channel;

    public NettyChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void close() {
        if (!isConnected()) return;
        channel.close();
    }

    @Override
    public void sendPacket(EmptyPacket packet) {
        if (isConnected()) channel.writeAndFlush(packet);
    }

    @Override
    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

    @Override
    public Channel getSocket() {
        return channel;
    }
}