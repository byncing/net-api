package eu.byncing.net.api.channel;

import eu.byncing.net.api.protocol.codec.IPacketDecoder;
import eu.byncing.net.api.protocol.codec.IPacketEncoder;

public class ChannelPipeline {

    private ChannelHandler handler = new ChannelHandler();

    private IPacketEncoder encoder = new IPacketEncoder.NetPacketEncoder();
    private IPacketDecoder decoder = new IPacketDecoder.NetPacketDecoder();

    public ChannelPipeline handle(ChannelHandler handler) {
        if (handler == null) return this;
        this.handler = handler;
        return this;
    }

    public ChannelPipeline codec(IPacketEncoder encoder) {
        if (encoder == null) return this;
        this.encoder = encoder;
        return this;
    }

    public ChannelPipeline codec(IPacketDecoder decoder) {
        if (decoder == null) return this;
        this.decoder = decoder;
        return this;
    }

    public ChannelHandler getHandler() {
        return handler;
    }

    public IPacketEncoder getEncoder() {
        return encoder;
    }

    public IPacketDecoder getDecoder() {
        return decoder;
    }
}