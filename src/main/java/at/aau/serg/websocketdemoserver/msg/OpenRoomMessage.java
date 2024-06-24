package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenRoomMessage extends BaseMessage {

    private String roomId;
    private String roomName;
    private String playerId;
    private String playerName;
    private String numPlayers;
    private OpenRoomMessage.OpenRoomActionType openRoomActionType;

    public OpenRoomMessage () {
        //default
        this.messageType = MessageType.OPEN_ROOM;
    }

    public enum OpenRoomActionType {
        /**open room*/
        OPEN_ROOM_ASK,
        OPEN_ROOM_OK,
        OPEN_ROOM_ERR,

    }

}
