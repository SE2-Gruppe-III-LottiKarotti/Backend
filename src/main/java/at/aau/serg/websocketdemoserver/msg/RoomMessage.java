package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Spieler;

import java.util.ArrayList;

public class RoomMessage {

    private final MessageType messageType = MessageType.GAMEBOARD;
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

    public Spieler getAddPlayer() {
        return addPlayer;
    }

    public void setAddPlayer(Spieler addPlayer) {
        this.addPlayer = addPlayer;
    }

    public RoomMessage.ActionType getActionType() {
        return actionType;
    }

    public void setActionType(RoomMessage.ActionType actionType) {
        this.actionType = actionType;
    }

    public String getRandomCart() {
        return randomCart;
    }

    public void setRandomCart(String randomCart) {
        this.randomCart = randomCart;
    }

    public Spieler getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Spieler nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public enum ActionType {
        OPENROOM, JOINROOM, GAMEPLAY, DRAWCARD, CHAT, SETUPFIELD, GUESSCHEATER, NEXTPlAYER,
    }
}
