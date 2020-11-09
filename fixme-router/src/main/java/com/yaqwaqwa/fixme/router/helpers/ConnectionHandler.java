package com.yaqwaqwa.fixme.router.helpers;

import com.yaqwaqwa.fixme.core.util.Checksum;
import com.yaqwaqwa.fixme.router.controller.RouterController;
import com.yaqwaqwa.fixme.router.models.ClientInfo;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, ClientInfo> {

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, ClientInfo clientInfo) {
        try {
            SocketAddress clientAddress = socketChannel.getRemoteAddress();
            System.out.printf("Accepted a connection from: %s%n", clientAddress);
            clientInfo.server.accept(clientInfo, this);

            InputOutputHandler ioHandler = new InputOutputHandler();
            ClientInfo newClient = new ClientInfo();
            newClient.server = clientInfo.server;
            newClient.client = socketChannel;
            newClient.buffer = ByteBuffer.allocate(1024);
            newClient.isRead = false;
            newClient.clientAddress = clientAddress;
            newClient.isClient = clientInfo.isClient;
            newClient.clientId = Checksum.generateRandomSixDigitNumber();
            newClient.IOHandler = ioHandler;
            byte[] clientId = Integer.toString(newClient.clientId).getBytes(StandardCharsets.UTF_8);
            newClient.buffer.put(clientId);
            newClient.buffer.flip();
            RouterController.registerClient(newClient);

            socketChannel.write(newClient.buffer, newClient, ioHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable throwable, ClientInfo clientInfo) {
        System.out.println("Failed to accept connection.");
        throwable.printStackTrace();
    }

}
