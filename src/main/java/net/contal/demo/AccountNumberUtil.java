package net.contal.demo;

import java.util.Random;

public abstract class AccountNumberUtil {

    private static Random rand = new Random();

    /**
     * TODO implement this function
     * this function should generate random integer number and return
     * @return random integer
     */
    public static int generateAccountNumber(){
        //TODO help use Random  class part of java SDK
        int randNum = rand.nextInt(100);
        return randNum + 1;
    }

}
