package com.yaqwaqwa.fixme.broker.controller;

import com.yaqwaqwa.fixme.broker.helpers.InputOutputHandler;
import com.yaqwaqwa.fixme.core.models.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

public class BrokerController {

    private final String    HOST = "127.0.0.1";
    private final int       PORT_NUMBER = 5000;

    public void connect() {
        try {
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            SocketAddress serverAddress = new InetSocketAddress(HOST, PORT_NUMBER);
            socketChannel.connect(serverAddress).get();

            log("Connected to [SERVER]. Client at address: " + socketChannel.getLocalAddress());

            Client client = new Client();
            client.channel = socketChannel;
            client.buffer = ByteBuffer.allocate(1024);
            client.isRead = true;
            client.mainThread = Thread.currentThread();

            InputOutputHandler ioHandler = new InputOutputHandler();
            socketChannel.read(client.buffer, client, ioHandler);
            client.mainThread.join();

        } catch (InterruptedException | IOException | ExecutionException e) {
            System.err.println(e.getMessage());
        }

    }

    private void log(String string) {
        System.out.println(string);
    }

}

