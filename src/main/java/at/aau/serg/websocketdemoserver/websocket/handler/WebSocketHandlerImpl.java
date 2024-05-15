package at.aau.serg.websocketdemoserver.websocket.handler;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.RandomCardGenerator;
import at.aau.serg.websocketdemoserver.model.game.Spieler;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.*;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import ch.qos.logback.core.joran.spi.EventPlayer;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.random;

@Component
public class WebSocketHandlerImpl implements WebSocketHandler {

    @Autowired
    private final InMemoryRoomRepo roomRepo = new InMemoryRoomRepo();

    private final Gson gson = new Gson();
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    static long counter = 0; // wird nur für die initialisierung der testRooms verwendet
/*
    private void initTestRooms() {
        String playerName = "FranzSissi";
        String player2 = "Daniel";
        Spieler spieler1 = new Spieler(playerName);
        Spieler spieler2 = new Spieler(player2);
        Room testRoom1 = new Room(2, "TestRo1");
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
 */



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        //hinzufügen von Testrooms
        if (counter == 0) {
            //initTestRooms();
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
                case TEST -> handleTestMessage(session, payload);
                //case SPIELER -> handleSpielerMessage(session, payload);
                case CREATE_ROOM -> createRoomMessage(session, payload);
                case GAMEBOARD -> handleGameBoardMessage(session, payload);
                case CHAT -> handleChatMessage(session, payload);
                case DRAW_CARD -> handleDrawCard(session, payload);
                default -> System.out.println("unknown message type received");
            }
        }
    }

    public void handleDrawCard(WebSocketSession session, String payload) throws Exception {
        //TODO:
        /**
         * Spieler "zieht" clientseitig eine karte...
         * der spieler übermittelt dabei eine message, welche folgende bestandteile aufweist
         * roomID, spielerID, messageIdentifier (damit die nachricht exakt an den zurück geht,
         * welcher die nachricht abgesendet hat (da jedoch nur einer pro Raum zum gleichen
         * Zeitpunkt am ziehen sein kann, kann man sich den msgIdentifier vll. sparen)
         * die wichtigste Variable ist jedoch der String, welcher übermittelt wird,...
         *
         * random => spieler schummelt nicht und bekommt eine Karte zufällig übermittelt
         * 1 oder 2, oder 3 oder Karotte --> spieler schummelt und der Spieler wird so lange
         * als schummler deklariert, bis er wieder am Zug ist,...
         * in dieser Zeit kann er auch als schummler erwischt werden*/
        Gson gson = new Gson();
        DrawCardMessage drawCardMessage = gson.fromJson(payload, DrawCardMessage.class);

        RandomCardGenerator randomCardGenerator = new RandomCardGenerator();
        String randomCart = randomCardGenerator.start();
        DrawCardResponse drawCardResponse = new DrawCardResponse();
        drawCardResponse.setCard(randomCart);

        String positivePayload = gson.toJson(drawCardResponse);
        session.sendMessage(new TextMessage(positivePayload));
    }

    public void handleChatMessage(WebSocketSession session, String payload) throws Exception{
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

    }

    public void handleSetupField(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);
        Room room = roomRepo.findRoomById(roomMessage.getRoomID());

        ArrayList<Spieler> playerList = room.getListOfPlayers();
        Random random = new Random();
        int playerToStart = random.nextInt(4)+1;
        roomMessage.setCurrentPlayer(playerList.get(playerToStart));
        roomMessage.setPlayerIndex(playerToStart);

        Gameboard gameboard = new Gameboard(playerList.size());
        roomMessage.setGameboard(gameboard);

        String positivePayload = gson.toJson(roomMessage);
        session.sendMessage(new TextMessage(positivePayload));
    }

    public void handleGuessCheater(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);
    }

    public void handleNextPlayer(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);
        ArrayList<Spieler> PlayerList;
        PlayerList = roomMessage.getListOfPlayers();
        int playerIndex = roomMessage.getPlayerIndex();
        roomMessage.setNextPlayer(PlayerList.get((playerIndex+1) % PlayerList.size()));
        roomMessage.setCurrentPlayer(roomMessage.getNextPlayer());

        String positivePayload = gson.toJson(roomMessage);
        session.sendMessage(new TextMessage(positivePayload));
    }

    public void handleGameBoardMessage(WebSocketSession session, String payload) throws Exception{
        Gson gson = new Gson();
        RoomMessage roomMessage = gson.fromJson(payload, RoomMessage.class);

        switch(roomMessage.getActionType()) {
            case DRAWCARD -> handleDrawCard(session, payload);
            case CHAT -> handleChatMessage(session, payload);
            case SETUPFIELD -> handleSetupField(session, payload);
            case GUESSCHEATER -> handleGuessCheater(session, payload);
            case NEXTPlAYER -> handleNextPlayer(session,payload);
        }
    }

    private void createRoomMessage(WebSocketSession session, String payload) throws Exception{
        CreateRoomMessage createRoomMessage = gson.fromJson(payload, CreateRoomMessage.class);
        Room room =  new Room();
        room.setRoomName(createRoomMessage.getRoomName());
        Spieler spieler = new Spieler();
        spieler.setName(createRoomMessage.getPlayerName());
        room.addPlayer(spieler);
        roomRepo.addRoom(room);
        String message = gson.toJson(room.getRoomID());
        session.sendMessage(new TextMessage(message));
    }
