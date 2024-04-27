package at.aau.serg.websocketdemoserver.model.game;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class Gameboard {
    private Feld[] felder;
    //private int carrotCounter;??
    SecureRandom random = new SecureRandom();
    private final int maxPosition; // Separate Variable für maximale Position des Spielbretts

    public Gameboard(int players) {
        this.felder = new Feld[26]; //26 felder inkl. karotte
        //this.carrotCounter = new Random().nextInt(12);??
        this.maxPosition = felder.length - 1; // Maximale Position ist die Länge des Arrays-1
        initFields();
        initSpecialFields();
        initFiguren(players);
    }

    //initFields
    private void initFields() {
        initMaulwurfhuegel();
        //TODO: maulwurfhügel initiieren --> diese per schleife

    }

    private void initMaulwurfhuegel() {
        int maxMaulwurfhuegel = 6; // Maximale Anzahl von Maulwurfhügeln

        // Zufällig ausgewählte Felder als Maulwurfhügel markieren
        for (int i = 0; i < maxMaulwurfhuegel; i++) {
            int randomIndex = random.nextInt(felder.length);
            if (felder[randomIndex] == null) {
                felder[randomIndex] = new Feld(false);
            }
            if (!felder[randomIndex].isIstEsEinMaulwurfLoch()) {
                felder[randomIndex].setIstEsEinMaulwurfLoch(true);
            } else {
                // Falls das Feld bereits ein Maulwurfhügel ist, versuche ein anderes Feld zu wählen
                i--;
            }
        }
    }

    private void initSpecialFields() {
        // Maulwurfshöhle
        int maulwurfshoehleIndex = 3;
        felder[maulwurfshoehleIndex] = new Feld(false); // Maulwurfshöhle-Feld

        // Hochgeklappte Brücke
        int brueckeIndex = 8;
        felder[brueckeIndex] = new Feld(false); // Brücke-Feld (hochgeklappt)

        // Schwingendes Gatter
        int gatterIndex = 20;
        felder[gatterIndex] = new Feld(false); // Gatter-Feld

        // Weitere Felder initialisieren
        for (int i = 0; i < felder.length; i++) {
            if (i != maulwurfshoehleIndex && i != brueckeIndex && i != gatterIndex && felder[i] == null) {
                // Normale Felder initialisieren
                felder[i] = new Feld(false);
                }
            }
        }

    private void initFiguren(int players) {
        //Jeder Spieler hat 4 Spielfiguren
        if(players>4 || players<2){
            throw new IllegalArgumentException("Die Spieleranzahl muss zwischen 2 und 4 liegen.");
        }
        int maxFiguresPerPlayer = 4;

        for (int i = 0; i < players; i++) {
            Spieler spieler = new Spieler("Spieler " + (i + 1)); // Spieler erstellen
            spieler.setFarbe(Spieler.Farbe.values()[i]); // Farbe setzen, abhängig von Spielerindex
            ArrayList<Spielfigur> spielfiguren = new ArrayList<>();

            // Spielfiguren für diesen Spieler erstellen und der Liste hinzufügen
            for (int j = 0; j < maxFiguresPerPlayer; j++) {
                Spielfigur spielfigur = new Spielfigur(j, spieler);
                spielfiguren.add(spielfigur);
            }

            spieler.setUnusedBunnies(spielfiguren); // Spielfiguren dem Spieler hinzufügen
        }
    }

    public void playerMove(Spieler spieler) {
        String card = RandomCardGenerator.start();
        System.out.println(spieler.getName() + " drew a card: " + card);

        // Hole die Spielfiguren des Spielers
        ArrayList<Spielfigur> spielfiguren = spieler.getUnusedBunnies();

        // Bewege jede Spielfigur entsprechend der gezogenen Karte
        for (Spielfigur spielfigur : spielfiguren) {
            int newPosition = spielfigur.getPosition();

            // Entscheide, wie weit die Spielfigur bewegt werden soll, basierend auf der gezogenen Karte
            switch (card) {
                case "3":
                    newPosition += 3;
                    break;
                case "2":
                    newPosition += 2;
                    break;
                case "Karotte":
                    // Logik für die Karottenkarte
                    for (Feld feld : felder) {
                        if (feld != null && feld.isIstEsEinMaulwurfLoch()) {
                            // Wenn eine Spielfigur auf einem Feld steht, das nach dem Drehen ein Maulwurfsloch wird
                            if (feld.getSpielfigur() != null) {
                                // Entferne die Spielfigur von diesem Feld
                                feld.setSpielfigur(null);
                            }
                        }
                    }
                    break;
                case "1":
                    newPosition += 1;
                default:
                    break;
            }

            // Überprüfe, ob die neue Position gültig ist und ob Feld besetzt ist
            while (newPosition <= maxPosition) {
                Feld newField = felder[newPosition];
                if (newField.isIstEsEinMaulwurfLoch()) {
                    System.out.println("Oh no! " + spieler.getName() + " fell into a mole hole!");
                    // Setze die Position der Spielfigur auf die Startposition
                    spielfigur.setPosition(0);
                    break; // Beende die Schleife, da die Bewegung abgebrochen wurde
                } else if (newField.getSpielfigur() != null) {
                    System.out.println("Oops! " + spieler.getName() + " skipped over a field because it's occupied!");
                    newPosition++; // Bewege die Spielfigur auf das nächste Feld
                } else {
                    // Setze die Position der Spielfigur auf das neue Feld
                    spielfigur.setPosition(newPosition);
                    break; // Beende die Schleife, da die Bewegung erfolgreich war
                }
            }
            if (newPosition > maxPosition) {
                System.out.println(spieler.getName() + " cannot move any further!");
            }
        }
    }

    public boolean checkWinCondition(Spieler spieler) {
        // Überprüfe, ob der Spieler genug Karotten gesammelt hat
        return spieler.hasReachedCarrot();
    }

    public Feld[] getFelder() {
        return felder;
    }

    public void setFelder(Feld[] felder) {
        this.felder = felder;
    }

    /*public int getCarrotCounter() {
        return carrotCounter;
    }

    public void setCarrotCounter(int carrotCounter) {
        this.carrotCounter = carrotCounter;
    }*/
}
