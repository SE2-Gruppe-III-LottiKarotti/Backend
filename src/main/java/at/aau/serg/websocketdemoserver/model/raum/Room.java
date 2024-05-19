package at.aau.serg.websocketdemoserver.model.raum;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Spieler;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class Room {
    private String roomID;
    private String roomName;
    private ArrayList<Spieler> listOfPlayers;
    private int maxPlayers;
    private int availablePlayersSpace;
    private Gameboard gameboard;
    private Spieler currentPlayer; // oder spielerID ...

    private ArrayList<String> cheaters;
    private String creatorName;

    //TODO: vll hier drinnen auch eine ArrayList mit Farben ablegen...
    /***/


    public Room(int maxPlayers, String roomName) {
        this.roomID = UUID.randomUUID().toString();
        this.maxPlayers = maxPlayers;
        this.availablePlayersSpace = maxPlayers;
        this.listOfPlayers = new ArrayList<>();
        this.cheaters = new ArrayList<>();
//TODO: vll auch das gameboard hier initialisieren...
    }


    public Spieler getPlayerById(String spielerID) {
        for (Spieler spieler : listOfPlayers) {
            if (spieler.getSpielerID().equals(spielerID)) {
                return spieler;
            }
        }
        return null;
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

    public void addPlayerToCheatList(String playerId) {
        if (!cheaters.contains(playerId)) {
            cheaters.add(playerId);
        }
    }

    public void deletePlayerFromCheatList(String playerId) {
        if (cheaters.contains(playerId)) {
            cheaters.remove(playerId);
        }
    }


}
