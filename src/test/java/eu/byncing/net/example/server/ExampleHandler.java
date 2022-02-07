package eu.byncing.net.example.server;

import eu.byncing.net.api.channel.INetChannel;
import eu.byncing.net.api.protocol.packet.IPacketHandler;
import eu.byncing.net.example.PacketExample;

public class ExampleHandler implements IPacketHandler<PacketExample> {
    @Override
    public void handle(INetChannel channel, PacketExample packet) {
        System.out.println("[Server] Channel" + channel.getSocket().remoteAddress() + " data(name: " +
                packet.getName() + ", country: " +
                packet.getCountry() + ", age: " +
                packet.getAge() + ")");
    }

    @Override
    public Class<?>[] getClasses() {
        return new Class[]{PacketExample.class};
    }
}