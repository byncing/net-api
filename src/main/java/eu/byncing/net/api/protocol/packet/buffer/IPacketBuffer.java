package eu.byncing.net.api.protocol.packet.buffer;

import java.util.List;
import java.util.UUID;

public interface IPacketBuffer {

    void writeString(String string);

    String readString();

    String readString(int length);

    void writeArray(byte[] array);

    byte[] toArray();

    byte[] readArray();

    byte[] readArray(int limit);

    int[] readIntArray();

    void writeStringArray(List<String> s);

    List<String> readStringArray();

    int readInt();

    int readInt(int maxBytes);

    void writeInt(int value);

    void writeBoolean(boolean bool);

    boolean  readBoolean();

    int readShort();

    void writeShort(int toWrite);

    void writeUUID(UUID value);

    UUID readUUID();

    void writeEnum(Enum<?> anEnum);

    <E extends Enum<E>> E readEnum(Class<E> enumClass);
}