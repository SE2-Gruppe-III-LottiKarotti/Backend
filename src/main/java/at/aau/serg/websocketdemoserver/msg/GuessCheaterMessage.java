package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class GuessCheaterMessage {

    private final MessageType messageType = MessageType.CHEAT;
    //der spieler, welcher beschuldigt
    String accusingPlayerId;
    String accusingPlayerName;
    //der spieler, welcher beschuldigt wird
    String playerToBlameName;
    String playerToBlameId;
    String roomId;

    /*message need to be broadcasted*/
}
