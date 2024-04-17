package at.aau.serg.websocketdemoserver.model.game;

import java.util.Random;

public class Gameboard {
    private Feld[] felder;
    private int carrotCounter;

    public Gameboard() {
        this.felder = new Feld[26]; //26 felder inkl. karotte
        this.carrotCounter = new Random().nextInt(12);
        initFields();
        //initFigures(maxPlayers);

    }

    //initFields
    private void initFields() {
        //
        //TODO: maulwurfhügel initiieren --> diese per schleife

    }

    private void initSpecialFields() {
        //TODO: init spezielle Felder --> gatter, maulwurf und brücke initialisieren --> diese hardcoded
    }

    private void initFiguren() {
        //TODO: zu jedem Spieler 4 figuren in der richtigen Farbe hinzufügen...
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
