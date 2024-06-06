package at.aau.serg.websocketdemoserver.model.game;

import java.util.ArrayList;
import java.util.UUID;

public class Player {
    private String playerID;
    private String name;
    private Colour colour;
    private ArrayList<PlayingPiece> unusedBunnies;
    private boolean isCheater;
    private boolean reachedCarrot;

    public Player(String name) {
        this.playerID = UUID.randomUUID().toString();
        this.name = name;
        this.unusedBunnies = new ArrayList<>();
        this.isCheater = false; //default
        this.reachedCarrot = false; // Zu Beginn hat kein Spieler das letzte Feld erreicht
    }

    public enum Colour {
        RED, GREEN, BLUE, BLACK, YELLOW
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Colour getColour() {
        return colour;
    }

    public void setFarbe(Colour colour) {
        this.colour = colour;
    }

    public boolean isCheater() {
        return isCheater;
    }

    public void setCheater(boolean cheater) {
        isCheater = cheater;
    }

    public boolean hasReachedCarrot() {
        return reachedCarrot;
    }

    public void setReachedCarrot(boolean reachedCarrot) {
        this.reachedCarrot = reachedCarrot;
    }
}
