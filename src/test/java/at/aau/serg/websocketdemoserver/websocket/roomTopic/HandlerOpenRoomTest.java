package at.aau.serg.websocketdemoserver.websocket.roomTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.OpenRoomMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerOpenRoom;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HandlerOpenRoomTest {

    private static final Gson gson = new Gson();

    private WebSocketSession session;

    private OpenRoomMessage openRoomMessage;

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
        openRoomMessage = new OpenRoomMessage();
    }

    @Test
    public void testValidOpenRoom() throws Exception {
        OpenRoomMessage openRoomMessage = new OpenRoomMessage();
        openRoomMessage.setRoomName("roomDani");
        openRoomMessage.setPlayerName("Daniel");
        openRoomMessage.setNumPlayers("2");
        String payload = TransportUtils.helpToJson(openRoomMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerOpenRoom.handleOpenRoomMessage(session, payload, inMemoryRoomRepoTest);

        verify(session, times(1)).sendMessage(captor.capture());
        TextMessage responseMessage = captor.getValue();
        OpenRoomMessage responseOpenRoomMessage = TransportUtils.helpFromJson(responseMessage.getPayload(), OpenRoomMessage.class);

        assertNotNull(responseOpenRoomMessage);
        assertEquals(OpenRoomMessage.OpenRoomActionType.OPEN_ROOM_OK, responseOpenRoomMessage.getOpenRoomActionType());
        assertNotNull(responseOpenRoomMessage.getRoomId());
        assertEquals("roomDani", responseOpenRoomMessage.getRoomName());
        assertEquals("Daniel", responseOpenRoomMessage.getPlayerName());
        assertEquals("2", responseOpenRoomMessage.getNumPlayers());
    }


    @Test
    public void sessionIsNULL () {
        //handleOpenRoomMsg_ShouldThrowNullPointerExeptionIfSessionIsNull
        String payload = gson.toJson(openRoomMessage);
        assertThrows(NullPointerException.class, () -> HandlerOpenRoom.handleOpenRoomMessage(null, payload, inMemoryRoomRepoTest));
    }

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerOpenRoom.handleOpenRoomMessage(session, null, inMemoryRoomRepoTest));
    }

    @Test
    public void testJsonErrMsg () {
        String payload = "{error}";
        assertThrows(JsonParseException.class, () -> HandlerOpenRoom.handleOpenRoomMessage(session, payload, inMemoryRoomRepoTest));
    }

    @AfterEach
    public void tearDown () {
        session = null;
        openRoomMessage = null;

    }




}
