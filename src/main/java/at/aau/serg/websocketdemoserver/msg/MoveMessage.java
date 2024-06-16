package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class MoveMessage implements BaseMessageImpl {
    //private final MessageType messageType = MessageType.MOVE;
    MessageType messageType;

    String roomId;
    String spielerId;
    String card;

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.MOVE;
    }
}
