package com.yaqwaqwa.fixme.router.controller;

import com.yaqwaqwa.fixme.router.helpers.Servers;
import com.yaqwaqwa.fixme.router.models.ClientInfo;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouterController {

    private final String    HOST = "127.0.0.1";
    private final int       PORT_NUMBER = 5000;
    private static ArrayList<ClientInfo> clients = new ArrayList<>();

    public void startServers() {
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(new Servers(HOST, PORT_NUMBER));
        service.submit(new Servers(HOST, PORT_NUMBER + 1));
        service.shutdown();
    }

    public static void registerClient(ClientInfo client) {
        clients.add(client);
    }

    public static void unregisterClient(ClientInfo client) {
        clients.remove(client);
    }

    public static ClientInfo getClient(int id) {
        for (ClientInfo client : clients
        ) {
            if (id == client.clientId)
                return client;
        }
        return null;
    }

    public static ClientInfo getMarket(int id) {
        for (ClientInfo market : clients
        ) {
            if (market.clientId == id)
                return market;
        }
        return null;
    }

    public static String getMarkets() {

        String marketsID = null;
        for (ClientInfo market : clients
             ) {
            if (!market.isClient)
                if (marketsID == null) marketsID = "\n\nMARKETS AVAILABLE\n[MARKET_ID]: " + market.clientId + "\n";
                else marketsID += "[MARKET_ID]: " + market.clientId + "\n";
        }
        return marketsID;
    }

}
