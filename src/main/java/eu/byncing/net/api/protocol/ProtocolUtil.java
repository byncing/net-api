package eu.byncing.net.api.protocol;

import io.netty.buffer.ByteBuf;

public class ProtocolUtil {

    public static void writeVarInt(int value, ByteBuf output) {
        int part;
        do {
            part = value & 0x7F;
            value >>>= 7;
            if (value != 0) {
                part |= 0x80;
            }
            output.writeByte(part);
        } while (value != 0);
    }

    public static int readVarInt(ByteBuf input) {
        return readVarInt(input, 5);
    }

    public static int readVarInt(ByteBuf input, int maxBytes) {
        int out = 0, bytes = 0;
        byte in;
        do {
            in = input.readByte();
            out |= (in & 0x7F) << (bytes++ * 7);
            if (bytes > maxBytes) throw new RuntimeException("VarInt too big");
        } while ((in & 0x80) == 0x80);
        return out;
    }
}