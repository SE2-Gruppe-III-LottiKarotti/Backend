package at.aau.serg.websocketdemoserver.msg;

public class ChatMessage {
    String sender;
    String text;
    String roomID;
    String msgIdentifier;

    public ChatMessage(String sender, String text, String roomID, String msgIdentifier) {
        this.sender = sender;
        this.text = text;
        this.roomID = roomID;
        this.msgIdentifier = msgIdentifier;
    }

    public ChatMessage() {
        //default
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getMsgIdentifier() {
        return msgIdentifier;
    }

    public void setMsgIdentifier(String msgIdentifier) {
        this.msgIdentifier = msgIdentifier;
    }
}