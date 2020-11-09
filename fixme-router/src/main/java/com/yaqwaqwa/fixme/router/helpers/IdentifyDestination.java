package com.yaqwaqwa.fixme.router.helpers;

import com.yaqwaqwa.fixme.router.controller.RouterController;
import com.yaqwaqwa.fixme.router.models.ClientInfo;

import java.nio.charset.StandardCharsets;

public class IdentifyDestination extends Router {

    ClientInfo toClient;
    ClientInfo fromClient;

    public IdentifyDestination(Router router) {
        super(router);
    }

    @Override
    public boolean performAction(ClientInfo clientInfo, Action action) {

        fromClient = clientInfo;
        if (action.equals(Action.IdentifyDestination)) {
            if (clientInfo.isClient) {
                toClient = RouterController.getMarket(Integer.parseInt(clientInfo.message.substring(clientInfo.message.indexOf("56=") + 3, clientInfo.message.indexOf("|54="))));
                if (toClient != null) {
                    prepareToClient();
                } else {
                    prepareToClient("Market ID entered is unavailable, please try again later");
                    return nextRouter.performAction(fromClient, Action.ForwardMessage);
                }
            } else {
                String id = null;
                if (clientInfo.message.contains("56=")) {
                    id = clientInfo.message.substring(clientInfo.message.indexOf("56=") + 3, clientInfo.message.indexOf("56=") + 9);
                    toClient = RouterController.getClient(Integer.parseInt(id));
                    prepareToClient();
                }
            }
        }
        return nextRouter.performAction(toClient, Action.ForwardMessage);
    }

    private void prepareToClient() {
        byte[] data = fromClient.message.getBytes(StandardCharsets.UTF_8);
        toClient.buffer.clear();
        toClient.buffer.put(data);
        toClient.buffer.flip();
    }

    private void prepareToClient(String string) {
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        fromClient.buffer.clear();
        fromClient.buffer.put(data);
        fromClient.buffer.flip();
    }
    
}
