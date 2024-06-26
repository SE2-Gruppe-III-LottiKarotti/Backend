package at.aau.serg.websocketdemoserver.websocket.handler.roomTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;
import at.aau.serg.websocketdemoserver.msg.RoomListMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.logging.Logger;

public class HandlerRoomList {

    private static final Logger logger = Logger.getLogger(HandlerRoomList.class.getName());

    public static void handleAskForRoomList(WebSocketSession session, String payload, InMemoryRoomRepo roomRepo)  {

        logger.info("ask for room list reached");
        TransportUtils.validateSessionAndPayload(session, payload);
        RoomListMessage roomListMessage = TransportUtils.helpFromJson(payload, RoomListMessage.class);

        if (roomListMessage == null) {
            RoomListMessage response = new RoomListMessage();
            response.setActionTypeRoomListMessage(RoomListMessage.ActionTypeRoomListMessage.ANSWER_ROOM_LIST_ERR);

            //prepare and send
            String responseErrorPayload = TransportUtils.helpToJson(response);
            TransportUtils.sendMsg(session, responseErrorPayload);
        }

        //get repo List
        ArrayList<Room> roomList = roomRepo.listAllRooms();

        //arrayList for request
        ArrayList<RoomInfo> roomInfoList = new ArrayList<>();

        for (Room room : roomList) {
            //just rooms, where still places are available
            //no sense to transfer full rooms
            if (room.getAvailablePlayersSpace() >= 1) {
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.setRoomID(room.getRoomID());
                roomInfo.setRoomName(room.getRoomName());
                roomInfo.setCreator(room.getCreatorName());
                roomInfo.setAvailablePlayersSpace(room.getAvailablePlayersSpace());
                roomInfoList.add(roomInfo);
                logger.info(roomInfo + " " + roomInfo.getRoomName() + " " + roomInfo.getCreator() + " " + roomInfo.getAvailablePlayersSpace());
            }

        }
        logger.info("###");
        logger.info(String.valueOf(roomInfoList));
        logger.info("###");

        //msg
        RoomListMessage response = new RoomListMessage();
        response.setActionTypeRoomListMessage(RoomListMessage.ActionTypeRoomListMessage.ANSWER_ROOM_LIST_OK);
        response.setRoomInfoArrayList(roomInfoList);

        //prepare and send
        String responsePayload = TransportUtils.helpToJson(response);
        TransportUtils.sendMsg(session, responsePayload);

        logger.info("###");
        logger.info(responsePayload);
        logger.info("###");

    }
}
