package com.yaqwaqwa.fixme.router.helpers;

import com.yaqwaqwa.fixme.core.util.Checksum;
import com.yaqwaqwa.fixme.router.models.ClientInfo;

public class ValidateChecksum extends Router {

    public ValidateChecksum(Router router) {
        super(router);
    }

    @Override
    public boolean performAction(ClientInfo clientInfo, Action action) {

        if (action.equals(Action.Checksum)) {
            if (clientInfo.isClient) {
                clientInfo.message += "10=" + Checksum.generateChecksum(clientInfo.message);
            } else {
                if (!Checksum.validateChecksum(clientInfo.message)) {
                    clientInfo.message = "Checksum didn't match";
                    return nextRouter.performAction(clientInfo, Action.ForwardMessage);
                }
            }
        }
        return nextRouter.performAction(clientInfo, Action.IdentifyDestination);
    }
}
