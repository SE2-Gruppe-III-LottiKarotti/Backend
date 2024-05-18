package at.aau.serg.websocketdemoserver.msg;

public class ChatMessage {
    String playerName;
    String playerId;
    String text;
    String roomID;

    /*message need to be broadcasted*/


    //String msgIdentifier; //da der socket eh bekannt ist

    public ChatMessage(String playerName, String playerId, String text, String roomID) {
        this.playerName = playerName;
        this.playerId = playerId;
        this.text = text;
        this.roomID = roomID;

    }

    public ChatMessage() {
        //default
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }


}