package at.aau.serg.websocketdemoserver.websocket.handler;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.logic.CensorMessageFunction;
import at.aau.serg.websocketdemoserver.model.logic.RandomCardGenerator;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;
import at.aau.serg.websocketdemoserver.msg.*;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;

import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerHeartbeat;
import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerTestMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerDrawCard;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerJoinRoom;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerOpenRoom;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public void broadcastMsg(String message, WebSocketSession sender) throws Exception {
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
    }

    private void initTestRooms() {
        String playerName = "FranzSissi";
        String player2 = "Daniel";
        Player player1 = new Player(playerName);
        Player spieler2 = new Player(player2);
        Room testRoom1 = new Room(2, "TestRoom");
        testRoom1.setCreatorName(playerName);
        testRoom1.addPlayer(player1);
        Room testRoom2 = new Room(3, "TestRo2");
        testRoom2.setCreatorName(playerName);
        testRoom2.addPlayer(player1);
        Room testRoom3 = new Room(2, "TestRo3");
        testRoom3.setCreatorName(playerName);
        testRoom3.addPlayer(player1);
        testRoom3.addPlayer(spieler2);
        //testRoom3 dient dem Test, ob ein voller Raum übermittelt wird oder nicht...


        //add
        roomRepo.addRoom(testRoom1);
        roomRepo.addRoom(testRoom2);
        roomRepo.addRoom(testRoom3);
    }

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
            initTestRooms(); //for checking if list is working and default room till that time
            counter++;
        }
        System.out.println("reached point handleMessage");
        // TODO handle the messages here
        //session.sendMessage(new TextMessage("echo from handler: " + message.getPayload()));

        String payload = (String) message.getPayload();
        System.out.println("from client" + payload);

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
                case CHAT -> handleChatMessage(session, payload);
                //case DRAW_CARD -> handleDrawCard(session, payload);
                case DRAW_CARD -> HandlerDrawCard.handleDrawCard(session, payload, sessions, roomRepo);
                //def.
                default -> System.out.println("unknown message type received");
            }
        }
    }

    public void handleGameBoardMessage(WebSocketSession session, String payload) throws Exception {
        //TODO: Task Flo...
        //TODO: at the moment empty class, that no error occur
    }



    public void handleChatMessage(WebSocketSession session, String payload) throws Exception {
                //TODO:
                /**
                 * nachricht erreicht den handler
                 * die nachricht hat einen sender, eine raumID und eine text(nachricht)
                 * ein client "sendet" eine nachricht an den server
                 * danach wird die nachricht wieder vom server als json verpackt und an alle clients versendet
                 * ob eine nachricht dann vom client angenommen wird, hängt von der raumID ab
                 * wenn raumID des clients == raumID in der message --> dann wird die Nachricht angenommen
                 * zusätzlich wäre es auch möglich, dass man eigene nachrichten nicht sieht, wenn man zb
                 * noch zusätzlich die playerID vom absender hinzufügt und der absender seine eigene
                 * chat-nachricht ignoriert...*/

                ChatMessage chatMessage = gson.fromJson(payload, ChatMessage.class);

                if (chatMessage == null) {
                    ChatMessage errorMsg = new ChatMessage();
                    errorMsg.setActionTypeChat(ChatMessage.ActionTypeChat.CHAT_MSG_TO_CLIENTS_ERR);
                    String exportErrMsg = gson.toJson(errorMsg);
                    session.sendMessage(new TextMessage(exportErrMsg));

                }

                String playerNameSender;
                String playerIdSender;
                String inputText;
                String roomId;

                if (chatMessage.getPlayerId() == null || chatMessage.getPlayerName() == null || chatMessage.getText() == null || chatMessage.getRoomID() == null
                        || chatMessage.getPlayerId().isEmpty() || chatMessage.getPlayerName().isEmpty() || chatMessage.getText().isEmpty() || chatMessage.getRoomID().isEmpty()) {
                    System.out.println("invalid input");
                    ChatMessage errorMsg = new ChatMessage();
                    errorMsg.setActionTypeChat(ChatMessage.ActionTypeChat.CHAT_MSG_TO_CLIENTS_ERR);
                    String exportErrMsg = gson.toJson(errorMsg);
                    session.sendMessage(new TextMessage(exportErrMsg));
                    return;
                }

                //playerIdSender = chatMessage.getPlayerId();
                //playerNameSender = chatMessage.getPlayerName();
                inputText = chatMessage.getText();
                //roomId = chatMessage.getRoomID();

                String outputText = CensorMessageFunction.censorText(inputText, CensoredWordsDB.censoredWords);

                chatMessage.setText(outputText);
                String payloadExport = gson.toJson(chatMessage);
                broadcastMsg(payloadExport, session);
            }



    private void handleGuessCheater(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);
    }







    private void handleDrawCard(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        DrawCardMessage drawCardMessage = gson.fromJson(payload, DrawCardMessage.class);

        //error check1
        if (drawCardMessage == null) {
            System.out.println("error draw card");
            DrawCardMessage errorMsg = new DrawCardMessage();
            errorMsg.setActionTypeDrawCard(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_ERR);
            errorMsg.setCard("error");
            String export = gson.toJson(errorMsg);
            broadcastMsg(export, session);
            return;
        }

        String roomId = drawCardMessage.getRoomID();
        String playerId = drawCardMessage.getPlayerID();
        //DrawCardMessage.ActionTypeDrawCard actionTypeDrawCard = drawCardMessage.getActionTypeDrawCard();
        String inputCard = drawCardMessage.getCard();

        Room room = roomRepo.findRoomById(roomId);
        if (room == null) {
            System.out.println("room not found" + roomId);
            return;
        }

        Player player = room.getPlayerById(playerId);
        if (player == null) {
            System.out.println("player not found" + playerId);
            return;
        }

        //now handle the return of card

        //first reset if player == cheater
        //catching is only possible, if it not the players turn again
        room.deletePlayerFromCheatList(playerId);

        String nextPlayerId = room.getNextPlayer(playerId).getPlayerID();
        String cardReturned;

        switch (inputCard) {
            case "random" -> cardReturned = RandomCardGenerator.startCardGenerator();
            case "1", "2", "3", "carrot" -> {
                room.addPlayerToCheatList(playerId);
                cardReturned = inputCard;
            }
            default -> {
                System.out.println("invalid input: " + inputCard);
                return;
            }
        }

        //now send it... finally
        drawCardMessage.setActionTypeDrawCard(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_OK);
        drawCardMessage.setCard(cardReturned);
        drawCardMessage.setNextPlayerId(nextPlayerId);
        String exportPayload = gson.toJson(drawCardMessage);
        broadcastMsg(exportPayload, session);

        logger.info("toClient: " + exportPayload);

    }





    private void handleAskForRoomList(WebSocketSession session, String payload) throws Exception {
        Logger logger = Logger.getLogger(getClass().getName());
        //Gson gson = new Gson();
        logger.info("ask for room list reached");
        RoomListMessage roomListMessage = gson.fromJson(payload, RoomListMessage.class);

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

