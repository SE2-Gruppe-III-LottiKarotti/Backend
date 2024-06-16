package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class JoinRoomMessageImpl implements BaseMessageImpl {
    //private final MessageType messageType = MessageType.JOIN_ROOM;
    MessageType messageType;

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

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.JOIN_ROOM;
    }

}
