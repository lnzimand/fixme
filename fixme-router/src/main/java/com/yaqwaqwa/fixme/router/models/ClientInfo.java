package com.yaqwaqwa.fixme.router.models;

import com.yaqwaqwa.fixme.router.helpers.InputOutputHandler;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class ClientInfo {

    public AsynchronousServerSocketChannel server;
    public AsynchronousSocketChannel client;
    public ByteBuffer buffer;
    public SocketAddress clientAddress;
    public String message;
    public boolean isRead;
    public boolean isClient;
    public int clientId;
    public InputOutputHandler IOHandler;

}
