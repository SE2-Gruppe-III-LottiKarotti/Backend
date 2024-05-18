package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;

import java.util.ArrayList;

public class RoomListMessage {
    ArrayList<RoomInfo> roomInfoArrayList;
    enum ActionTypeRoomListMessage {
        ASK_FOR_ROOM_LIST,
        ANSWER_ROOM_LIST_OK,
        ANSWER_ROOM_LISR_ERR
    }
}

