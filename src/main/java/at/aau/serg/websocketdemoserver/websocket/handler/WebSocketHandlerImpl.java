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



        //4 checks --> wenn der raum schon existiert...
        if (foundRoom != null) {
            // Room already exists
            // 4_1 senden...
            roomSetupMessage.setActionType(RoomSetupMessage.ActionType.OPEN_ROOM_ERR);
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
        System.out.println(positivePayload);


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
