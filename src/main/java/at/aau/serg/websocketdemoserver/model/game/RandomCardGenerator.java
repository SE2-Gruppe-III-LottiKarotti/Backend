package at.aau.serg.websocketdemoserver.model.game;

import java.security.SecureRandom;

public class RandomCardGenerator implements CardGenerator {
    @Override
    public String drawCard() {
        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(46); // Es gibt 46 mögliche Werte (0 bis 45)

        if (randomNumber <= 3) { // keine Grenze nach unten, weil modulo!
            return "3"; // 3 Felder weiter
        } else if (randomNumber >= 4 && randomNumber <= 10) {
            return "2"; // 2 Felder weiter
        } else if (randomNumber >= 11 && randomNumber <= 21) {
            return "Karotte"; // Karotte drehen
        } else {
            // für 22 bis 45
            return "1"; // 1 Feld weiter
        }
    }
}
