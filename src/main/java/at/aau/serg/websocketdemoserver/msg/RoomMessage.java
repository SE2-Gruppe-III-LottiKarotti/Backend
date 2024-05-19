package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Spieler;
import lombok.Data;

import java.util.ArrayList;

@Data
public class RoomMessage {

   // private final MessageType messageType = MessageType.GAMEBOARD;
    private RoomMessage.ActionType actionType;

    private String roomID;
    private String roomName;
    private ArrayList<Spieler> listOfPlayers;
    private int maxPlayers;
    private Gameboard gameboard;
    private Spieler currentPlayer; // oder spielerID ...
    private Spieler nextPlayer;
    private Spieler addPlayer;
    private String randomCart;
    private int playerIndex;

    public RoomMessage() {
        //default
    }

    public enum ActionType {
        OPENROOM, JOINROOM, GAMEPLAY, DRAWCARD, CHAT, SETUPFIELD, GUESSCHEATER, NEXTPlAYER,
    }
}
