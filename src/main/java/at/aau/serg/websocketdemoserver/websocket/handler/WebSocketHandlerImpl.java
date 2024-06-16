package at.aau.serg.websocketdemoserver.websocket.handler;

import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;
import at.aau.serg.websocketdemoserver.model.raum.TestRoomInit;
import at.aau.serg.websocketdemoserver.msg.*;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;

import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerHeartbeat;
import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerTestMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerChatMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerDrawCard;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerJoinRoom;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerOpenRoom;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;
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

    private final Gson gson = new Gson();
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Logger logger = Logger.getLogger(getClass().getName());

    static long counter = 0; // wird nur für die initialisierung der testRooms verwendet

    //broadcasting

    /*public void broadcastMsg(String message, WebSocketSession sender) throws Exception {
        if (message == null) {
            logger.warning("error: message to broadcast was null");
            //assert (false);
            //sollte mit logger ausgetauscht werden
            return;
        }

        Set<WebSocketSession> sessionsToBroadcast = new HashSet<>(sessions);
        sessionsToBroadcast.remove(sender);

        for (WebSocketSession client : sessionsToBroadcast) {
            if (client.isOpen()) {
                client.sendMessage(new TextMessage(message));
            } else {
                logger.info("Session {} is closed, skipping message send");
            }
        }
    }*/


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            sessions.add(session);
        }
        catch (Exception e) {
            throw new Exception (e);
        }

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (counter == 0) {
            //initTestRooms(); //for checking if list is working and default room till that time
            TestRoomInit.initTestRooms(roomRepo);
            counter++;
        }
        logger.info("reached point handleMessage");
        // TODO handle the messages here
        //session.sendMessage(new TextMessage("echo from handler: " + message.getPayload()));

        String payload = (String) message.getPayload();
        logger.info("from client" + payload);

        handleMessageByType(session, payload);
    }

    private void handleMessageByType(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        BaseMessage baseMessage = gson.fromJson(payload, BaseMessage.class);

        if (baseMessage != null) {
            MessageType messageType = baseMessage.getMessageType();
            switch (messageType) {
                //testMsg at startup
                case TEST -> HandlerTestMessage.handleTestMessage(session, payload);
                //msg for constant load on network (ping - pong - ping - ...)
                case HEARTBEAT -> HandlerHeartbeat.handleHeartbeat(session, payload);
                //messages for opening, joining and listing rooms
                //case OPEN_ROOM -> handleOpenRoomMessage(session, payload);
                case OPEN_ROOM -> HandlerOpenRoom.handleOpenRoomMessage(session, payload);
                case JOIN_ROOM -> HandlerJoinRoom.handleAskForJoinRoom(session, payload, roomRepo);
                case LIST_ROOMS -> handleAskForRoomList(session, payload);
                //messages for gameboard logic
                case GAMEBOARD -> handleGameBoardMessage(session, payload);
                //case CHAT -> handleChatMessage(session, payload);
                case CHAT -> HandlerChatMessage.handleChatMessage(session, payload, sessions);
                //case DRAW_CARD -> handleDrawCard(session, payload);
                case DRAW_CARD -> HandlerDrawCard.handleDrawCard(session, payload, sessions, roomRepo);
                //def.
                default -> logger.info("unknown message type received");
            }
        }
    }

    public void handleGameBoardMessage(WebSocketSession session, String payload) throws Exception {
        //TODO: Task Flo...
        //TODO: at the moment empty class, that no error occur
    }


    private void handleGuessCheater(WebSocketSession session, String payload) throws Exception {
        //Gson gson = new Gson();
        //RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);
    }


    private void handleAskForRoomList(WebSocketSession session, String payload) throws Exception {
        Logger logger = Logger.getLogger(getClass().getName());
        //Gson gson = new Gson();
        logger.info("ask for room list reached");
        //RoomListMessage roomListMessage = gson.fromJson(payload, RoomListMessage.class);

        //get repo List
        ArrayList<Room> roomList = roomRepo.listAllRooms();

        //arrayList for request
        ArrayList<RoomInfo> roomInfoList = new ArrayList<>();

        for (Room room : roomList) {
            //nur räume, welche mehr oder gleich einen platz haben, welcher noch unbesetzt ist...
            //es macht keinen sinn volle räume zu übermitteln...
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
        String responsePayload = gson.toJson(response);
        session.sendMessage(new TextMessage(responsePayload));

        logger.info("###");
        logger.info(responsePayload);
        logger.info("###");

    }



    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        //sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}

