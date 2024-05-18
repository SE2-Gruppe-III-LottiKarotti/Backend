package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class OpenRoomMessage {
    String roomId;
    String roomName;
    String playerId;
    String playerName;
    OpenRoomMessage.ActionTypeOpenOrJoinRoom actionTypeOpenOrJoinRoom;

    public enum ActionTypeOpenOrJoinRoom {
        /**open room*/
        OPEN_ROOM_ASK,
        OPEN_ROOM_OK,
        OPEN_ROOM_ERR,

    }
}
