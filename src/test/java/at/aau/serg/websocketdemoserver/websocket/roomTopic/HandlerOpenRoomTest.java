package at.aau.serg.websocketdemoserver.websocket.roomTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.msg.OpenRoomMessageImpl;
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

    private OpenRoomMessageImpl openRoomMessage;

    @BeforeEach
    public void setup() {
        session = mock(WebSocketSession.class);
        openRoomMessage = new OpenRoomMessageImpl();
    }

    @Test
    public void testValidOpenRoom() throws Exception {
        OpenRoomMessageImpl openRoomMessage = new OpenRoomMessageImpl();
        openRoomMessage.setRoomName("roomDani");
        openRoomMessage.setPlayerName("Daniel");
        openRoomMessage.setNumPlayers("2");
        String payload = TransportUtils.helpToJson(openRoomMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerOpenRoom.handleOpenRoomMessage(session, payload);

        verify(session, times(1)).sendMessage(captor.capture());
        TextMessage responseMessage = captor.getValue();
        OpenRoomMessageImpl responseOpenRoomMessage = TransportUtils.helpFromJson(responseMessage.getPayload(), OpenRoomMessageImpl.class);

        assertNotNull(responseOpenRoomMessage);
        assertEquals(OpenRoomMessageImpl.OpenRoomActionType.OPEN_ROOM_OK, responseOpenRoomMessage.getOpenRoomActionType());
        assertNotNull(responseOpenRoomMessage.getRoomId());
        assertEquals("roomDani", responseOpenRoomMessage.getRoomName());
        assertEquals("Daniel", responseOpenRoomMessage.getPlayerName());
        assertEquals("2", responseOpenRoomMessage.getNumPlayers());
    }


    @Test
    public void sessionIsNULL () {
        String payload = gson.toJson(openRoomMessage);
        assertThrows(NullPointerException.class, () -> HandlerOpenRoom.handleOpenRoomMessage(null, payload));
    }

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerOpenRoom.handleOpenRoomMessage(session, null));
    }

    @Test
    public void testJsonErrMsg () {
        String payload = "{error}";
        assertThrows(JsonParseException.class, () -> HandlerOpenRoom.handleOpenRoomMessage(session, payload));
    }

    @AfterEach
    public void tearDown () {
        session = null;
        openRoomMessage = null;

    }




}
