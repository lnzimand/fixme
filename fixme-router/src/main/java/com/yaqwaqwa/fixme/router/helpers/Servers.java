package com.yaqwaqwa.fixme.router.helpers;

import com.yaqwaqwa.fixme.router.models.ClientInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class Servers implements Runnable {

    private final String HOST;
    private final int PORT_NUMBER;

    public Servers(String host, int PORT_NUMBER) {
        this.HOST = host;
        this.PORT_NUMBER = PORT_NUMBER;
    }

    @Override
    public void run() {
        try {
            AsynchronousServerSocketChannel channelServer = AsynchronousServerSocketChannel.open();
            channelServer.bind(new InetSocketAddress(HOST, PORT_NUMBER));
            System.out.printf("[SERVER] listening at %s%n", channelServer.getLocalAddress());

            ClientInfo clientInfo = new ClientInfo();
            clientInfo.server = channelServer;
            clientInfo.isClient = (PORT_NUMBER == 5000) ? true : false;
            channelServer.accept(clientInfo, new ConnectionHandler());

            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
