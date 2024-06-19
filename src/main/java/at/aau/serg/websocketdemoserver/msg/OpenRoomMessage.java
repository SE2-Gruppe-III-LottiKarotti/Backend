package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class OpenRoomMessage implements BaseMessageImpl {
    MessageType messageType;
    String roomId;
    String roomName;
    String playerId;
    String playerName;
    String numPlayers;
    OpenRoomMessage.OpenRoomActionType openRoomActionType;

    public enum OpenRoomActionType {
        /**open room*/
        OPEN_ROOM_ASK,
        OPEN_ROOM_OK,
        OPEN_ROOM_ERR,

    }

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.OPEN_ROOM;
    }
}
