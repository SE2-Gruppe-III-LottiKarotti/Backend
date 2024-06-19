package at.aau.serg.websocketdemoserver.websocket.handler;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.raum.TestRoomInit;
import at.aau.serg.websocketdemoserver.msg.*;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;

import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerHeartbeat;
import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerTestMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerChatMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerDrawCard;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerGameMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerMoveMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerJoinRoom;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerOpenRoom;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerRoomList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@Component
public class WebSocketHandlerImpl implements WebSocketHandler {

    @Autowired
    private static final InMemoryRoomRepo roomRepo = new InMemoryRoomRepo();

    public static InMemoryRoomRepo getRoomRepo () {
        return roomRepo;
    }

    private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Logger logger = Logger.getLogger(getClass().getName());

    static long counter = 0; // this variable is used to init the default testing rooms

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            sessions.add(session);
        }
        catch (Exception e) {
            throw new Exception ("error when establishing connection", e);
        }

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (counter == 0) {

            TestRoomInit.initTestRooms(roomRepo);
            counter++;
        }
        logger.info("reached point handleMessage");
        // TODO handle the messages here

        String payload = (String) message.getPayload();
        logger.info("from client" + payload);

        handleMessageByType(session, payload);
    }

    private void handleMessageByType(WebSocketSession session, String payload) throws Exception {
        BaseMessageClass baseMessageClass = TransportUtils.helpFromJson(payload, BaseMessageClass.class);

        if (baseMessageClass != null) {
            MessageType messageType = baseMessageClass.getMessageType();
            switch (messageType) {
                //testMsg at startup
                case TEST -> HandlerTestMessage.handleTestMessage(session, payload);
                //msg for constant load on network (ping - pong - ping - ...)
                case HEARTBEAT -> HandlerHeartbeat.handleHeartbeat(session, payload);
                //messages for opening, joining and listing rooms
                case OPEN_ROOM -> HandlerOpenRoom.handleOpenRoomMessage(session, payload, roomRepo);
                case JOIN_ROOM -> HandlerJoinRoom.handleAskForJoinRoom(session, payload, roomRepo);
                case LIST_ROOMS -> HandlerRoomList.handleAskForRoomList(session, payload, roomRepo);
                //messages for gameplay
                case CHAT -> HandlerChatMessage.handleChatMessage(session, payload, sessions);
                case DRAW_CARD -> HandlerDrawCard.handleDrawCard(session, payload, sessions, roomRepo);
                case GAME -> HandlerGameMessage.handleGameMessage(session, payload, sessions, roomRepo);
                case MOVE -> HandlerMoveMessage.handleMoveMessage(session, payload, sessions, roomRepo);
                default -> logger.info("unknown message type received");
            }
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        try {
            sessions.remove(session);
        }
        catch (Exception e) {
            throw new Exception ("error after closing connection",e);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}

