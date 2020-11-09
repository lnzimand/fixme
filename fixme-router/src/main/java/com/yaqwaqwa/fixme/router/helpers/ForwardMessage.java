package com.yaqwaqwa.fixme.router.helpers;

import com.yaqwaqwa.fixme.router.models.ClientInfo;

public class ForwardMessage extends Router {

    public ForwardMessage(Router router) {
        super(router);
    }

    @Override
    public boolean performAction(ClientInfo clientInfo, Action action) {
        if (action.equals(Action.ForwardMessage)) {
            clientInfo.isRead = false;
            clientInfo.client.write(clientInfo.buffer, clientInfo, clientInfo.IOHandler);
            return true;
        }
        return false;
    }
    
}
