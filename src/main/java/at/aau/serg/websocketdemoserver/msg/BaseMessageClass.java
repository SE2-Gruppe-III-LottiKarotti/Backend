package at.aau.serg.websocketdemoserver.msg;

public class BaseMessageClass implements BaseMessageImpl{

    private MessageType messageType;

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
