package eu.byncing.net.example;

import eu.byncing.net.api.protocol.packet.EmptyPacket;
import eu.byncing.net.api.protocol.packet.buffer.IPacketBuffer;

public class PacketExample extends EmptyPacket {

    private String name, country;
    private int age;

    public PacketExample() {
        super();
    }

    public PacketExample(String name, String country, int age) {
        this.name = name;
        this.country = country;
        this.age = age;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.writeString(name);
        buffer.writeString(country);
        buffer.writeInt(age);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        name = buffer.readString();
        country = buffer.readString();
        age = buffer.readInt();
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }
}