package eu.byncing.net.example;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

public class PacketExample extends Packet {

    private String name, country;
    private int age;

    public PacketExample() {}

    public PacketExample(String name, String country, int age) {
        this.name = name;
        this.country = country;
        this.age = age;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("name", name);
        buffer.write("country", country);
        buffer.write("age", age);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        name = buffer.read("name", String.class);
        country = buffer.read("country", String.class);
        age = buffer.read("age", Integer.class);
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