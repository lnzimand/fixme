package com.yaqwaqwa.fixme.market;

import com.yaqwaqwa.fixme.market.controller.MarketController;

/**
 * @author yaqwaqwa
 *
 */
public class App 
{
    private static boolean isInputValid(String[] args) {
        try {
            for (String string : args
                 ) {
                Integer.parseInt(string);
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static void usage()  {
        System.out.println("####################################################################");
        System.out.println("#                             ERROR                                #");
        System.out.println("# Usage:                                                           #");
        System.out.println("#       execute.sh <quantity : int> <price : int>                  #");
        System.out.println("####################################################################");
        System.out.println("#  *quantity: number of items you're selling                       #");
        System.out.println("#  *price: how much are you selling for                            #");
        System.out.println("####################################################################");
    }

    public static void main( String[] args ) {
        if (args.length != 2 || !isInputValid(args)) {
            usage();
        } else {
            MarketController controller = new MarketController(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            controller.connect();
        }
    }

}
