package com.yaqwaqwa.fixme.market.controller;

import com.yaqwaqwa.fixme.core.models.Client;
import com.yaqwaqwa.fixme.market.helpers.InputOutputHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

public class MarketController {

    private static int quantity;
    private static int price;
    private static int capital;
    private final String    HOST = "127.0.0.1";
    private final int       PORT_NUMBER = 5001;

    public MarketController(int quantity, int price) {
        MarketController.quantity = quantity;
        MarketController.price = price;
    }

    public void connect() {
        try {
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            SocketAddress serverAddress = new InetSocketAddress(HOST, PORT_NUMBER);
            socketChannel.connect(serverAddress).get();

            System.out.printf("Connected to [SERVER]. Client at address: %s%n", socketChannel.getLocalAddress());

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

    public static int getPrice() {
        return price;
    }

    public static void setPrice(int price) {
        MarketController.price = price;
    }

    public static int getCapital() {
        return capital;
    }

    public static void setCapital(int capital) {
        MarketController.capital = capital;
    }

    public static int getQuantity() {
        return quantity;
    }

    public static void setQuantity(int quantity) {
        MarketController.quantity = quantity;
    }

}

