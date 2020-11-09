package com.yaqwaqwa.fixme.market.helpers;

import com.yaqwaqwa.fixme.core.messages.FIXmessage;
import com.yaqwaqwa.fixme.core.util.Checksum;
import com.yaqwaqwa.fixme.market.controller.MarketController;

public class MarketFIXmessages extends FIXmessage {

    public MarketFIXmessages(String string) {
        super(string);
    }

    public String processRequest() {

        String string;
        if (side == 2 && msgType == 'D') string = buyFromClient();
        else if (side == 1 && msgType == 'D') string = sellToClient();
        else string = processRequest("Invalid");

        return string;
    }

    private String buyFromClient() {

        if (MarketController.getCapital() < MarketController.getPrice())
            return processRequest("[ORDER REJECTED] MARKET not yet ready to BUY");
        else if (MarketController.getQuantity() > 999)
            return processRequest("[ORDER REJECTED] MARKET satisfied with what they currently have");
        else if (orderQty + MarketController.getQuantity() > 1000)
            return processRequest("[ORDER REJECTED] MARKET can only buy " + (1000 - MarketController.getQuantity()) + " worth of stock");
        else {
            MarketController.setQuantity(MarketController.getQuantity() + orderQty);
            MarketController.setCapital(MarketController.getCapital() - (orderQty * 50));
            return processRequest("[ORDER ACCEPTED] Order was successful");
        }

    }

    private String sellToClient() {

        if (MarketController.getQuantity() < 10)
            return processRequest("[ORDER REJECTED] Not enough stock to sell");
        else if (orderQty > MarketController.getQuantity())
            return processRequest("[ORDER REJECTED] Not enough stock for this order");
        else {
            MarketController.setQuantity(MarketController.getQuantity() - orderQty);
            MarketController.setCapital(MarketController.getCapital() + (orderQty * 50));
            return processRequest("[ORDER ACCEPTED] Order was successful");
        }

    }

    private String processRequest(String string) {
        String fixString = String.format("%s|9=%s|35=%s|49=%s|56=%s|58=%s|", beginString, bodyLength, msgType, targetMarketID, senderClientID, string);
        String checksumString = fixString.replace("|", "\u0001");
        String checksum = Checksum.generateChecksum(checksumString.substring(checksumString.indexOf("\u000135=")));
        return fixString + "10=" + checksum;
    }

}
