package at.aau.serg.websocketdemoserver.model.logic;

import java.util.Random;

public class RandomCardGenerator {

    public static String startCardGenerator() {
        Random random = new Random();
        int randomNumber = random.nextInt(Integer.MAX_VALUE);
        int numberToCalc = randomNumber % 46;

        return calculate(numberToCalc);
    }

    protected static String calculate(int numberToCalc) {
        if (numberToCalc < 0 || numberToCalc > 45) {
            throw new IllegalArgumentException("error - number has to be between 0 and 45");
        }
        if (numberToCalc <=3) { // keine grenze nach unten, weil modulo!
            return returningCard.THREE.toString(); //3 felder weiter
        }
        else if (numberToCalc >= 4 && numberToCalc <=10) {
            return returningCard.TWO.toString(); //2 felder weiter
        }
        else if (numberToCalc >=11 && numberToCalc <=21) {
            return returningCard.CARROT.toString(); //karotte drehen
        }
        else {
            //für 22 bis 45
            return returningCard.ONE.toString(); //1 feld weiter
        }
    }


    public enum returningCard {
        ONE,
        TWO,
        THREE,
        CARROT
    }
}
