package com.yaqwaqwa.fixme.core.messages;

public abstract class FIXmessage {

    protected String    beginString;
    protected int       bodyLength;
    protected char      msgType;
    protected int       senderClientID;
    protected int       targetMarketID;
    protected int       side;
    protected int       price;
    protected int       orderQty;

    public FIXmessage(String string) {
        convert(string);
    }

    private void convert(String string) {
        String[] splitString = string.split("\\|");
        for (String data: splitString
             ) {
                if (data.contains("8=FIX"))
                    beginString = data;
                else if (data.contains("|9="))
                    bodyLength = Integer.parseInt(data.split("=")[1]);
                else if (data.contains("35="))
                    msgType = data.charAt(3);
                else if (data.contains("49="))
                    senderClientID = Integer.parseInt(data.split("=")[1]);
                else if (data.contains("56="))
                    targetMarketID = Integer.parseInt(data.split("=")[1]);
                else if (data.contains("54="))
                    side = Integer.parseInt(data.split("=")[1]);
                else if (data.contains("44="))
                    price = Integer.parseInt(data.split("=")[1]);
                else orderQty = Integer.parseInt(data.split("=")[1]);
        }
    }

}
