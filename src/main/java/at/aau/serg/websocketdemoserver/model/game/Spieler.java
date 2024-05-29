package at.aau.serg.websocketdemoserver.model.game;

import java.util.ArrayList;
import java.util.UUID;

public class Spieler {
    private String spielerID;
    private String name;
    private Farbe farbe;
    // FIXME use interface instead of implementation
    private ArrayList<Spielfigur> unusedBunnies;
    private boolean isCheater;

    public Spieler() {
        //default
    }

    public Spieler (String name) {
        this.spielerID = UUID.randomUUID().toString();
        this.name = name;
        this.unusedBunnies = new ArrayList<>();
        this.isCheater = false; //default
    }

    public enum Farbe {
        ROT, GRUEN, BLAU, SCHWARZ, GELB
    }

    public String getSpielerID() {
        return spielerID;
    }

    /*public void setSpielerID(String spielerID) {
        this.spielerID = spielerID;
    }*/
    /*public void setSpielerID() {
        this.spielerID = UUID.randomUUID().toString();
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Farbe getFarbe() {
        return farbe;
    }

    public void setFarbe(Farbe farbe) {
        this.farbe = farbe;
    }

    public ArrayList<Spielfigur> getUnusedBunnies() {
        return unusedBunnies;
    }

    public void setUnusedBunnies(ArrayList<Spielfigur> unusedBunnies) {
        this.unusedBunnies = unusedBunnies;
    }

    public boolean isCheater() {
        return isCheater;
    }

    public void setCheater(boolean cheater) {
        isCheater = cheater;
    }
}
