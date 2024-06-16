package at.aau.serg.websocketdemoserver.msg;

public class HeartbeatMessageImpl implements BaseMessageImpl {
    //private final MessageType messageType = MessageType.HEARTBEAT;
    MessageType messageType;
    private String text;// = "pong";


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.HEARTBEAT;
    }

}
