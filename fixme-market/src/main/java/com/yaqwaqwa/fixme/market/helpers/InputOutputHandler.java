package com.yaqwaqwa.fixme.market.helpers;

import com.yaqwaqwa.fixme.core.models.Client;

import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;


public class InputOutputHandler implements CompletionHandler<Integer, Client> {

    @Override
    public void completed(Integer result, Client client) {
        if (client.isRead) {
            client.buffer.flip();
            int limits = client.buffer.limit();
            byte[] bytes = new byte[limits];
            client.buffer.get(bytes, 0, limits);
            String message = new String(bytes, StandardCharsets.UTF_8);
            System.out.printf("Server responded: %s%n", message);

            if (client.clientId == null) {
                client.isRead = false;
                client.buffer.clear();
                byte[] data = ("ID [" + message + "] received").getBytes(StandardCharsets.UTF_8);
                client.buffer.put(data);
                client.buffer.flip();
                client.clientId = message;
                client.channel.write(client.buffer, client, this);
                return;
            }

            client.isRead = false;

            client.buffer.clear();
            MarketFIXmessages fiXmessages = new MarketFIXmessages(message);
            byte[] data = (fiXmessages.processRequest()).getBytes(StandardCharsets.UTF_8);
            client.buffer.put(data);
            client.buffer.flip();
            client.channel.write(client.buffer, client, this);

        } else {
            client.isRead = true;
            client.buffer.clear();
            client.channel.read(client.buffer, client, this);
        }

    }

    @Override
    public void failed(Throwable throwable, Client client) {
        throwable.printStackTrace();
    }

}

