package com.yaqwaqwa.fixme.core.util;

import java.util.Random;

public class Checksum {

    public static int generateRandomSixDigitNumber() {
        Random random = new Random();
        return Integer.parseInt(String.format("%06d", random.nextInt(999999)));
    }

    public static String getChecksum(String fixMessage) {
        String[] array = fixMessage.split("\u0001");
        for (String string: array
        ) {
            if (string.contains("10=")) {
                return string.substring(string.indexOf("=") + 1);
            }
        }
        return null;
    }

    public static String generateChecksum(String fixMessage) {
        int sum = 0;
        for (int index = 0; index < fixMessage.length(); index++)
            sum += fixMessage.charAt(index);
        return String.format("%03d", sum & 0xFF);
    }

    public static boolean validateChecksum(String fixMessage) {
        String string = null;
        fixMessage = fixMessage.replace("|", "\u0001");
        if (fixMessage.contains("\u000135=") || fixMessage.contains("\u000110="))
            string = fixMessage.substring(fixMessage.indexOf("\u000135="), fixMessage.indexOf("\u000110=") + 1);

        return string != null && generateChecksum(string).equals(getChecksum(fixMessage));
    }

}
