# net-api
A simple network library for dealing with sockets


# Installation

### Gradle:

````gradle
repositories {
    mavenCentral()
    maven { url('https://byncing.eu/repository/snapshots/') }
}

dependencies {
    implementation('eu.byncing:net-api:1.0.0-SNAPSHOT')
}
````
# Example

### Packet:

````java
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
````

### Server:

```java
package eu.byncing.net.example.server;

import eu.byncing.net.api.NetOption;
import eu.byncing.net.api.NetServer;
import eu.byncing.net.api.channel.ChannelHandler;
import eu.byncing.net.api.channel.ChannelPipeline;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.example.PacketExample;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) {
        try {
            NetServer server = new NetServer();
            server.option(NetOption.TIMEOUT, 15000).init(channel -> {
                System.out.println("[Server] Channel" + channel.getRemoteAddress() + " has initialized.");

                ChannelPipeline pipeline = channel.getPipeline();
                pipeline.handle(new ChannelHandler() {
                    @Override
                    public void handleConnected(IChannel channel) {
                        System.out.println("[Server] Channel" + channel.getRemoteAddress() + " has connected.");
                        channel.sendPacket(new PacketExample("byncing", "Germany", 19));
                    }

                    @Override
                    public void handleDisconnected(IChannel channel) {
                        System.out.println("[Server] Channel" + channel.getRemoteAddress() + " has disconnected.");
                    }
                });
            }).bind(new InetSocketAddress(25565));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```


### Client:
```java
package eu.byncing.net.example.client;

import eu.byncing.net.api.NetClient;
import eu.byncing.net.api.channel.ChannelHandler;
import eu.byncing.net.api.channel.ChannelPipeline;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;
import eu.byncing.net.example.PacketExample;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Client {

    public static void main(String[] args) {
        try {
            NetClient client = new NetClient();
            client.init(channel -> {
                System.out.println("[Client] Channel" + channel.getRemoteAddress() + " has initialized.");

                ChannelPipeline pipeline = channel.getPipeline();
                pipeline.handle(new ChannelHandler() {
                    @Override
                    public void handleConnected(IChannel channel) {
                        System.out.println("[Client] Channel" + channel.getRemoteAddress() + " has connected.");
                    }

                    @Override
                    public void handlePacket(IChannel channel, Packet packet) {
                        if (packet instanceof PacketExample example) {
                            System.out.println("[Client] Channel" + channel.getRemoteAddress() + " Channel data: name " + example.getName() + ", country " + example.getCountry() + ", age " + example.getAge());
                        }
                    }

                    @Override
                    public void handleDisconnected(IChannel channel) {
                        System.out.println("[Client] Channel" + channel.getRemoteAddress() + " has disconnected.");
                    }
                });
            }).connect(new InetSocketAddress("127.0.0.1", 25565));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
