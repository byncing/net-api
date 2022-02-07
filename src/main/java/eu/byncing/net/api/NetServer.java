package eu.byncing.net.api;

import eu.byncing.net.api.channel.INetChannel;
import eu.byncing.net.api.channel.NettyChannel;
import eu.byncing.net.api.protocol.VarInt21FrameDecoder;
import eu.byncing.net.api.protocol.VarInt21LengthFieldPrepender;
import eu.byncing.net.api.protocol.packet.EmptyPacket;
import eu.byncing.net.api.protocol.packet.codec.PacketDecoder;
import eu.byncing.net.api.protocol.packet.codec.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetServer implements INetStructure {

    private final List<INetListener> listeners = new ArrayList<>();

    private final Collection<INetChannel> channels = new ConcurrentLinkedQueue<>();

    private Channel channel;

    public void bind(int port) {
        new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {
                                ch.pipeline().addLast(
                                        new VarInt21FrameDecoder(),
                                        new PacketDecoder(),
                                        new VarInt21LengthFieldPrepender(),
                                        new PacketEncoder(),
                                        new SimpleChannelInboundHandler<EmptyPacket>() {

                                            @Override
                                            public void handlerAdded(ChannelHandlerContext ctx) {
                                                if (!isConnected()) return;
                                                NettyChannel channel = new NettyChannel(ctx.channel());
                                                getChannels().add(channel);
                                                getListeners().forEach(listener -> listener.handleConnected(channel));
                                            }

                                            @Override
                                            public void handlerRemoved(ChannelHandlerContext ctx) {
                                                if (!isConnected()) return;
                                                INetChannel channel = getChannels().stream().filter(nettyChannel -> nettyChannel.getSocket().equals(ctx.channel())).findFirst().orElse(null);
                                                if (channel == null) return;
                                                getChannels().remove(channel);
                                                getListeners().forEach(listener -> listener.handleDisconnected(channel));
                                            }

                                            @Override
                                            protected void channelRead0(ChannelHandlerContext ctx, EmptyPacket msg) {
                                                if (!isConnected()) return;
                                                INetChannel channel = getChannels().stream().filter(nettyChannel -> nettyChannel.getSocket().equals(ctx.channel())).findFirst().orElse(null);
                                                if (channel == null) return;
                                                getListeners().forEach(listener -> listener.handlePacket(channel, msg));
                                            }

                                            @Override
                                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                            }
                                        });
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                ChannelFuture f = b.bind(port).sync();
                channel = f.channel();
                channel.closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }).start();
    }

    @Override
    public void close() {
        if (isConnected()) {
            for (INetChannel channel : channels) channel.close();
            channel.close();
        }
    }

    @Override
    public void sendPacket(EmptyPacket packet) {
        if (isConnected()) return;
        channels.forEach(channel -> channel.sendPacket(packet));
    }

    @Override
    public NetServer addListener(INetListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

    @Override
    public Collection<INetListener> getListeners() {
        return listeners;
    }

    public Collection<INetChannel> getChannels() {
        return channels;
    }
}