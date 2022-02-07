# net-api

A simple network library for dealing with sockets

# Example

### Packet

````java
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
````

### Server

```java
package eu.byncing.net.example.server;

import eu.byncing.net.api.INetListener;
import eu.byncing.net.api.NetServer;
import eu.byncing.net.api.channel.INetChannel;
import eu.byncing.net.api.protocol.packet.EmptyPacket;
import eu.byncing.net.example.PacketExample;

public class Server {

    public static void main(String[] args) {
        NetServer server = new NetServer();
        server.addListener(new INetListener() {
            @Override
            public void handleConnected(INetChannel channel) {
                System.out.println("[Server] Channel" + channel.getSocket().remoteAddress() + " has connected.");
            }

            @Override
            public void handleDisconnected(INetChannel channel) {
                System.out.println("[Server] Channel" + channel.getSocket().remoteAddress() + " has disconnected.");
            }

            @Override
            public void handlePacket(INetChannel channel, EmptyPacket packet) {
                if (packet instanceof PacketExample) {
                    PacketExample example = (PacketExample) packet;
                    System.out.println("[Server] name: " + example.getName() + ", country: " + example.getCountry() + ", age: " + example.getAge());
                }
            }
        }).bind(3000);
    }
}
```

### Client

```java
package eu.byncing.net.example.client;

import eu.byncing.net.api.INetListener;
import eu.byncing.net.api.NetClient;
import eu.byncing.net.api.channel.INetChannel;
import eu.byncing.net.api.protocol.packet.EmptyPacket;
import eu.byncing.net.example.PacketExample;

public class Client {

    public static void main(String[] args) {
        NetClient client = new NetClient();
        client.addListener(new INetListener() {
            @Override
            public void handleConnected(INetChannel channel) {
                System.out.println("[Client] Channel" + channel.getSocket().remoteAddress() + " has connected.");

                channel.sendPacket(new PacketExample("byncing", "Germany", 19));
            }

            @Override
            public void handleDisconnected(INetChannel channel) {
                System.out.println("[Client] Channel" + channel.getSocket().remoteAddress() + " has disconnected.");
            }

            @Override
            public void handlePacket(INetChannel channel, EmptyPacket packet) {
            }
        }).connect("127.0.0.1", 3000);
    }
}
```