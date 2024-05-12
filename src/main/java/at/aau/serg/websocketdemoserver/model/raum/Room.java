package at.aau.serg.websocketdemoserver.model.raum;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Spieler;

import java.util.ArrayList;
import java.util.UUID;

public class Room {
    private String roomID;
    private String roomName;
    private ArrayList<Spieler> listOfPlayers;
    private int maxPlayers;
    private int availablePlayersSpace;
    private Gameboard gameboard;
    private Spieler currentPlayer; // oder spielerID ...

    //TODO: vll hier drinnen auch eine ArrayList mit Farben ablegen...
    /***/

    public Room(int maxPlayers, String roomName) {
        this.roomName = roomName;
        this.roomID = UUID.randomUUID().toString();
        this.maxPlayers = maxPlayers;
        this.availablePlayersSpace = maxPlayers;
        this.listOfPlayers = new ArrayList<>();
        //TODO: vll auch das gameboard hier initialisieren...
    }

    public int getAvailablePlayersSpace() {
        return availablePlayersSpace;
    }

    public void setAvailabePlayersSpace(int availabePlayersSpace) {
        this.availablePlayersSpace = availabePlayersSpace;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<Spieler> getListOfPlayers() {
        return listOfPlayers;
    }

    public void setListOfPlayers(ArrayList<Spieler> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Gameboard getGameboard() {
        return gameboard;
    }

    public void setGameboard(Gameboard gameboard) {
        this.gameboard = gameboard;
    }

    public Spieler getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Spieler currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void addPlayer(Spieler spieler) {
        if (availablePlayersSpace > 0) {
            listOfPlayers.add(spieler);
            availablePlayersSpace--;
        } else {
            System.out.println("raum voll");
            //der punkt sollte eigentlich nie erreicht werden
        }
    }
}
