package at.aau.serg.websocketdemoserver.msg;

public class TestMessage extends BaseMessage {

    private String text;

    private String messageIdentifier;

    public TestMessage() {
        //default
        this.messageType = MessageType.TEST;
    }


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


}
