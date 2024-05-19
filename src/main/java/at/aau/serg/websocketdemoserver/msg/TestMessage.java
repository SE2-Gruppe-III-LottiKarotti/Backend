package at.aau.serg.websocketdemoserver.msg;

public class TestMessage {
    //private final MessageType messageType = MessageType.TEST;
    //private MessageType messageType;// = MessageType.TEST;
    MessageType messageType;
    String text;

    String messageIdentifier;


    //private String text;

    /*public TestMessage() {
    }*/

    /*public TestMessage(String text) {
        this.text = text;
    }*/
    public String getMessageIdentifier() {
        return messageIdentifier;
    }

    public void setMessageIdentifier(String messageIdentifier) {
        this.messageIdentifier = messageIdentifier;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
