package at.aau.serg.websocketdemoserver.websocket.gameboardTopic;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.GameMessageImpl;
import at.aau.serg.websocketdemoserver.msg.MessageType;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerGameMessage;
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

import static at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerGameMessage.gameboard;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class HandlerGameMessageTest {

    @Mock
    private WebSocketSession session1;

    @Mock
    private WebSocketSession session2;

    private static final Gson gson = new Gson();
    private List<WebSocketSession> sessions;
    private GameMessageImpl gameMessage;
    private final InMemoryRoomRepo inMemoryRoomRepoTest = new InMemoryRoomRepo();


    private void initTestRooms() {
        String playerName = "FranzSissi";
        String playerName2 = "Daniel";
        Player player1 = new Player(playerName);
        Player player2 = new Player(playerName2);
        String playerId1 = player1.getPlayerID();
        String playerId2 = player2.getPlayerID();
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

        //add
        inMemoryRoomRepoTest.addRoom(testRoom1);
        inMemoryRoomRepoTest.addRoom(testRoom2);
        inMemoryRoomRepoTest.addRoom(testRoom3);
    }

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);

        gameMessage = new GameMessageImpl();
        initTestRooms();
        sessions = new ArrayList<>();
        sessions.add(session1);
        sessions.add(session2);
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);
    }

    @Test
    public void testHandleGameMessage() throws Exception {
        // Convert the GameMessage object to JSON
        String jsonMessage = new Gson().toJson(gameMessage);

        // Call the method under test
        HandlerGameMessage.handleGameMessage(session1, jsonMessage, sessions, inMemoryRoomRepoTest);

        // Verify that the game board was initialized
        assertNotNull(gameboard);

        // Verify that the message was sent to both sessions
        verify(session1, times(1)).sendMessage(any(TextMessage.class));
        verify(session2, times(1)).sendMessage(any(TextMessage.class));

        // Verify that the message is correct
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session1, times(1)).sendMessage(captor.capture());
        TextMessage message1 = captor.getValue();
        verify(session2, times(1)).sendMessage(captor.capture());
        TextMessage message2 = captor.getValue();

        GameMessageImpl responseGameMessage1 = gson.fromJson(message1.getPayload(), GameMessageImpl.class);
        GameMessageImpl responseGameMessage2 = gson.fromJson(message2.getPayload(), GameMessageImpl.class);

        assertNotNull(responseGameMessage1);
        assertEquals(MessageType.GAME, responseGameMessage1.getMessageType());

        assertNotNull(responseGameMessage2);
        assertEquals(MessageType.GAME, responseGameMessage2.getMessageType());
    }

    @Test
    public void sessionIsNULL () {
        String payload = gson.toJson(gameMessage);
        assertThrows(NullPointerException.class, () -> HandlerGameMessage.handleGameMessage(null, payload, sessions, inMemoryRoomRepoTest));
    }

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerGameMessage.handleGameMessage(session1, null, sessions, inMemoryRoomRepoTest));
    }

    @AfterEach
    public void tearDown() {
        session1 = null;
        session2 = null;
        sessions = null;
        gameMessage = null;

    }
}
