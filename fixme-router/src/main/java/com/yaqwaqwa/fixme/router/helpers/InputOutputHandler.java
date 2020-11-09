package com.yaqwaqwa.fixme.router.helpers;

import com.yaqwaqwa.fixme.router.controller.RouterController;
import com.yaqwaqwa.fixme.router.models.ClientInfo;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class InputOutputHandler implements CompletionHandler<Integer, ClientInfo> {
    @Override
    public void completed(Integer result, ClientInfo clientInfo) {

        if (result == -1) {
            try {
                clientInfo.client.close();
                System.out.format("Stopped listening to the client at: %s%n", clientInfo.clientAddress);
                RouterController.unregisterClient(clientInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (clientInfo.isRead) {
            clientInfo.buffer.flip();
            int limits = clientInfo.buffer.limit();
            byte[] bytes = new byte[limits];

            clientInfo.buffer.get(bytes, 0, limits);
            String message = new String(bytes, StandardCharsets.UTF_8);
            System.out.printf("Client at %s says: %s%n", clientInfo.clientAddress, message);

            if (!clientInfo.isClient && message.contains("received")) {
                clientInfo.isRead = false;
                return;
            }
            clientInfo.message = message;
            if (clientInfo.client.isOpen()) {
                if (message.equalsIgnoreCase("list")) {
                    clientInfo.isRead = false;
                    clientInfo.buffer.clear();
                    byte[] data = RouterController.getMarkets().getBytes(StandardCharsets.UTF_8);
                    clientInfo.buffer.put(data);
                    clientInfo.buffer.flip();
                    clientInfo.client.write(clientInfo.buffer, clientInfo, this);
                } else {
                    Router router = getChainOfActions();
                    router.performAction(clientInfo, Action.Checksum);
                }
            }
        } else {
            clientInfo.isRead = true;
            clientInfo.buffer.clear();
            clientInfo.client.read(clientInfo.buffer, clientInfo,this);
        }
    }

    @Override
    public void failed(Throwable throwable, ClientInfo clientInfo) {
        throwable.printStackTrace();
    }

    private static Router getChainOfActions() {
        Router forwardMessage = new ForwardMessage(null);
        Router identifyDestination = new IdentifyDestination(forwardMessage);
        return new ValidateChecksum(identifyDestination);
    }

}