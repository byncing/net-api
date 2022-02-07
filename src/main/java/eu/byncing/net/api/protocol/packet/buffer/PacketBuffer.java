package eu.byncing.net.api.protocol.packet.buffer;


import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketBuffer implements IPacketBuffer {

    private final ByteBuf buf;

    public PacketBuffer(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public void writeString(String s) {
        if (s.length() > Short.MAX_VALUE) {
            throw new RuntimeException("Cannot send string longer than Short.MAX_VALUE (got " + s.length() + " characters)");
        }
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        writeInt(b.length);
        buf.writeBytes(b);
    }

    @Override
    public String readString() {
        return readString(Short.MAX_VALUE);
    }

    @Override
    public String readString(int maxLen) {
        int len = readInt();
        if (len > maxLen * 4) {
            throw new RuntimeException("Cannot receive string longer than " + maxLen * 4 + " (got " + len + " bytes)");
        }
        byte[] b = new byte[len];
        buf.readBytes(b);
        String s = new String(b, StandardCharsets.UTF_8);
        if (s.length() > maxLen) {
            throw new RuntimeException("Cannot receive string longer than " + maxLen + " (got " + s.length() + " characters)");
        }

        return s;
    }

    @Override
    public void writeArray(byte[] b) {
        if (b.length > Short.MAX_VALUE) {
            throw new RuntimeException("Cannot send byte array longer than Short.MAX_VALUE (got " + b.length + " bytes)");
        }
        writeInt(b.length);
        buf.writeBytes(b);
    }

    @Override
    public byte[] toArray() {
        byte[] ret = new byte[buf.readableBytes()];
        buf.readBytes(ret);
        return ret;
    }

    @Override
    public byte[] readArray() {
        return readArray(buf.readableBytes());
    }

    @Override
    public byte[] readArray(int limit) {
        int len = readInt();
        if (len > limit) {
            throw new RuntimeException("Cannot receive byte array longer than " + limit + " (got " + len + " bytes)");
        }
        byte[] ret = new byte[len];
        buf.readBytes(ret);
        return ret;
    }

    @Override
    public int[] readIntArray() {
        int len = readInt();
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) ret[i] = readInt();
        return ret;
    }

    @Override
    public void writeStringArray(List<String> s) {
        writeInt(s.size());
        for (String str : s) {
            writeString(str);
        }
    }

    @Override
    public List<String> readStringArray() {
        int len = readInt();
        List<String> ret = new ArrayList<>(len);
        for (int i = 0; i < len; i++) ret.add(readString());
        return ret;
    }

    @Override
    public int readInt() {
        return readInt(5);
    }

    @Override
    public int readInt(int maxBytes) {
        int out = 0, bytes = 0;
        byte in;
        do {
            in = buf.readByte();
            out |= (in & 0x7F) << (bytes++ * 7);
            if (bytes > maxBytes) throw new RuntimeException("VarInt too big");
        } while ((in & 0x80) == 0x80);
        return out;
    }

    @Override
    public void writeInt(int value) {
        int part;
        do {
            part = value & 0x7F;
            value >>>= 7;
            if (value != 0) part |= 0x80;
            buf.writeByte(part);
        } while (value != 0);
    }

    @Override
    public void writeBoolean(boolean bool) {
        int value = 0;
        if (bool) value = 1;
        writeInt(value);
    }

    @Override
    public boolean readBoolean() {
        return readInt() == 1;
    }

    @Override
    public int readShort() {
        int low = buf.readUnsignedShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = buf.readUnsignedByte();
        }
        return ((high & 0xFF) << 15) | low;
    }

    @Override
    public void writeShort(int toWrite) {
        int low = toWrite & 0x7FFF;
        int high = (toWrite & 0x7F8000) >> 15;
        if (high != 0) low = low | 0x8000;
        buf.writeShort(low);
        if (high != 0) {
            buf.writeByte(high);
        }
    }

    @Override
    public void writeUUID(UUID value) {
        buf.writeLong(value.getMostSignificantBits());
        buf.writeLong(value.getLeastSignificantBits());
    }

    @Override
    public UUID readUUID() {
        return new UUID(buf.readLong(), buf.readLong());
    }

    @Override
    public void writeEnum(Enum<?> anEnum) {
        writeInt(anEnum.ordinal());
    }

    @Override
    public <E extends Enum<E>> E readEnum(Class<E> enumClass) {
        int value = readInt();
        return value != -1 ? enumClass.getEnumConstants()[value] : null;
    }
}