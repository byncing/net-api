package eu.byncing.net.api;

import eu.byncing.net.api.channel.INetChannel;
import eu.byncing.net.api.channel.NettyChannel;
import eu.byncing.net.api.protocol.VarInt21FrameDecoder;
import eu.byncing.net.api.protocol.VarInt21LengthFieldPrepender;
import eu.byncing.net.api.protocol.packet.EmptyPacket;
import eu.byncing.net.api.protocol.packet.codec.PacketDecoder;
import eu.byncing.net.api.protocol.packet.codec.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NetClient implements INetStructure {

    private final List<INetListener> listeners = new ArrayList<>();

    private INetChannel channel;

    public void connect(String host, int port) {
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        channel = new NettyChannel(ch);
                        ch.pipeline().addLast(
                                new VarInt21FrameDecoder(),
                                new PacketDecoder(),
                                new VarInt21LengthFieldPrepender(),
                                new PacketEncoder(),
                                new SimpleChannelInboundHandler<EmptyPacket>() {
                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) {
                                        ctx.fireChannelActive();
                                        getListeners().forEach(listener -> listener.handleConnected(channel));
                                    }

                                    @Override
                                    public void channelInactive(ChannelHandlerContext ctx) {
                                        ctx.fireChannelInactive();
                                        getListeners().forEach(listener -> listener.handleDisconnected(channel));
                                    }

                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, EmptyPacket msg) {
                                        listeners.forEach(listener -> listener.handlePacket(channel, msg));
                                    }

                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    }
                                });
                    }
                });
                ChannelFuture f = b.connect(host, port).sync();
                f.channel().closeFuture().sync();
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    @Override
    public void close() {
        if (isConnected()) channel.close();
    }

    @Override
    public void sendPacket(EmptyPacket packet) {
        if (isConnected()) channel.sendPacket(packet);
    }

    @Override
    public NetClient addListener(INetListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public boolean isConnected() {
        return channel != null && channel.isConnected();
    }

    @Override
    public Collection<INetListener> getListeners() {
        return listeners;
    }

    public INetChannel getChannel() {
        return channel;
    }
}