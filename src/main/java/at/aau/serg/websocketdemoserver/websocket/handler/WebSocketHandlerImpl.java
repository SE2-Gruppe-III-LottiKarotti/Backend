package at.aau.serg.websocketdemoserver.websocket.handler;

import at.aau.serg.websocketdemoserver.model.game.Spieler;
import at.aau.serg.websocketdemoserver.model.logic.CensorMessageFunction;
import at.aau.serg.websocketdemoserver.model.logic.RandomCardGenerator;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;
import at.aau.serg.websocketdemoserver.msg.*;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;

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
    private final InMemoryRoomRepo roomRepo = new InMemoryRoomRepo();

    private final Gson gson = new Gson();
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    static long counter = 0; // wird nur für die initialisierung der testRooms verwendet

    //broadcasting
    private void broadcastMsg(String message) throws Exception{
        if (message == null) {
            System.out.println("error: message to broadcast was null");
            //assert (false);
            //sollte mit logger ausgetauscht werden
            return;
        }
        for (WebSocketSession client: sessions) {
            client.sendMessage(new TextMessage(message));
        }

    }

    private void initTestRooms() {
        String playerName = "FranzSissi";
        String player2 = "Daniel";
        Spieler spieler1 = new Spieler(playerName);
        Spieler spieler2 = new Spieler(player2);
        Room testRoom1 = new Room(2, "TestRoom");
        testRoom1.setCreatorName(playerName);
        testRoom1.addPlayer(spieler1);
        Room testRoom2 = new Room(3, "TestRo2");
        testRoom2.setCreatorName(playerName);
        testRoom2.addPlayer(spieler1);
        Room testRoom3 = new Room(2, "TestRo3");
        testRoom3.setCreatorName(playerName);
        testRoom3.addPlayer(spieler1);
        testRoom3.addPlayer(spieler2);
        //testRoom3 dient dem Test, ob ein voller Raum übermittelt wird oder nicht...


        //add
        roomRepo.addRoom(testRoom1);
        roomRepo.addRoom(testRoom2);
        roomRepo.addRoom(testRoom3);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
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
                case TEST -> handleTestMessage(session, payload);
                //msg for constant load on network (ping - pong - ping - ...)
                case HEARTBEAT -> handleHeartbeat(session, payload);
                //messages for opening, joining and listing rooms
                case OPEN_ROOM -> handleOpenRoomMessage(session, payload);
                case JOIN_ROOM -> handleAskForJoinRoom(session, payload);
                case LIST_ROOMS -> handleAskForRoomList(session, payload);
                //messages for gameboard logic
                case GAMEBOARD -> handleGameBoardMessage(session, payload);
                case CHAT -> handleChatMessage(session, payload);
                case DRAW_CARD -> handleDrawCard(session, payload);
                //def.
                default -> System.out.println("unknown message type received");
            }
        }
    }

    private void handleHeartbeat(WebSocketSession session, String payload) throws Exception {
        HeartbeatMessage heartbeatMessage = gson.fromJson(payload, HeartbeatMessage.class);

        if (heartbeatMessage != null && heartbeatMessage.getText().equals("ping")) {
            /*heartbeatMessage.setText("pong");
            String response = gson.toJson(heartbeatMessage);
            session.sendMessage(new TextMessage(response));*/
            System.out.println("valid message");

        }
        else {
            System.out.println("invalid message");
        }


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
        broadcastMsg(payloadExport);

    }

    private void handleSetupField(WebSocketSession session, String payload) throws Exception {
        /*Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);
        Room room = roomRepo.findRoomById(roomMessage.getRoomID());

        ArrayList<Spieler> playerList = room.getListOfPlayers();
        Random random = new SecureRandom();
        int playerToStart = random.nextInt(4) + 1;
        roomMessage.setCurrentPlayer(playerList.get(playerToStart));
        roomMessage.setPlayerIndex(playerToStart);

        Gameboard gameboard = new Gameboard(playerList.size());
        roomMessage.setGameboard(gameboard);

        String positivePayload = gson.toJson(roomMessage);
        session.sendMessage(new TextMessage(positivePayload));*/
    }

    private void handleGuessCheater(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);
    }

    private void handleNextPlayer(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);
        ArrayList<Spieler> PlayerList;
        PlayerList = roomMessage.getListOfPlayers();
        int playerIndex = roomMessage.getPlayerIndex();
        roomMessage.setNextPlayer(PlayerList.get((playerIndex + 1) % PlayerList.size()));
        roomMessage.setCurrentPlayer(roomMessage.getNextPlayer());

        String positivePayload = gson.toJson(roomMessage);
        session.sendMessage(new TextMessage(positivePayload));
    }

    private void handleGameBoardMessage(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);

        switch (roomMessage.getActionType()) {
            case DRAWCARD -> handleDrawCard(session, payload);
            case CHAT -> handleChatMessage(session, payload);
            case SETUPFIELD -> handleSetupField(session, payload);
            case GUESSCHEATER -> handleGuessCheater(session, payload);
            case NEXTPlAYER -> handleNextPlayer(session, payload);
        }
    }

    private void handleOpenRoomMessage(WebSocketSession session, String payload) throws Exception {
        //1 extract
        OpenRoomMessage openRoomMessage = gson.fromJson(payload, OpenRoomMessage.class);

        //2 set local variables
        String roomName = openRoomMessage.getRoomName();
        String playerName = openRoomMessage.getPlayerName();
        int maxPlayers = Integer.parseInt(openRoomMessage.getNumPlayers());

        //3_1 checks1
        if (roomName == null || roomName.isEmpty() || playerName == null || playerName.isEmpty() || maxPlayers < 2 || maxPlayers > 4) {
            //fehlerhafte Werte
            openRoomMessage.setOpenRoomActionType(OpenRoomMessage.OpenRoomActionType.OPEN_ROOM_ERR);
            String errorPayload = gson.toJson(openRoomMessage);
            session.sendMessage(new TextMessage(errorPayload));
            return;
        }

        //3_2 check2
        Room foundRoom = roomRepo.findRoomByName(roomName);
        System.out.println("****");
        System.out.println(foundRoom);
        System.out.println("****");

        //4 checks --> wenn der raum schon existiert...
        if (foundRoom != null) {
            // Room already exists
            // 4_1 senden...
            if (foundRoom.getRoomName().equals(openRoomMessage.getRoomName())) {
                openRoomMessage.setOpenRoomActionType(OpenRoomMessage.OpenRoomActionType.OPEN_ROOM_ERR);
                String errorPayload = gson.toJson(openRoomMessage);
                session.sendMessage(new TextMessage(errorPayload));
                System.out.println("toClient: " + errorPayload);
                return;

            }

        }

        //5 positive case --> der raum wird hinzugefügt
        //else
        //room does not exist already
        Room roomToAdd = new Room(maxPlayers, roomName);
        //Spieler erschaffen
        Spieler creator = new Spieler(playerName);
        String creatorId = creator.getSpielerID();
        roomToAdd.addPlayer(creator);
        roomToAdd.setCreatorName(playerName);
        System.out.println("after adding player " + roomToAdd.getAvailablePlayersSpace());

        //5_1 raum auch zur repo --> also zur DB hinzufügen
        //add room to repo
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
        String positivePayload = gson.toJson(openRoomMessage);
        session.sendMessage(new TextMessage(positivePayload));
        System.out.println("end of player hinzugefügt");
        System.out.println("toClient: " + positivePayload);


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
            broadcastMsg(export);
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

        Spieler player = room.getPlayerById(playerId);
        if (player == null) {
            System.out.println("player not found" + playerId);
            return;
        }

        //now handle the return of card

        //first reset if player == cheater
        //catching is only possible, if it not the players turn again
        room.deletePlayerFromCheatList(playerId);

        String cardReturned;

        switch (inputCard) {
            case "random" -> cardReturned = RandomCardGenerator.start();
            case "1", "2", "3", "karotte" -> {
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
        String exportPayload = gson.toJson(drawCardMessage);
        session.sendMessage(new TextMessage(exportPayload));

        System.out.println("toClient: " + exportPayload);

    }


    private void handleAskForJoinRoom(WebSocketSession session, String payload) throws Exception {
        //TODO:implement the logic here
        /**
         * es ist nicht notwendig, dass der name gecheckt wird
         * es dürfen auch mehrere spieler mit gleichen namen dabei sein
         * es muss jedoch der raum stimmen und es muss
         * noch platz im raum sein --> hinweis availablePlayerSpace
         * vll. braucht man einen mutex für die operation, wo ein Spieler zu einem Raum beitritt
         * ... es könnten ja gleich mehrere potenzielle Spieler den Gedanken haben beim gleichen
         * Raum beitreten bzw. einsteigen zu wollen...
         * */

        //1 extract
        JoinRoomMessage joinRoomMessage = gson.fromJson(payload, JoinRoomMessage.class);

        //2 set local variables
        String roomName = joinRoomMessage.getRoomName();
        String playerName = joinRoomMessage.getPlayerName();

        //3 check
        if (roomName == null || roomName.isEmpty() || playerName == null || playerName.isEmpty()) {
            //fehlerhafte Werte
            joinRoomMessage.setActionTypeJoinRoom(JoinRoomMessage.ActionTypeJoinRoom.JOIN_ROOM_ERR);
            String errorPayload = gson.toJson(joinRoomMessage);
            session.sendMessage(new TextMessage(errorPayload));
            return;
        }

        // this need to be syncronized

        Room foundRoom = roomRepo.findRoomByName(roomName);
        System.out.println("****");
        System.out.println(foundRoom);
        System.out.println("****");


        // if room exists, add player to room
        if (foundRoom == null) {
            // raum existiert nicht
            joinRoomMessage.setActionTypeJoinRoom(JoinRoomMessage.ActionTypeJoinRoom.JOIN_ROOM_ERR);
            String errorPayload = gson.toJson(joinRoomMessage);
            session.sendMessage(new TextMessage(errorPayload));
            System.out.println("toClient: " + errorPayload);
            return;

        }

        //valid, now add player
        Spieler playerToAdd = new Spieler(playerName);
        foundRoom.addPlayer(playerToAdd);

        //msg set
        joinRoomMessage.setActionTypeJoinRoom(JoinRoomMessage.ActionTypeJoinRoom.JOIN_ROOM_OK);
        joinRoomMessage.setRoomId(foundRoom.getRoomID());
        joinRoomMessage.setPlayerId(playerToAdd.getSpielerID());
        joinRoomMessage.setPlayerName(playerToAdd.getName());
        //msg send
        String positivePayload = gson.toJson(joinRoomMessage);
        session.sendMessage(new TextMessage(positivePayload));
        System.out.println("toClient: " + positivePayload);


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

    private void handleTestMessage(WebSocketSession session, String payload) throws Exception {
        //1 user message aus der payload extrahieren
        Gson gson = new Gson();
        //TestMessage testMessage = gson.fromJson(payload, TestMessage.class);
        TestMessage testMessage = gson.fromJson(payload, TestMessage.class);

        System.out.println("testmessage received " + testMessage.getText());
        //2 add additional or set information to message with set
        testMessage.setMessageType(MessageType.TEST);
        testMessage.setText("juhu");
        String msgIdentifier = testMessage.getMessageIdentifier();
        testMessage.setMessageIdentifier(msgIdentifier);
        //make a new payload
        String payloadExport = gson.toJson(testMessage);
        //print to terminal
        System.out.println("testmessage changed and send " + testMessage.getText());
        // Send echo back to the client --> export
        //umbedingt payload verwenden!!!!!!
        //session.sendMessage(new TextMessage("echo from handler: " + payload));
        session.sendMessage(new TextMessage(payloadExport));
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
