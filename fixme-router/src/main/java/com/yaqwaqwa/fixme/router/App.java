package com.yaqwaqwa.fixme.router;

import com.yaqwaqwa.fixme.router.controller.RouterController;

/**
 * @author yaqwaqwa
 *
 */
public class App {
    public static void main( String[] args ) {
        RouterController routerController = new RouterController();
        routerController.startServers();
    }
}
