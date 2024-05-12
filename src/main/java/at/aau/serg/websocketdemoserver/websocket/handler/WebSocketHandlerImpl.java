package at.aau.serg.websocketdemoserver.websocket.handler;

import at.aau.serg.websocketdemoserver.model.game.Spieler;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.*;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.repository.InMemorySpielerRepo;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandlerImpl implements WebSocketHandler {

    @Autowired
    private final InMemoryRoomRepo roomRepo = new InMemoryRoomRepo();

    /*@Autowired
    public WebSocketHandlerImpl(InMemoryRoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }*/

    //private static final Set<Room> roomsRepo = new HashSet<>();

    //@Autowired
    //private InMemoryRoomRepo roomRepo;

    /*@PostConstruct
    public void init() {
        this.roomRepo = InMemoryRoomRepo.getInstance();
    }*/


    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
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
                case SPIELER -> handleSpielerMessage(session, payload);
                case SETUP_ROOM -> handleOpenRoomMessage(session, payload);
                default -> System.out.println("unknown message type received");
            }
        }
    }

    private void handleOpenRoomMessage(WebSocketSession session, String payload) throws Exception {
        Gson gson = new Gson();
        RoomSetupMessage roomSetupMessage = gson.fromJson(payload, RoomSetupMessage.class);
        System.out.println(payload);
        System.out.println("open room message handler reached");

        String roomName = roomSetupMessage.getRoomName();
        String playerName = roomSetupMessage.getPlayerName();
        int maxPlayers = Integer.parseInt(roomSetupMessage.getNumPlayers());
        String messageIdentifier = roomSetupMessage.getMessageIdentifier();

        if (roomName == null || roomName.length() == 0 || playerName == null || playerName.length() == 0 || maxPlayers < 2 || maxPlayers > 4 || messageIdentifier == null) {
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



        if (foundRoom != null) {
            // Room already exists
            roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_ERR);
            String errorPayload = gson.toJson(roomSetupMessage);
            session.sendMessage(new TextMessage(errorPayload));
            return;
        }

        //else
        //room does not exist already
        Room roomToAdd = new Room(maxPlayers, roomName);
        //Spieler erschaffen
        Spieler creator = new Spieler(playerName);
        String creatorId = creator.getSpielerID();
        roomToAdd.addPlayer(creator);
        System.out.println("after adding player " + roomToAdd.getAvailablePlayersSpace());

        //add room to repo
        roomRepo.addRoom(roomToAdd);

        //now prepare message
        roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_OK);
        roomSetupMessage.setRoomID(roomToAdd.getRoomID());
        roomSetupMessage.setRoomName(roomToAdd.getRoomName());
        roomSetupMessage.setPlayerID(creatorId);
        roomSetupMessage.setPlayerName(creator.getName());
        roomSetupMessage.setNumPlayers(Integer.toString(maxPlayers));
        roomSetupMessage.setMessageIdentifier(messageIdentifier);


        //RoomSetupMessage roomSetupMessage = new RoomSetupMessage(roomName, "emptyRoomID", creatorName, "emptyPlayerID", finalNumPlayers, messageIdentifierOpenRoom, RoomSetupMessage.ActionType.OPEN_ROOM);

        //prepare positive payload
        String positivePayload = gson.toJson(roomSetupMessage);
        session.sendMessage(new TextMessage(positivePayload));
        System.out.println("end of player hinzugefügt");
        System.out.println(positivePayload);


    }



    /*private void handleOpenRoomMessage(WebSocketSession session, String payload) throws Exception {

        //TODO: use the situation, that for roomID is send emtpyRoomID
        //TODO: and for playerID is send emptyPlayerID
        //TODO: so it should be easier to check for duplicates ;)

        System.out.println("open room message handler reached");
        Gson gson = new Gson();
        RoomSetupMessage roomSetupMessage = gson.fromJson(payload, RoomSetupMessage.class);
        String roomName = roomSetupMessage.getRoomName();
        int maxPlayers = Integer.parseInt(roomSetupMessage.getNumPlayers());
        String playerName = roomSetupMessage.getPlayerName();

        // Check for existing room using findRoomByName
        Room existingRoom = roomRepo.findRoomByName(roomName);

        if (existingRoom != null) {
            System.out.println("Room already exists");
            // Room already exists, send error
            roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_ERR);
            String errorPayload = gson.toJson(roomSetupMessage);
            session.sendMessage(new TextMessage(errorPayload));
            return;
        }

        // Create a new room if it doesn't exist
        Room roomToAdd = new Room(maxPlayers, roomName);
        System.out.println("add room");
        roomRepo.addRoom(roomToAdd);

        // Check if room is full before adding player
        if (roomToAdd.getAvailablePlayersSpace() == 0) {
            System.out.println("Raum voll");
            roomSetupMessage.setActionType(RoomSetupMessage.ActionType.ROOM_FULL);
            String fullRoomPayload = gson.toJson(roomSetupMessage);
            session.sendMessage(new TextMessage(fullRoomPayload));
            System.out.println("end of send raum voll");
            return;
        }

        // Add player to the new room

        System.out.println("before adding player " + roomToAdd.getAvailablePlayersSpace());

        System.out.println("player hinzugefügt");

        roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_OK);

        Spieler spieler = new Spieler(playerName);
        String spielerId = spieler.getSpielerID();
        roomToAdd.addPlayer(spieler);
        System.out.println("after adding player " + roomToAdd.getAvailablePlayersSpace());


        roomSetupMessage.setRoomID(roomToAdd.getRoomID());
        roomSetupMessage.setPlayerID(spielerId);
        String positivePayload = gson.toJson(roomSetupMessage);
        session.sendMessage(new TextMessage(positivePayload));
        System.out.println("end of player hinzugefügt");
        System.out.println(positivePayload);
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

    private void handleSpielerMessage(WebSocketSession session, String payload) throws Exception{
        Gson gson = new Gson();
        SpielerMessage spielerMessage = gson.fromJson(payload, SpielerMessage.class);
        System.out.println("recieved userMsg.:");
        System.out.println(spielerMessage);
        System.out.println("*************");

        //Spieler Message actionType switch
        switch(spielerMessage.getActionType()) {
            //case LOGIN -> handleLoginAction(session, payload);
            default -> System.out.println("unknown type");
        }

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
