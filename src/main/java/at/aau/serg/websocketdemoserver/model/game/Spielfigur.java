package at.aau.serg.websocketdemoserver.model.game;

public class Spielfigur {
    private int spielfigur;
    private Spieler spieler;
    private int position;

    public Spielfigur() {
    }

    public Spielfigur(int spielfigur, Spieler spieler) {
        this.spielfigur = spielfigur;
        this.spieler = spieler;
        this.position = 0;
    }

    public int getSpielfigur() {
        return spielfigur;
    }

    public void setSpielfigur(int spielfigur) {
        this.spielfigur = spielfigur;
    }

    public Spieler getSpieler() {
        return spieler;
    }

    public void setSpieler(Spieler spieler) {
        this.spieler = spieler;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}