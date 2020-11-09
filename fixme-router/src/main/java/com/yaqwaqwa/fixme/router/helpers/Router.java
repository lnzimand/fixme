package com.yaqwaqwa.fixme.router.helpers;

import com.yaqwaqwa.fixme.router.models.ClientInfo;

public abstract class Router {

    public Router nextRouter;

    public Router(Router router) {
        this.nextRouter = router;
    }

    public abstract boolean performAction(ClientInfo clientInfo, Action action);
}
