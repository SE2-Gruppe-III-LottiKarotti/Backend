package at.aau.serg.websocketdemoserver.websocket.handler.roomTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.OpenRoomMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.WebSocketHandlerImpl;
import org.springframework.web.socket.WebSocketSession;

import java.util.logging.Logger;

public class HandlerOpenRoom {

    private static final Logger logger = Logger.getLogger(HandlerOpenRoom.class.getName());

    public static void handleOpenRoomMessage(WebSocketSession session, String payload, InMemoryRoomRepo roomRepo)  {
        //1 extract
        TransportUtils.validateSessionAndPayload(session, payload);
        OpenRoomMessage openRoomMessage = TransportUtils.helpFromJson(payload, OpenRoomMessage.class);

        //2 set local variables
        String roomName = openRoomMessage.getRoomName();
        String playerName = openRoomMessage.getPlayerName();
        int maxPlayers = Integer.parseInt(openRoomMessage.getNumPlayers());

        //3_1 checks1
        if (roomName == null || roomName.isEmpty() || playerName == null || playerName.isEmpty() || maxPlayers < 2 || maxPlayers > 4) {
            //error values
            openRoomMessage.setOpenRoomActionType(OpenRoomMessage.OpenRoomActionType.OPEN_ROOM_ERR);
            String errorPayload = TransportUtils.helpToJson(openRoomMessage);
            TransportUtils.sendMsg(session, errorPayload);
            return;
        }

        //3_2 check2
        Room foundRoom = WebSocketHandlerImpl.getRoomRepo().findRoomByName(roomName);

        //4 checks → if the room already exists...
        if (foundRoom != null) {
            logger.info(foundRoom.toString());
            // room already exists
            // 4_1 send...
            if (foundRoom.getRoomName().equals(openRoomMessage.getRoomName())) {
                openRoomMessage.setOpenRoomActionType(OpenRoomMessage.OpenRoomActionType.OPEN_ROOM_ERR);
                String errorPayload = TransportUtils.helpToJson(openRoomMessage);
                TransportUtils.sendMsg(session, errorPayload);
                logger.info("toClient: " + errorPayload);
                return;

            }

        }
        logger.info("found room == null");

        //5 positive case --> the room will be created
        Room roomToAdd = new Room(maxPlayers, roomName);
        //create player
        Player creator = new Player(playerName);
        String creatorId = creator.getPlayerID();
        roomToAdd.addPlayer(creator);
        roomToAdd.setCreatorName(playerName);
        logger.info("after adding player " + roomToAdd.getAvailablePlayersSpace());

        //5_1 add the created room to the repo
        //WebSocketHandlerImpl.getRoomRepo().addRoom(roomToAdd);
        roomRepo.addRoom(roomToAdd);

        //5_2 message vorbereiten und senden --> serialisieren
        //now prepare message
        openRoomMessage.setOpenRoomActionType(OpenRoomMessage.OpenRoomActionType.OPEN_ROOM_OK);
        openRoomMessage.setRoomId(roomToAdd.getRoomID());
        openRoomMessage.setRoomName(roomToAdd.getRoomName());
        openRoomMessage.setPlayerId(creatorId);
        openRoomMessage.setPlayerName(creator.getName());
        openRoomMessage.setNumPlayers(Integer.toString(maxPlayers));

        //openRoomMessage.setMessageIdentifier(messageIdentifier);



        //5_3 hier wird schlussendlich die positive nachricht versendet...
        //prepare positive payload
        String positivePayload = TransportUtils.helpToJson(openRoomMessage);
        TransportUtils.sendMsg(session, positivePayload);
        logger.info("end of player hinzugefügt");
        logger.info("toClient: " + positivePayload);



    }
}
