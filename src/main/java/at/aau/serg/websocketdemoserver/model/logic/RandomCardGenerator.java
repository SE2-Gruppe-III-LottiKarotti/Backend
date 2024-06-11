package at.aau.serg.websocketdemoserver.model.logic;

import java.security.SecureRandom;

public class RandomCardGenerator {

    public static String startCardGenerator() {
        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(Integer.MAX_VALUE);
        int numberToCalc = randomNumber % 46;

        return calculate(numberToCalc);
    }

    private static String calculate(int numberToCalc) {

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

  /*
    private static String calculate(int numberToCalc) {
        int randomIndex = numberToCalc % 4;
        switch (randomIndex) {
            case 0:
                return "1";
            case 1:
                return "2";
            case 2:
                return "3";
            case 3:
                return "Karotte";
            default:
                throw new RuntimeException("Unreachable code");
        }
    }*/


    public enum returningCard {
        ONE,
        TWO,
        THREE,
        CARROT
    }
}
