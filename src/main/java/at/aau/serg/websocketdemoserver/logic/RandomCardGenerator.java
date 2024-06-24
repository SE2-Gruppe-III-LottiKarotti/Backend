package at.aau.serg.websocketdemoserver.logic;

import java.security.SecureRandom;


public class RandomCardGenerator {


    public static String startCardGenerator() {
        SecureRandom random = new SecureRandom();
        int numberToCalc = random.nextInt(46);

        return calculate(numberToCalc);
    }

    public static String calculate(int numberToCalc) {
        if (numberToCalc < 0 || numberToCalc > 45) {
            throw new IllegalArgumentException("error - number has to be between 0 and 45");
        }

        if (numberToCalc <=3) { // no barrier below, because result of modulo can't be negative
            return returningCard.THREE.toString();
        }
        else if (numberToCalc <=10) {
            return returningCard.TWO.toString();
        }
        else if (numberToCalc <=21) {
            return returningCard.CARROT.toString();

        }
        else {
            //from 22 to 45
            return returningCard.ONE.toString();
        }
    }


    public enum returningCard {
        ONE,
        TWO,
        THREE,
        CARROT
    }
}