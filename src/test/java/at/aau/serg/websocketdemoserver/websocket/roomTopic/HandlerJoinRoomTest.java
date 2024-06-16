package at.aau.serg.websocketdemoserver.websocket.roomTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.JoinRoomMessageImpl;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerJoinRoom;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class HandlerJoinRoomTest {

    private static final Gson gson = new Gson ();
    private WebSocketSession session;

    private JoinRoomMessageImpl joinRoomMessage;

    private final InMemoryRoomRepo inMemoryRoomRepoTest = new InMemoryRoomRepo();

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
        inMemoryRoomRepoTest.addRoom(testRoom1);
        inMemoryRoomRepoTest.addRoom(testRoom2);
        inMemoryRoomRepoTest.addRoom(testRoom3);
    }

    @BeforeEach
    public void setup() {
        session = mock(WebSocketSession.class);
        joinRoomMessage = new JoinRoomMessageImpl();
        //initialize the default rooms
        initTestRooms();
    }

    @Test
    public void testValidJoinRoom() throws Exception {

        JoinRoomMessageImpl joinRoomMessage = new JoinRoomMessageImpl();
        joinRoomMessage.setRoomName("TestRoom");
        joinRoomMessage.setPlayerName("Julie");
        String payload = TransportUtils.helpToJson(joinRoomMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerJoinRoom.handleAskForJoinRoom(session, payload, inMemoryRoomRepoTest);

        verify(session, times(1)).sendMessage(captor.capture());
        TextMessage responseMessage = captor.getValue();
        JoinRoomMessageImpl responseJoinRoomMessage = TransportUtils.helpFromJson(responseMessage.getPayload(), JoinRoomMessageImpl.class);

        assertNotNull(responseJoinRoomMessage);
        assertEquals(JoinRoomMessageImpl.ActionTypeJoinRoom.JOIN_ROOM_OK, responseJoinRoomMessage.getActionTypeJoinRoom());
        assertNotNull(responseJoinRoomMessage.getRoomId());
        assertEquals("TestRoom", responseJoinRoomMessage.getRoomName());
        assertEquals("Julie", responseJoinRoomMessage.getPlayerName());
        assertNotNull(responseJoinRoomMessage.getPlayerId());

    }

    @Test
    public void testInvalidJoinRoom_RoomNameDoesntExist() throws Exception {

        JoinRoomMessageImpl joinRoomMessage = new JoinRoomMessageImpl();
        joinRoomMessage.setRoomName("TestRoo");
        joinRoomMessage.setPlayerName("Julie");
        String payload = TransportUtils.helpToJson(joinRoomMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerJoinRoom.handleAskForJoinRoom(session, payload, inMemoryRoomRepoTest);

        verify(session, times(1)).sendMessage(captor.capture());
        TextMessage responseMessage = captor.getValue();
        JoinRoomMessageImpl responseJoinRoomMessage = TransportUtils.helpFromJson(responseMessage.getPayload(), JoinRoomMessageImpl.class);

        assertNotNull(responseJoinRoomMessage);
        assertEquals(JoinRoomMessageImpl.ActionTypeJoinRoom.JOIN_ROOM_ERR, responseJoinRoomMessage.getActionTypeJoinRoom());
        assertEquals("TestRoo", responseJoinRoomMessage.getRoomName());
        assertEquals("Julie", responseJoinRoomMessage.getPlayerName());

    }

    @Test
    public void testInvalidJoinRoom_RoomNameNULL() throws Exception {

        JoinRoomMessageImpl joinRoomMessage = new JoinRoomMessageImpl();
        joinRoomMessage.setRoomName(null);
        joinRoomMessage.setPlayerName("Julie");
        String payload = TransportUtils.helpToJson(joinRoomMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerJoinRoom.handleAskForJoinRoom(session, payload, inMemoryRoomRepoTest);

        verify(session, times(1)).sendMessage(captor.capture());
        TextMessage responseMessage = captor.getValue();
        JoinRoomMessageImpl responseJoinRoomMessage = TransportUtils.helpFromJson(responseMessage.getPayload(), JoinRoomMessageImpl.class);

        assertNotNull(responseJoinRoomMessage);
        assertEquals(JoinRoomMessageImpl.ActionTypeJoinRoom.JOIN_ROOM_ERR, responseJoinRoomMessage.getActionTypeJoinRoom());
        assertNull(responseJoinRoomMessage.getRoomName());
        assertEquals("Julie", responseJoinRoomMessage.getPlayerName());

    }

    @Test
    public void testInvalidJoinRoom_PlayerNameNULL() throws Exception {

        JoinRoomMessageImpl joinRoomMessage = new JoinRoomMessageImpl();
        joinRoomMessage.setRoomName("TestRoom");
        joinRoomMessage.setPlayerName(null);
        String payload = TransportUtils.helpToJson(joinRoomMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerJoinRoom.handleAskForJoinRoom(session, payload, inMemoryRoomRepoTest);

        verify(session, times(1)).sendMessage(captor.capture());
        TextMessage responseMessage = captor.getValue();
        JoinRoomMessageImpl responseJoinRoomMessage = TransportUtils.helpFromJson(responseMessage.getPayload(), JoinRoomMessageImpl.class);

        assertNotNull(responseJoinRoomMessage);
        assertEquals(JoinRoomMessageImpl.ActionTypeJoinRoom.JOIN_ROOM_ERR, responseJoinRoomMessage.getActionTypeJoinRoom());
        assertNull(responseJoinRoomMessage.getPlayerName());
        assertEquals("TestRoom", responseJoinRoomMessage.getRoomName());

    }

    @Test
    public void sessionIsNULL () {
        String payload = gson.toJson(joinRoomMessage);
        assertThrows(NullPointerException.class, () -> HandlerJoinRoom.handleAskForJoinRoom(null, payload, inMemoryRoomRepoTest));
    }

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerJoinRoom.handleAskForJoinRoom(session, null, inMemoryRoomRepoTest));
    }

    @Test
    public void testJsonErrMsg () {
        String payload = "{error}";
        assertThrows(JsonParseException.class, () -> HandlerJoinRoom.handleAskForJoinRoom(session, payload, inMemoryRoomRepoTest));
    }

    @AfterEach
    public void tearDown() {
        session = null;
        joinRoomMessage = null;

    }



}
