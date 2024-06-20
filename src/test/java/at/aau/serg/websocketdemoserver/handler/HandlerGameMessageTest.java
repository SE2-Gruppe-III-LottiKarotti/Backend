package at.aau.serg.websocketdemoserver.handler;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.GameMessage;
import at.aau.serg.websocketdemoserver.msg.MessageType;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerGameMessage;
import com.google.gson.Gson;
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

class HandlerGameMessageTest {
    @Mock
    private WebSocketSession session1;

    @Mock
    private WebSocketSession session2;

    private static final Gson gson = new Gson();
    private List<WebSocketSession> sessions;
    private GameMessage gameMessage;
    private final InMemoryRoomRepo inMemoryRoomRepoTest = new InMemoryRoomRepo();


    void initTestRooms() {
        String playerName = "FranzSissi";
        String playerName2 = "Daniel";
        Player player1 = new Player(playerName);
        Player player2 = new Player(playerName2);
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
    void setup () {
        MockitoAnnotations.openMocks(this);

        gameMessage = new GameMessage();
        initTestRooms();
        sessions = new ArrayList<>();
        sessions.add(session1);
        sessions.add(session2);
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);
    }

    @Test
    void testHandleGameMessage() throws Exception {
        // Convert the GameMessage object to JSON
        String jsonMessage = new Gson().toJson(gameMessage);

        // Call the method under test
        HandlerGameMessage.handleGameMessage(session1, jsonMessage, sessions, inMemoryRoomRepoTest);

        // Verify that the message was sent to both sessions
        verify(session1, times(1)).sendMessage(any(TextMessage.class));
        verify(session2, times(1)).sendMessage(any(TextMessage.class));

        // Verify that the message is correct
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session1, times(1)).sendMessage(captor.capture());
        TextMessage message1 = captor.getValue();
        verify(session2, times(1)).sendMessage(captor.capture());
        TextMessage message2 = captor.getValue();

        GameMessage responseGameMessage1 = gson.fromJson(message1.getPayload(), GameMessage.class);
        GameMessage responseGameMessage2 = gson.fromJson(message2.getPayload(), GameMessage.class);

        assertNotNull(responseGameMessage1);
        assertEquals(MessageType.GAME, responseGameMessage1.getMessageType());
        assertNotNull(responseGameMessage1.getFields());

        assertNotNull(responseGameMessage2);
        assertEquals(MessageType.GAME, responseGameMessage2.getMessageType());
        assertNotNull(responseGameMessage2.getFields());
    }

    @Test
    void sessionIsNULL () {
        String payload = gson.toJson(gameMessage);
        assertThrows(NullPointerException.class, () -> HandlerGameMessage.handleGameMessage(null, payload, sessions, inMemoryRoomRepoTest));
    }

    @Test
    void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerGameMessage.handleGameMessage(session1, null, sessions, inMemoryRoomRepoTest));
    }

    @AfterEach
    void tearDown() {
        session1 = null;
        session2 = null;
        sessions = null;
        gameMessage = null;

    }

}
