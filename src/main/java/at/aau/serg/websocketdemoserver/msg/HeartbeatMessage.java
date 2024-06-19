package at.aau.serg.websocketdemoserver.msg;

public class HeartbeatMessage implements BaseMessageImpl {
    MessageType messageType;
    private String text;// = "ping/pong";


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
