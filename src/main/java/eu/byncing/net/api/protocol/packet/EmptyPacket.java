package eu.byncing.net.api.protocol.packet;

import eu.byncing.net.api.protocol.packet.buffer.IPacketBuffer;

import java.lang.reflect.InvocationTargetException;

public abstract class EmptyPacket {

    public static <P extends EmptyPacket> P typeInstance(EmptyPacket packet) {
        return (P) packet;
    }

    public static <P extends EmptyPacket> P newInstance(IPacketBuffer buffer) {
        String clazz = buffer.readString();
        if (clazz != null && clazz.length() > 0) {
            try {
                return (P) Class.forName(clazz).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public EmptyPacket() {
    }

    public abstract void write(IPacketBuffer buffer);

    public abstract void read(IPacketBuffer buffer);
}