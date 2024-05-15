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

    private String creatorName;

    //TODO: vll hier drinnen auch eine ArrayList mit Farben ablegen...
    /***/

    public Room() {
        this.roomID = UUID.randomUUID().toString();
        this.listOfPlayers = new ArrayList<>();
        //TODO: vll auch das gameboard hier initialisieren...
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
