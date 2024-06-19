package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Field;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GameMessage implements BaseMessageImpl{
    MessageType messageType;

    @JsonProperty("fields")
    Field fields[];

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.GAME;
    }
}

