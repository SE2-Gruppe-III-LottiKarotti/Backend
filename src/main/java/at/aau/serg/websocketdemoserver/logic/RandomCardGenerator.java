package at.aau.serg.websocketdemoserver.logic;

import at.aau.serg.websocketdemoserver.repository.InMemorySpielerRepo;

import java.util.Random;

// FIXME Unused
public class RandomCardGenerator {

    // FIXME confusing method name
    // FIXME unfitting return type for "1", "2", "3", should be complex object/enum
    public static String start() {
        Random random = new Random();

        // FIXME Simplify and refactor random number creation to not use modulo
        int randomNumber = random.nextInt(Integer.MAX_VALUE);

        int numberToCalc = randomNumber % 46;

        if (numberToCalc <=3) { // keine grenze nach unten, weil modulo!
            return "3"; //3 felder weiter
        }
        // FIXME first part of condition always true
        else if (numberToCalc >= 4 && numberToCalc <=10) {
            return "2"; //2 felder weiter
        }
        // FIXME first part of condition always true
        else if (numberToCalc >=11 && numberToCalc <=21) {
            return "Karotte"; //karotte drehen
        }
        else {
            //für 22 bis 45
            return "1"; //1 feld weiter
        }


    }
}
