package at.aau.serg.websocketdemoserver.websocket.defaults;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.msg.MessageType;
import at.aau.serg.websocketdemoserver.msg.TestMessageImpl;
import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerHeartbeat;
import at.aau.serg.websocketdemoserver.websocket.handler.defaults.HandlerTestMessage;
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

public class HandlerTestMessageTest {

    private WebSocketSession session;
    private TestMessageImpl testMessage;
    private static final Gson gson = new Gson();


    @BeforeEach
    public void setup() {
        session = mock(WebSocketSession.class);
        testMessage = new TestMessageImpl();

    }

    @Test
    public void testValidMsg () throws Exception {
        testMessage.setText("test message");
        testMessage.setMessageIdentifier("validIdentifier");
        String payload = TransportUtils.helpToJson(testMessage);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

        HandlerTestMessage.handleTestMessage(session, payload);

        verify(session, times(1)).sendMessage(captor.capture());
        TextMessage responseMessage = captor.getValue();
        TestMessageImpl responseTestMessage = TransportUtils.helpFromJson(responseMessage.getPayload(), TestMessageImpl.class);

        assertNotNull(responseTestMessage);
        assertEquals("juhu", responseTestMessage.getText());
        assertEquals(MessageType.TEST, responseTestMessage.getMessageType());
        assertEquals("validIdentifier", responseTestMessage.getMessageIdentifier());
    }

    @Test
    public void testInvalidMsg () {
        String payload = "{error}";
        assertThrows(JsonParseException.class, () -> HandlerTestMessage.handleTestMessage(session, payload));
    }

    @Test
    public void sessionIsNULL () {
        String payload = gson.toJson(testMessage);
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
        testMessage = null;

    }



}
