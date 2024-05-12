package at.aau.serg.websocketdemoserver.msg;

import java.util.ArrayList;


import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;

public class RoomSetupMessage {

    MessageType messageType = MessageType.SETUP_ROOM;

    ActionType actionType;

    String messageIdentifier;
    String roomID;
    String roomName;
    String playerID;
    String playerName;
    String numPlayers;
    ArrayList<RoomInfo> roomInfoList;


    public RoomSetupMessage() {
        //default constructor
    }

    public RoomSetupMessage(String roomName, String roomID, String playerName, String playerID, String numPlayers, String messageIdentifier, ActionType actionType) {
        this.roomName = roomName;
        this.roomID = roomID;
        this.playerName = playerName;
        this.playerID = playerID;
        this.numPlayers = numPlayers;
        this.messageIdentifier = messageIdentifier;
        this.actionType = actionType;
    }


    /*public RoomSetupMessage() {
        //default constructor
    }

    public RoomSetupMessage(String roomName, String numPlayers, String playerName) {
        this.roomName = roomName;
        this.numPlayers = numPlayers;
        this.playerName = playerName;
    }*/



    /**um eine Nachricht definitiv von allen anderen zu unterscheiden
     * wird im client eine UUID für die Nachricht generiert und beim absenden
     * mitgesendet...
     * der client merkt sich die UUID als lokale varialbe und wenn
     * der client die antwort vom Server bekommt, wird überprüft, ob
     * diese nachricht für den client bestimmt war oder nicht...*/


    public String getMessageIdentifier() {
        return messageIdentifier;
    }

    public void setMessageIdentifier(String messageIdentifier) {
        this.messageIdentifier = messageIdentifier;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public enum ActionType {
        OPEN_ROOM, OPEN_ROOM_OK, OPEN_ROOM_ERR, ROOM_FULL, ASK_FOR_ROOM_LIST, ASK_FOR_JOIN_ROOM, ANSWER_ROOM_LIST, ANSWER_JOIN_ROOM_OK, ANSWER_JOIN_ROOM_ERR
    }


    public String getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(String numPlayers) {
        this.numPlayers = numPlayers;
    }

    public MessageType getMessageType() {
        return messageType;
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

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ArrayList<RoomInfo> getRoomInfoList() {
        return roomInfoList;
    }

    public void setRoomInfoList(ArrayList<RoomInfo> roomInfoList) {
        this.roomInfoList = roomInfoList;
    }

}