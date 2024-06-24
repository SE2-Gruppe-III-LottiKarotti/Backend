package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Field;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GuessCheaterMessage extends BaseMessage {

    private String accusingPlayerId;
    private String playerToBlameName;
    private String playerToBlameId;
    private String roomId;
    private String cheater;
    private Field[] fields;

    public GuessCheaterMessage () {
        //default
        this.messageType = MessageType.CHEAT;
    }
}
