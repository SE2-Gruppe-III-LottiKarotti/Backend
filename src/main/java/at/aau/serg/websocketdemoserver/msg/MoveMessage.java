package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Field;
import at.aau.serg.websocketdemoserver.model.game.PlayingPiece;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MoveMessage extends BaseMessage {

    private String roomId;
    private String spielerId;
    private String card;
    private Field[] fields;
    private PlayingPiece playingPiece;

    public MoveMessage () {
        //default
        this.messageType = MessageType.MOVE;
    }

}
