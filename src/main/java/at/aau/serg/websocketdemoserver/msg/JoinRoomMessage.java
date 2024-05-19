package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class JoinRoomMessage {
    private final MessageType messageType = MessageType.JOIN_ROOM;

    String roomId;
    String roomName;
    String playerId;
    String playerName;
    ActionTypeJoinRoom actionTypeJoinRoom;

    public enum ActionTypeJoinRoom {
        /**join room*/
        JOIN_ROOM_ASK,
        JOIN_ROOM_OK,
        JOIN_ROOM_FULL,
        JOIN_ROOM_ERR
    }

}
