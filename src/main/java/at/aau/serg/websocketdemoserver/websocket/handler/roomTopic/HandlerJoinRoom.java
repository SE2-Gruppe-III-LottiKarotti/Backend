package at.aau.serg.websocketdemoserver.websocket.handler.roomTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.JoinRoomMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import org.springframework.web.socket.WebSocketSession;

import java.util.logging.Logger;

public class HandlerJoinRoom {

    private static final Logger logger = Logger.getLogger(HandlerJoinRoom.class.getName());

    public static void handleAskForJoinRoom(WebSocketSession session, String payload, InMemoryRoomRepo roomRepo)  {

        //1 extract
        TransportUtils.validateSessionAndPayload(session, payload);


        JoinRoomMessage joinRoomMessage = TransportUtils.helpFromJson(payload, JoinRoomMessage.class); //gson.fromJson(payload, JoinRoomMessage.class);

        //2 set local variables
        String roomName = joinRoomMessage.getRoomName();
        String playerName = joinRoomMessage.getPlayerName();

        //3 check
        if (roomName == null || roomName.isEmpty() || playerName == null || playerName.isEmpty()) {
            //fehlerhafte Werte
            joinRoomMessage.setActionTypeJoinRoom(JoinRoomMessage.ActionTypeJoinRoom.JOIN_ROOM_ERR);
            String errorPayload = TransportUtils.helpToJson(joinRoomMessage);
            TransportUtils.sendMsg(session, errorPayload);
            return;
        }

        //TODO: check FIXME -> maybe need to be syncronised

        //Room foundRoom = WebSocketHandlerImpl.getRoomRepo().findRoomByName(roomName);
        Room foundRoom = roomRepo.findRoomByName(roomName);



        // if room exists, add player to room
        if (foundRoom == null) {
            logger.info("foundRoom == null");
            // raum existiert nicht
            joinRoomMessage.setActionTypeJoinRoom(JoinRoomMessage.ActionTypeJoinRoom.JOIN_ROOM_ERR);
            String errorPayload = TransportUtils.helpToJson(joinRoomMessage);
            TransportUtils.sendMsg(session, errorPayload);
            logger.info("toClient: " + errorPayload);
            return;

        }

        logger.info("foundRoom != null");

        //valid, now add player
        Player playerToAdd = new Player(playerName);
        foundRoom.addPlayer(playerToAdd);

        //msg set
        joinRoomMessage.setActionTypeJoinRoom(JoinRoomMessage.ActionTypeJoinRoom.JOIN_ROOM_OK);
        joinRoomMessage.setRoomId(foundRoom.getRoomID());
        joinRoomMessage.setPlayerId(playerToAdd.getPlayerID());
        joinRoomMessage.setPlayerName(playerToAdd.getName());
        //msg send
        String positivePayload = TransportUtils.helpToJson(joinRoomMessage);
        TransportUtils.sendMsg(session, positivePayload);
        logger.info("toClient: " + positivePayload);



    }



}
