package at.aau.serg.websocketdemoserver.websocket.defaults;


import at.aau.serg.websocketdemoserver.msg.HeartbeatMessage;
import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerHeartbeat;
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

public class HandlerHeartbeatTest {
    private static final Gson gson = new Gson();
    private WebSocketSession session;
    private HeartbeatMessage heartbeatMessage;

    @BeforeEach
    public void setup() {
        session = mock(WebSocketSession.class);
        heartbeatMessage = new HeartbeatMessage();
    }

    @Test
    public void testValid () throws Exception {
        heartbeatMessage.setText("ping");
        String payload = gson.toJson(heartbeatMessage);
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerHeartbeat.handleHeartbeat(session, payload);

        verify(session, times(1)).sendMessage(captor.capture());
        TextMessage responseMessage = captor.getValue();
        HeartbeatMessage responseHeartbeatMessage = gson.fromJson(responseMessage.getPayload(), HeartbeatMessage.class);

        assertNotNull(responseHeartbeatMessage);
        assertEquals("pong", responseHeartbeatMessage.getText());

    }

    @Test
    public void testNotValidPayload () {
        heartbeatMessage.setText("notPing");
        String notValidPayload = gson.toJson(heartbeatMessage);
        assertThrows(IllegalArgumentException.class, () -> HandlerHeartbeat.handleHeartbeat(session, notValidPayload));
    }

    @Test
    public void sessionIsNULL () {
        String payload = gson.toJson(heartbeatMessage);
        assertThrows(NullPointerException.class, () -> HandlerHeartbeat.handleHeartbeat(null, payload));
    }

    @Test
    public void payloadIsNULL () {
        assertThrows(NullPointerException.class, () -> HandlerHeartbeat.handleHeartbeat(session, null));
    }

    @Test
    public void testJsonErrMsg () {
        String payload = "{error}";
        assertThrows(JsonParseException.class, () -> HandlerHeartbeat.handleHeartbeat(session, payload));
    }

    @AfterEach
    public void tearDown () {
        session = null;
        heartbeatMessage = null;

    }




}
