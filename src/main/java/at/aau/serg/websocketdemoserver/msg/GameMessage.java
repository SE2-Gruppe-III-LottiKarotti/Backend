package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Field;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GameMessage extends BaseMessage {

    public GameMessage() {
        //default
        this.messageType = MessageType.GAME;
    }

    @JsonProperty("fields")
    Field fields[];
    String playerId;
    String roomId;
    String[] playerNames;
}

