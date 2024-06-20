package at.aau.serg.websocketdemoserver.websocket.gameboardTopic;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.game.PlayingPiece;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.MoveMessageImpl;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerChatMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerMoveMessage;
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

public class HandlerMoveMessageTest {
    @Mock
    private WebSocketSession session1;

    @Mock
    private WebSocketSession session2;

    private static final Gson gson = new Gson();
    private List<WebSocketSession> sessions;
    private MoveMessageImpl moveMessage;
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

        moveMessage = new MoveMessageImpl();
        initTestRooms();
        sessions = new ArrayList<>();
        sessions.add(session1);
        sessions.add(session2);
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);
    }

    @Test
    public void testHandleMoveMessage_CarrotCard() throws Exception {
        // Arrange
        gameboard = new Gameboard();
        moveMessage.setCard("CARROT");
        moveMessage.setFields(gameboard.getFelder());
        String jsonMessageMove = gson.toJson(moveMessage);

        // Act
        HandlerMoveMessage.handleMoveMessage(session1, jsonMessageMove, sessions, inMemoryRoomRepoTest);

        // Verify that the message was sent to both sessions
        verify(session1, times(1)).sendMessage(any(TextMessage.class));
        verify(session2, times(1)).sendMessage(any(TextMessage.class));

        // Verify that the message is correct
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session1, times(1)).sendMessage(captor.capture());
        TextMessage message1 = captor.getValue();
        verify(session2, times(1)).sendMessage(captor.capture());
        TextMessage message2 = captor.getValue();

        MoveMessageImpl responseMoveMessage1 = gson.fromJson(message1.getPayload(), MoveMessageImpl.class);
        MoveMessageImpl responseMoveMessage2 = gson.fromJson(message2.getPayload(), MoveMessageImpl.class);

        assertNotNull(responseMoveMessage1);
        assertEquals("CARROT", responseMoveMessage1.getCard());

        assertNotNull(responseMoveMessage2);
        assertEquals("CARROT", responseMoveMessage2.getCard());

    }

    @Test
    public void testHandleMoveMessage_InsertFigure() throws Exception {
        // Arrange
        PlayingPiece playingPiece = new PlayingPiece(1, "123456");
        gameboard = new Gameboard();
        moveMessage.setCard("1");
        moveMessage.setPlayingPiece(playingPiece);
        moveMessage.setSpielerId("123456");
        moveMessage.setFields(gameboard.getFelder());
        String jsonMessageMove = gson.toJson(moveMessage);

        // Act
        HandlerMoveMessage.handleMoveMessage(session1, jsonMessageMove, sessions, inMemoryRoomRepoTest);
        // Verify that the message was sent to both sessions
        verify(session1, times(1)).sendMessage(any(TextMessage.class));
        verify(session2, times(1)).sendMessage(any(TextMessage.class));

        // Verify that the message is correct
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session1, times(1)).sendMessage(captor.capture());
        TextMessage message1 = captor.getValue();
        verify(session2, times(1)).sendMessage(captor.capture());
        TextMessage message2 = captor.getValue();

        MoveMessageImpl responseMoveMessage1 = gson.fromJson(message1.getPayload(), MoveMessageImpl.class);
        MoveMessageImpl responseMoveMessage2 = gson.fromJson(message2.getPayload(), MoveMessageImpl.class);
        PlayingPiece expectedPiece = responseMoveMessage1.getFields()[0].getPlayingPiece();
        PlayingPiece expectedPiece2 = responseMoveMessage2.getFields()[0].getPlayingPiece();

        assertNotNull(responseMoveMessage1);
        assertEquals("1", responseMoveMessage1.getCard());
        assertEquals(playingPiece, expectedPiece);

        assertNotNull(responseMoveMessage2);
        assertEquals("1", responseMoveMessage2.getCard());
        assertEquals(playingPiece, expectedPiece2);;
    }

    @Test
    public void testHandleMoveMessage_MoveFigureForward() throws Exception {
        // Arrange
        PlayingPiece playingPiece = new PlayingPiece(1, "123456");
        gameboard = new Gameboard();
        moveMessage.setCard("2");
        moveMessage.setPlayingPiece(playingPiece);
        moveMessage.setSpielerId("123456");
        moveMessage.setFields(gameboard.getFelder());
        String jsonMessageMove = gson.toJson(moveMessage);

        // Act
        HandlerMoveMessage.handleMoveMessage(session1, jsonMessageMove, sessions, inMemoryRoomRepoTest);

        // Verify that the message was sent to both sessions
        verify(session1, times(1)).sendMessage(any(TextMessage.class));
        verify(session2, times(1)).sendMessage(any(TextMessage.class));

        // Verify that the message is correct
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session1, times(1)).sendMessage(captor.capture());
        TextMessage message1 = captor.getValue();
        verify(session2, times(1)).sendMessage(captor.capture());
        TextMessage message2 = captor.getValue();

        MoveMessageImpl responseMoveMessage1 = gson.fromJson(message1.getPayload(), MoveMessageImpl.class);
        MoveMessageImpl responseMoveMessage2 = gson.fromJson(message2.getPayload(), MoveMessageImpl.class);
        PlayingPiece expectedPiece = responseMoveMessage1.getFields()[1].getPlayingPiece();
        PlayingPiece expectedPiece2 = responseMoveMessage2.getFields()[1].getPlayingPiece();

        assertNotNull(responseMoveMessage1);
        assertEquals("2", responseMoveMessage1.getCard());
        assertEquals(playingPiece, expectedPiece);

        assertNotNull(responseMoveMessage2);
        assertEquals("2", responseMoveMessage2.getCard());
        assertEquals(playingPiece, expectedPiece2);
    }

    @Test
    public void sessionIsNULL () {
        String payload = gson.toJson(moveMessage);
        assertThrows(NullPointerException.class, () -> HandlerMoveMessage.handleMoveMessage(null, payload, sessions, inMemoryRoomRepoTest));
    }

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerMoveMessage.handleMoveMessage(session1, null, sessions, inMemoryRoomRepoTest));
    }

    @AfterEach
    public void tearDown() {
        session1 = null;
        session2 = null;
        sessions = null;
        moveMessage = null;

    }
}
