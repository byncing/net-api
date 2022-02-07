package eu.byncing.net.api.protocol.packet;

import eu.byncing.net.api.channel.INetChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketRegistry {

    private final List<IPacketHandler<?>> handlers = new ArrayList<>();

    public void addHandlers(IPacketHandler<?>... handlers) {
        this.handlers.addAll(Arrays.asList(handlers));
    }

    public void process(INetChannel channel, EmptyPacket packet) {
        handlers.forEach(handler -> {
            if (Arrays.stream(handler.getClasses()).anyMatch(aClass -> aClass.equals(packet.getClass()))) {
                handler.handle(channel, EmptyPacket.typeInstance(packet));
            }
        });
    }

    public List<IPacketHandler<?>> getHandlers() {
        return handlers;
    }
}