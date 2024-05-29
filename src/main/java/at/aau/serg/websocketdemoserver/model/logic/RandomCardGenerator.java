package at.aau.serg.websocketdemoserver.model.logic;

import java.security.SecureRandom;

// FIXME duplicate code of at.aau.serg.websocketdemoserver.logic.RandomCardGenerator, same issues
public class RandomCardGenerator {

    public static String start() {
        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(Integer.MAX_VALUE);

        int numberToCalc = randomNumber % 46;

        return calculate(numberToCalc);


    }
    private static String calculate(int numberToCalc) {
        if (numberToCalc <=3) { // keine grenze nach unten, weil modulo!
            return "3"; //3 felder weiter
        }
        else if (numberToCalc >= 4 && numberToCalc <=10) {
            return "2"; //2 felder weiter
        }
        else if (numberToCalc >=11 && numberToCalc <=21) {
            return "Karotte"; //karotte drehen
        }
        else {
            //für 22 bis 45
            return "1"; //1 feld weiter
        }
    }
}
