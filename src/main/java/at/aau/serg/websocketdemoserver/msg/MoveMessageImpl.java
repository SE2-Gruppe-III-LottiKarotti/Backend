package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Field;
import at.aau.serg.websocketdemoserver.model.game.PlayingPiece;
import lombok.Data;

@Data
public class MoveMessageImpl implements BaseMessageImpl {
    MessageType messageType;

    String roomId;
    String spielerId;
    String card;
    Field[] fields;
    PlayingPiece playingPiece;

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.MOVE;
    }
}
