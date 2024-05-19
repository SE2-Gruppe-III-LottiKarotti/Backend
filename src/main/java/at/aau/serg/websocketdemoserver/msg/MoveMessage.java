package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class MoveMessage {
    private final MessageType messageType = MessageType.MOVE;

    String roomId;
    String spielerId;
    String card;
}
