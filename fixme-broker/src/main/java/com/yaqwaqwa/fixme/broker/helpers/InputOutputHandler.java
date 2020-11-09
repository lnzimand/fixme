package com.yaqwaqwa.fixme.broker.helpers;

import com.yaqwaqwa.fixme.core.models.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            if (client.clientId == null) {
                client.clientId = message;
            }

            System.out.printf("Client ID[%s]: Server responded => %s%n", client.clientId, message);

            message = this.getTextFromUser();

            if (message.equalsIgnoreCase("exit") || message.equalsIgnoreCase("quit")) {
                try {
                    client.channel.shutdownInput();
                    client.channel.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
                return;
            }

            message = getFormattedFixMessage(message, client);

            client.isRead = false;
            client.buffer.clear();
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            client.buffer.put(data);
            client.buffer.flip();
            client.channel.write(client.buffer, client, this);

        } else {
            client.isRead = true;
            client.buffer.clear();
            client.channel.read(client.buffer, client, this);
        }

    }

    private String getFormattedFixMessage(String message, Client client) {
        if (message.equalsIgnoreCase("list"))
            return "list";
        else {
            String[] holder = message.split(" ");
            String secondPart;
            if (holder[0].equalsIgnoreCase("buy"))
                secondPart = String.format("35=%s|49=%s|56=%s|54=%s|44=%s|38=%s|", "D", client.clientId, holder[2], "1", "50", holder[1]);
            else
                secondPart = String.format("35=%s|49=%s|56=%s|54=%s|44=%s|38=%s|", "D", client.clientId, holder[2], "2", "50", holder[1]);
            return String.format("8=FIX.4.2|9=%d|%s", getBodyLength(secondPart), secondPart);
        }
    }

    private int getBodyLength(String secondPart) {
        secondPart = secondPart.replace("|", "\u0001");
        int sum = 0;
        for (int index = 0; index < secondPart.length(); index++)
            sum += secondPart.charAt(index);
        return sum % 256;
    }

    @Override
    public void failed(Throwable throwable, Client client) {
        throwable.printStackTrace();
    }

    private String getTextFromUser() {

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        String message = "";

        try {
            while (message.length() == 0 || !isInputValid(message)) {
                usage();
                System.out.print("Please enter command: ");
                message = console.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message.trim();
    }

    private boolean isInputValid(String string) {
        if (string.trim().length() == 0)
            return false;
        else if (string.trim().equalsIgnoreCase("list")
                || string.trim().equalsIgnoreCase("exit")
                || string.trim().equalsIgnoreCase("quit"))
            return true;
        else if (string.trim().split(" ").length == 3) {
            String[] holder = string.trim().split(" ");
            if (holder[0].equalsIgnoreCase("buy")
                    || holder[0].equalsIgnoreCase("sell")) {
                try {
                    Integer.parseInt(holder[1]);
                    Integer.parseInt(holder[2]);
                } catch (NumberFormatException e) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private void usage() {
        System.out.println("#########################################################");
        System.out.println("#     Usage:                                            #");
        System.out.println("#      <BUY>/<SELL>  <QUANTITY> <MARKET_ID> || <LIST>   #");
        System.out.println("#########################################################");
    }

}
