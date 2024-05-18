package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class GuessCheaterMessage {
    String accusingPlayerId;
    String accusingPlayerName;
    String playerToBlameName;
    String playerToBlameId;
    String roomId;

    /*message need to be broadcasted*/
}
