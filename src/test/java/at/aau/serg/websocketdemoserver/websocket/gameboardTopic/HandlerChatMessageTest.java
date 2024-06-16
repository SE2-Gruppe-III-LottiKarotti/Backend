package at.aau.serg.websocketdemoserver.websocket.gameboardTopic;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.ChatMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic.HandlerChatMessage;
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

public class HandlerChatMessageTest {

    @Mock
    private WebSocketSession session1;

    @Mock
    private WebSocketSession session2;




    private static final Gson gson = new Gson();
    private List<WebSocketSession> sessions;
    private ChatMessage chatMessage;
    private final InMemoryRoomRepo inMemoryRoomRepoTest = new InMemoryRoomRepo();

    private String playerId1;
    //private String playerId2;

    private void initTestRooms() {
        String playerName1 = "FranzSissi";
        String playerName2 = "Daniel";
        Player player1 = new Player(playerName1);
        Player player2 = new Player(playerName2);
        playerId1 = player1.getPlayerID();
        //playerId2 = player2.getPlayerID();
        Room testRoom1 = new Room(2, "TestRoom");
        testRoom1.setCreatorName(playerName1);
        testRoom1.addPlayer(player1);
        testRoom1.addPlayer(player2);
        Room testRoom2 = new Room(3, "TestRo2");
        testRoom2.setCreatorName(playerName1);
        testRoom2.addPlayer(player1);
        Room testRoom3 = new Room(2, "TestRo3");
        testRoom3.setCreatorName(playerName1);
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

        chatMessage = new ChatMessage();
        initTestRooms();
        sessions = new ArrayList<>();
        sessions.add(session1);
        sessions.add(session2);
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);

    }

    @Test
    public void testValidChatMsg() throws Exception {

        Room roomToFind = inMemoryRoomRepoTest.findRoomByName("TestRoom");
        String roomId = roomToFind.getRoomID();

        chatMessage.setRoomID(roomId);
        chatMessage.setPlayerId(playerId1);
        chatMessage.setPlayerName("FranzSissi");
        chatMessage.setText("testMessage");
        String payload = gson.toJson(chatMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerChatMessage.handleChatMessage(session1, payload, sessions);

        for (WebSocketSession session : sessions) {
            verify(session, times(1)).sendMessage(captor.capture());
        }

        List<TextMessage> capturedMessages = captor.getAllValues();
        int count = 0;
        for (TextMessage message : capturedMessages) {
            count++;
            ChatMessage responseChatMessage = gson.fromJson(message.getPayload(), ChatMessage.class);

            assertNotNull(responseChatMessage);
            assertEquals(ChatMessage.ActionTypeChat.CHAT_MSG_TO_CLIENTS_OK, responseChatMessage.getActionTypeChat());
            assertEquals(playerId1, responseChatMessage.getPlayerId());
            assertEquals("FranzSissi", responseChatMessage.getPlayerName());
            assertEquals("testMessage", responseChatMessage.getText());

            assertEquals(roomId, responseChatMessage.getRoomID());
            assertEquals(playerId1, chatMessage.getPlayerId());
        }
        assertEquals(2, count);

    }

    @Test
    public void testValidChatMsgWithCensor() throws Exception {

        Room roomToFind = inMemoryRoomRepoTest.findRoomByName("TestRoom");
        String roomId = roomToFind.getRoomID();

        chatMessage.setRoomID(roomId);
        chatMessage.setPlayerId(playerId1);
        chatMessage.setPlayerName("FranzSissi");
        chatMessage.setText("Kurva testMessage");
        String payload = gson.toJson(chatMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerChatMessage.handleChatMessage(session1, payload, sessions);

        for (WebSocketSession session : sessions) {
            verify(session, times(1)).sendMessage(captor.capture());
        }

        List<TextMessage> capturedMessages = captor.getAllValues();
        int count = 0;
        for (TextMessage message : capturedMessages) {
            count++;
            ChatMessage responseChatMessage = gson.fromJson(message.getPayload(), ChatMessage.class);

            assertNotNull(responseChatMessage);
            assertEquals(ChatMessage.ActionTypeChat.CHAT_MSG_TO_CLIENTS_OK, responseChatMessage.getActionTypeChat());
            assertEquals(playerId1, responseChatMessage.getPlayerId());
            assertEquals("FranzSissi", responseChatMessage.getPlayerName());
            assertEquals("***** testMessage", responseChatMessage.getText());

            assertEquals(roomId, responseChatMessage.getRoomID());
            assertEquals(playerId1, chatMessage.getPlayerId());
        }
        assertEquals(2, count);

    }



    @Test
    public void testChatMsg_ERR() throws Exception {
        // Simulate an invalid payload by using an empty string or a broken JSON
        String invalidPayload = "{}";

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerChatMessage.handleChatMessage(session1, invalidPayload, sessions);

        // Verify that the session1 sends a message
        verify(session1, times(1)).sendMessage(captor.capture());

        TextMessage capturedMessage = captor.getValue();
        ChatMessage responseChatMessage = gson.fromJson(capturedMessage.getPayload(), ChatMessage.class);

        assertNotNull(responseChatMessage);
        assertEquals(ChatMessage.ActionTypeChat.CHAT_MSG_TO_CLIENTS_ERR, responseChatMessage.getActionTypeChat());
    }


    @Test
    public void sessionIsNULL () {
        String payload = gson.toJson(chatMessage);
        assertThrows(NullPointerException.class, () -> HandlerChatMessage.handleChatMessage(null, payload, sessions));
    }

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerChatMessage.handleChatMessage(session1, null, sessions));
    }

    @Test
    public void testJsonErrMsg () {
        String payload = "{error}";
        assertThrows(JsonParseException.class, () -> HandlerChatMessage.handleChatMessage(session1, payload, sessions));
    }

    @AfterEach
    public void tearDown() {
        session1 = null;
        session2 = null;
        sessions = null;
        chatMessage = null;

    }

}
