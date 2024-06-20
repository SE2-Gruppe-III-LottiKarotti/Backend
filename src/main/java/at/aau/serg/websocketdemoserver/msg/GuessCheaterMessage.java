package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class GuessCheaterMessage implements BaseMessageImpl {

    MessageType messageType;
    //the player who blame the other player to be a cheater (blamer)
    String accusingPlayerId;
    String accusingPlayerName;
    //the player who should be the cheater (cheater)
    String playerToBlameName;
    String playerToBlameId;
    String roomId;

    /*message need to be broadcasted*/
    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.CHEAT;
    }
}
