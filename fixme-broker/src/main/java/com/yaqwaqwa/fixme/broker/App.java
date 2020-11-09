package com.yaqwaqwa.fixme.broker;

import com.yaqwaqwa.fixme.broker.controller.BrokerController;

/**
 * @author yaqwaqwa
 */
public class App {
    public static void main( String[] args ) {
        BrokerController controller = new BrokerController();
        controller.connect();
    }
}

