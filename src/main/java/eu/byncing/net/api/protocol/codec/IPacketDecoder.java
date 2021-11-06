package eu.byncing.net.api.protocol.codec;

import eu.byncing.net.api.protocol.Packet;
import eu.byncing.net.api.protocol.PacketBuffer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface IPacketDecoder {

    Packet decode(Packet packet, PacketBuffer buffer) throws IOException;

    class NetPacketDecoder implements IPacketDecoder {

        @Override
        public Packet decode(Packet packet, PacketBuffer buffer) {
            try {
                String clazz = buffer.read("clazz", String.class);
                if (clazz == null) return new Packet();
                Packet instance = (Packet) Class.forName(clazz).getConstructor().newInstance();
                instance.read(buffer);
                return instance;
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return new Packet();
        }
    }
}