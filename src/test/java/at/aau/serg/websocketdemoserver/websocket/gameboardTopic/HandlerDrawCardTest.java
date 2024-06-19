package at.aau.serg.websocketdemoserver.websocket.gameboardTopic;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.DrawCardMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerDrawCard;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class HandlerDrawCardTest {

    @Mock
    private WebSocketSession session1;

    @Mock
    private WebSocketSession session2;




    private static final Gson gson = new Gson();
    private List<WebSocketSession> sessions;
    private DrawCardMessage drawCardMessage;
    private final InMemoryRoomRepo inMemoryRoomRepoTest = new InMemoryRoomRepo();

    private String playerId1;
    private String playerId2;

    private void initTestRooms() {
        String playerName = "FranzSissi";
        String playerName2 = "Daniel";
        Player player1 = new Player(playerName);
        Player player2 = new Player(playerName2);
        playerId1 = player1.getPlayerID();
        playerId2 = player2.getPlayerID();
        Room testRoom1 = new Room(2, "TestRoom");
        testRoom1.setCreatorName(playerName);
        testRoom1.addPlayer(player1);
        testRoom1.addPlayer(player2);
        Room testRoom2 = new Room(3, "TestRo2");
        testRoom2.setCreatorName(playerName);
        testRoom2.addPlayer(player1);
        Room testRoom3 = new Room(2, "TestRo3");
        testRoom3.setCreatorName(playerName);
        testRoom3.addPlayer(player1);
        testRoom3.addPlayer(player2);
        //testRoom3 dient dem Test, ob ein voller Raum übermittelt wird oder nicht...


        //add
        inMemoryRoomRepoTest.addRoom(testRoom1);
        inMemoryRoomRepoTest.addRoom(testRoom2);
        inMemoryRoomRepoTest.addRoom(testRoom3);
    }

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);

        drawCardMessage = new DrawCardMessage();
        initTestRooms();
        sessions = new ArrayList<>();
        sessions.add(session1);
        sessions.add(session2);
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);

    }

    @Test
    public void testValidDrawCardRandom() throws Exception {

        Room roomToFind = inMemoryRoomRepoTest.findRoomByName("TestRoom");
        String roomId = roomToFind.getRoomID();
        drawCardMessage.setRoomID(roomId);
        drawCardMessage.setPlayerID(playerId1);
        drawCardMessage.setCard("RANDOM");
        String payload = gson.toJson(drawCardMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerDrawCard.handleDrawCard(session1, payload, sessions, inMemoryRoomRepoTest);

        for (WebSocketSession session : sessions) {
            verify(session, times(1)).sendMessage(captor.capture());
        }

        List<TextMessage> capturedMessages = captor.getAllValues();
        int count = 0;
        for (TextMessage message : capturedMessages) {
            count++;
            DrawCardMessage responseDrawCardMessage = gson.fromJson(message.getPayload(), DrawCardMessage.class);

            assertNotNull(responseDrawCardMessage);
            assertEquals(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_OK, responseDrawCardMessage.getActionTypeDrawCard());
            assertNotNull(responseDrawCardMessage.getCard());
            assertNotNull(responseDrawCardMessage.getNextPlayerId());
            assertEquals(playerId1, responseDrawCardMessage.getPlayerID());
            assertEquals(playerId2, responseDrawCardMessage.getNextPlayerId());
        }
        assertEquals(2, count);

    }

    @Test
    public void testValidDrawCard_ONE() throws Exception {

        Room roomToFind = inMemoryRoomRepoTest.findRoomByName("TestRoom");
        String roomId = roomToFind.getRoomID();
        drawCardMessage.setRoomID(roomId);
        drawCardMessage.setPlayerID(playerId1);
        drawCardMessage.setCard("ONE");
        String payload = gson.toJson(drawCardMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerDrawCard.handleDrawCard(session1, payload, sessions, inMemoryRoomRepoTest);

        for (WebSocketSession session : sessions) {
            verify(session, times(1)).sendMessage(captor.capture());
        }

        List<TextMessage> capturedMessages = captor.getAllValues();
        int count = 0;
        for (TextMessage message : capturedMessages) {
            count++;
            DrawCardMessage responseDrawCardMessage = gson.fromJson(message.getPayload(), DrawCardMessage.class);

            assertNotNull(responseDrawCardMessage);
            assertEquals(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_OK, responseDrawCardMessage.getActionTypeDrawCard());
            assertNotNull(responseDrawCardMessage.getCard());
            assertNotNull(responseDrawCardMessage.getNextPlayerId());
            assertEquals(playerId1, responseDrawCardMessage.getPlayerID());
            assertEquals(playerId2, responseDrawCardMessage.getNextPlayerId());
        }
        assertEquals(2, count);

    }

    @Test
    public void testValidDrawCardRandom_TWO() throws Exception {

        Room roomToFind = inMemoryRoomRepoTest.findRoomByName("TestRoom");
        String roomId = roomToFind.getRoomID();
        drawCardMessage.setRoomID(roomId);
        drawCardMessage.setPlayerID(playerId1);
        drawCardMessage.setCard("TWO");
        String payload = gson.toJson(drawCardMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerDrawCard.handleDrawCard(session1, payload, sessions, inMemoryRoomRepoTest);

        for (WebSocketSession session : sessions) {
            verify(session, times(1)).sendMessage(captor.capture());
        }

        List<TextMessage> capturedMessages = captor.getAllValues();
        int count = 0;
        for (TextMessage message : capturedMessages) {
            count++;
            DrawCardMessage responseDrawCardMessage = gson.fromJson(message.getPayload(), DrawCardMessage.class);

            assertNotNull(responseDrawCardMessage);
            assertEquals(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_OK, responseDrawCardMessage.getActionTypeDrawCard());
            assertNotNull(responseDrawCardMessage.getCard());
            assertNotNull(responseDrawCardMessage.getNextPlayerId());
            assertEquals(playerId1, responseDrawCardMessage.getPlayerID());
            assertEquals(playerId2, responseDrawCardMessage.getNextPlayerId());
        }
        assertEquals(2, count);

    }

    @Test
    public void testValidDrawCardRandom_THREE() throws Exception {

        Room roomToFind = inMemoryRoomRepoTest.findRoomByName("TestRoom");
        String roomId = roomToFind.getRoomID();
        drawCardMessage.setRoomID(roomId);
        drawCardMessage.setPlayerID(playerId1);
        drawCardMessage.setCard("THREE");
        String payload = gson.toJson(drawCardMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerDrawCard.handleDrawCard(session1, payload, sessions, inMemoryRoomRepoTest);

        for (WebSocketSession session : sessions) {
            verify(session, times(1)).sendMessage(captor.capture());
        }

        List<TextMessage> capturedMessages = captor.getAllValues();
        int count = 0;
        for (TextMessage message : capturedMessages) {
            count++;
            DrawCardMessage responseDrawCardMessage = gson.fromJson(message.getPayload(), DrawCardMessage.class);

            assertNotNull(responseDrawCardMessage);
            assertEquals(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_OK, responseDrawCardMessage.getActionTypeDrawCard());
            assertNotNull(responseDrawCardMessage.getCard());
            assertNotNull(responseDrawCardMessage.getNextPlayerId());
            assertEquals(playerId1, responseDrawCardMessage.getPlayerID());
            assertEquals(playerId2, responseDrawCardMessage.getNextPlayerId());
        }
        assertEquals(2, count);

    }

    @Test
    public void testValidDrawCardRandom_CARROT() throws Exception {

        Room roomToFind = inMemoryRoomRepoTest.findRoomByName("TestRoom");
        String roomId = roomToFind.getRoomID();
        drawCardMessage.setRoomID(roomId);
        drawCardMessage.setPlayerID(playerId1);
        drawCardMessage.setCard("CARROT");
        String payload = gson.toJson(drawCardMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerDrawCard.handleDrawCard(session1, payload, sessions, inMemoryRoomRepoTest);

        for (WebSocketSession session : sessions) {
            verify(session, times(1)).sendMessage(captor.capture());
        }

        List<TextMessage> capturedMessages = captor.getAllValues();
        int count = 0;
        for (TextMessage message : capturedMessages) {
            count++;
            DrawCardMessage responseDrawCardMessage = gson.fromJson(message.getPayload(), DrawCardMessage.class);

            assertNotNull(responseDrawCardMessage);
            assertEquals(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_OK, responseDrawCardMessage.getActionTypeDrawCard());
            assertNotNull(responseDrawCardMessage.getCard());
            assertNotNull(responseDrawCardMessage.getNextPlayerId());
            assertEquals(playerId1, responseDrawCardMessage.getPlayerID());
            assertEquals(playerId2, responseDrawCardMessage.getNextPlayerId());
        }
        assertEquals(2, count);

    }

    @Test
    public void testDrawCardMsg_ERR () throws Exception{

        String payload = gson.toJson(null);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerDrawCard.handleDrawCard(session1, payload, sessions, inMemoryRoomRepoTest);

        for (WebSocketSession session : sessions) {
            verify(session, times(1)).sendMessage(captor.capture());
        }

        List<TextMessage> capturedMessages = captor.getAllValues();
        int count = 0;
        for (TextMessage message : capturedMessages) {
            count++;
            DrawCardMessage responseDrawCardMessage = gson.fromJson(message.getPayload(), DrawCardMessage.class);

            assertNotNull(responseDrawCardMessage);
            assertEquals(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_ERR, responseDrawCardMessage.getActionTypeDrawCard());
            assertNotNull(responseDrawCardMessage.getCard());

        }
        assertEquals(2, count);
    }


    @Test
    public void sessionIsNULL () {
        String payload = gson.toJson(drawCardMessage);
        assertThrows(NullPointerException.class, () -> HandlerDrawCard.handleDrawCard(null, payload, sessions, inMemoryRoomRepoTest));
    }

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerDrawCard.handleDrawCard(session1, null, sessions, inMemoryRoomRepoTest));
    }

    @Test
    public void testJsonErrMsg () {
        String payload = "{error}";
        assertThrows(JsonParseException.class, () -> HandlerDrawCard.handleDrawCard(session1, payload, sessions, inMemoryRoomRepoTest));
    }

    @AfterEach
    public void tearDown() {
        session1 = null;
        session2 = null;
        sessions = null;
        drawCardMessage = null;

    }



}