/*
    private void handleSetupRoomMessage(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        //1 deserialisierung
        RoomSetupMessage roomSetupMessage = gson.fromJson(payload, RoomSetupMessage.class);

        switch(roomSetupMessage.getActionType()) {
            case OPEN_ROOM -> handleOpenRoomMessage(session, payload);
            case ASK_FOR_ROOM_LIST -> handleAskForRoomList(session, payload);
            case ASK_FOR_JOIN_ROOM -> handleAskForJoinRoom(session, payload);
        }
    }
 */

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
    }

    private void handleAskForRoomList(WebSocketSession session, String payload) throws Exception{
        //TODO:
        /**
         * es kommt eine SetupRoomMessage rein...
         * bei welcher ActionType == ASK_FOR_ROOM_LIST ist
         * bekommt der server diese nachricht, kommt man in diesen handler
         * dieser handler hat die Aufgabe eine Liste an den Client via json zu transportieren
         * aber nur räume, wo zumindest noch ein Spieler beitreten kann...
         * RoomInfo würde sich gut dazu eignen, da dort nur die Variablen definiert sind, welche
         * benötigt werden*/


    }
    /*
    private void handleOpenRoomMessage(WebSocketSession session, String payload) throws Exception {
        //1 nachricht, welche vom server reinkommt und durch den messageHandler durch ist verwenden...
        // aus der json wieder das Objekt basteln --> also deserialisieren
        Gson gson = new Gson();
        RoomSetupMessage roomSetupMessage = gson.fromJson(payload, RoomSetupMessage.class);
        System.out.println(payload);
        System.out.println("open room message handler reached");

        //2 daten extrahieren
        String roomName = roomSetupMessage.getRoomName();
        String playerName = roomSetupMessage.getPlayerName();
        int maxPlayers = Integer.parseInt(roomSetupMessage.getNumPlayers());
        String messageIdentifier = roomSetupMessage.getMessageIdentifier();

        //3 checks
        if (roomName == null || roomName.isEmpty() || playerName == null || playerName.isEmpty() || maxPlayers < 2 || maxPlayers > 4 || messageIdentifier == null) {
            //fehlerhafte Werte
            roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_ERR);
            String errorPayload = gson.toJson(roomSetupMessage);
            session.sendMessage(new TextMessage(errorPayload));
            return;
        }

        Room foundRoom = roomRepo.findRoomByName(roomName);
        System.out.println("****");
        System.out.println(foundRoom);
        System.out.println("****");



        //4 checks --> wenn der raum schon existiert...
        if (foundRoom != null) {
            // Room already exists
            // 4_1 senden...
            if (foundRoom.getRoomName().equals(roomSetupMessage.getRoomName())) {
                roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_ERR);
                String errorPayload = gson.toJson(roomSetupMessage);
                session.sendMessage(new TextMessage(errorPayload));
                System.out.println("toClient: " + errorPayload);
                return;

            }
            //das wird wsl nicht mehr benötigt
            /*roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_ERR);
            String errorPayload = gson.toJson(roomSetupMessage);
            session.sendMessage(new TextMessage(errorPayload));
            return;
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
        roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_OK);
        roomSetupMessage.setRoomID(roomToAdd.getRoomID());
        roomSetupMessage.setRoomName(roomToAdd.getRoomName());
        roomSetupMessage.setPlayerID(creatorId);
        roomSetupMessage.setPlayerName(creator.getName());
        roomSetupMessage.setNumPlayers(Integer.toString(maxPlayers));
        roomSetupMessage.setMessageIdentifier(messageIdentifier);


        //RoomSetupMessage roomSetupMessage = new RoomSetupMessage(roomName, "emptyRoomID", creatorName, "emptyPlayerID", finalNumPlayers, messageIdentifierOpenRoom, RoomSetupMessage.ActionType.OPEN_ROOM);

        //5_3 hier wird schlussendlich die positive nachricht versendet...
        //prepare positive payload
        String positivePayload = gson.toJson(roomSetupMessage);
        session.sendMessage(new TextMessage(positivePayload));
        System.out.println("end of player hinzugefügt");
        System.out.println("toClient: " + positivePayload);


    }
    */


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

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
