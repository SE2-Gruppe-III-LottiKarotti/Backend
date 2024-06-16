package at.aau.serg.websocketdemoserver.websocket.roomTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;
import at.aau.serg.websocketdemoserver.msg.ChatMessage;
import at.aau.serg.websocketdemoserver.msg.JoinRoomMessage;
import at.aau.serg.websocketdemoserver.msg.RoomListMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerJoinRoom;
import at.aau.serg.websocketdemoserver.websocket.handler.roomTopic.HandlerRoomList;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HandlerRoomListTest {

    @Mock
    private WebSocketSession session1;

    @Mock
    private WebSocketSession session2;




    private static final Gson gson = new Gson();
    private RoomListMessage roomListMessage;
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
    public void setup() {
        session1 = mock(WebSocketSession.class);
        roomListMessage = new RoomListMessage();
        //initialize the default rooms
        initTestRooms();
    }


    /*@Test
    public void sessionIsNULL () {
        String payload = gson.toJson(roomListMessage);
        assertThrows(NullPointerException.class, () -> HandlerJoinRoom.handleAskForJoinRoom(null, payload, inMemoryRoomRepoTest));
    }*/

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerJoinRoom.handleAskForJoinRoom(session1, null, inMemoryRoomRepoTest));
    }

    @Test
    public void testJsonErrMsg () {
        String payload = "{error}";
        assertThrows(JsonParseException.class, () -> HandlerJoinRoom.handleAskForJoinRoom(session1, payload, inMemoryRoomRepoTest));
    }

    @AfterEach
    public void tearDown() {
        session1 = null;
        roomListMessage = null;

    }

    /*
    @Test
    public void handleAskForRoomList_withRooms() throws Exception {
        Room testRoom1 = new Room(2, "TestTest");
        testRoom1.setCreatorName("TestCreator");
        inMemoryRoomRepoTest.addRoom(testRoom1);

        //String payload = "{\"messageType\":\"LIST_ROOMS\",\"actionTypeRoomListMessage\":\"ASK_FOR_ROOM_LIST\"}";
        RoomListMessage roomListMessage = new RoomListMessage();
        roomListMessage.setActionTypeRoomListMessage(RoomListMessage.ActionTypeRoomListMessage.ASK_FOR_ROOM_LIST);
        String payload = TransportUtils.helpToJson(roomListMessage);

        HandlerRoomList.handleAskForRoomList(session1, payload, inMemoryRoomRepoTest);

        verify(session1).sendMessage(any(TextMessage.class));

        // Verify that the response contains the correct room info
        ArgumentCaptor<TextMessage> argument = ArgumentCaptor.forClass(TextMessage.class);
        verify(session1).sendMessage(argument.capture());
        TextMessage messageSent = argument.getValue();
        RoomListMessage response = new Gson().fromJson(messageSent.getPayload(), RoomListMessage.class);

        assertEquals(1, response.getRoomInfoArrayList().size());
        RoomInfo roomInfo = response.getRoomInfoArrayList().get(0);
        assertEquals("TestTest", roomInfo.getRoomName());
        assertEquals("TestCreator", roomInfo.getCreator());
    }*/
}
