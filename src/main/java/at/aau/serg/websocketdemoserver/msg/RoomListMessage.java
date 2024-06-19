package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;
import lombok.Data;

import java.util.ArrayList;

@Data
public class RoomListMessage implements BaseMessageImpl {
    MessageType messageType;

    ArrayList<RoomInfo> roomInfoArrayList;
    ActionTypeRoomListMessage actionTypeRoomListMessage;
    public enum ActionTypeRoomListMessage {
        ASK_FOR_ROOM_LIST,
        ANSWER_ROOM_LIST_OK,
        ANSWER_ROOM_LIST_ERR
    }

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.LIST_ROOMS;
    }
}

