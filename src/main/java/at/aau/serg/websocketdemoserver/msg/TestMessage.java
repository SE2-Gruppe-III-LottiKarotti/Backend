package at.aau.serg.websocketdemoserver.msg;

public class TestMessage implements BaseMessageImpl {

    MessageType messageType;
    String text;

    String messageIdentifier;


    public String getMessageIdentifier() {
        return messageIdentifier;
    }

    public void setMessageIdentifier(String messageIdentifier) {
        this.messageIdentifier = messageIdentifier;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.TEST;
    }
}
