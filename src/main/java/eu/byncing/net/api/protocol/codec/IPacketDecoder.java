package eu.byncing.net.api.protocol.codec;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface IPacketDecoder {

    Packet decode(Packet packet, IPacketBuffer buffer) throws IOException;

    class NetPacketDecoder implements IPacketDecoder {

        @Override
        public Packet decode(Packet packet, IPacketBuffer buffer) {
            try {
                String clazz = buffer.read("clazz", String.class);
                if (clazz == null) return null;
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