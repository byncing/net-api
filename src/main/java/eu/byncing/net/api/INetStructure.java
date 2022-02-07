package eu.byncing.net.api;

import eu.byncing.net.api.protocol.packet.IPacketSender;
import eu.byncing.net.api.protocol.packet.EmptyPacket;

import java.io.Closeable;
import java.util.Collection;

public interface INetStructure extends Closeable, IPacketSender {

    @Override
    void close();

    @Override
    void sendPacket(EmptyPacket packet);

    INetStructure addListener(INetListener listener);

    boolean isConnected();

    Collection<INetListener> getListeners();
}