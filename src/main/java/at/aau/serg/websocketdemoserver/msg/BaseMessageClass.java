package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

public class BaseMessageClass implements BaseMessageImpl{

    private MessageType messageType;
    @Data
    public class BaseMessage {

    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }
}
