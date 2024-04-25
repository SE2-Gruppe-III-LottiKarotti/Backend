package at.aau.serg.websocketdemoserver.model.game;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class Gameboard {
    private Feld[] felder;
    private int carrotCounter;
    SecureRandom random = new SecureRandom();

    public Gameboard(int players) {
        this.felder = new Feld[26]; //26 felder inkl. karotte
        this.carrotCounter = new Random().nextInt(12);
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

    public Feld[] getFelder() {
        return felder;
    }

    public void setFelder(Feld[] felder) {
        this.felder = felder;
    }

    public int getCarrotCounter() {
        return carrotCounter;
    }

    public void setCarrotCounter(int carrotCounter) {
        this.carrotCounter = carrotCounter;
    }
}
