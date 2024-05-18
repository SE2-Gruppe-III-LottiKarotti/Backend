package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class JoinRoomMessage {
    String roomId;
    String roomName;
    String playerId;
    String playerName;
    ActionTypeOpenOrJoinRoom actionTypeOpenOrJoinRoom;

    public enum ActionTypeOpenOrJoinRoom {
        /**join room*/
        JOIN_ROOM_ASK,
        JOIN_ROOM_OK,
        JOIN_ROOM_FULL,
        JOIN_ROOM_ERR
    }

}
