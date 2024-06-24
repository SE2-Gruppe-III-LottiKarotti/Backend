package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class BaseMessage implements BaseMessageInterface{

    protected MessageType messageType;

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

}
