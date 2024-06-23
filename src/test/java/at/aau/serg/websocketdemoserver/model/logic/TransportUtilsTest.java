package at.aau.serg.websocketdemoserver.model.logic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransportUtilsTest {

    @Test
    public void testValidateSessionAndPayload_NullSession() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            TransportUtils.validateSessionAndPayload(null, "payload");
        });
        assertEquals("session == null", exception.getMessage());
    }

    @Test
    public void testValidateSessionAndPayload_NullPayload() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            TransportUtils.validateSessionAndPayload(Mockito.mock(WebSocketSession.class), null);
        });
        assertEquals("payload == null", exception.getMessage());
    }

    @Test
    public void testHelpFromJson_ValidJson() {
        String json = "{\"name\":\"test\"}";
        TestObject obj = TransportUtils.helpFromJson(json, TestObject.class);
        assertNotNull(obj);
        assertEquals("test", obj.name);
    }

    @Test
    public void testHelpFromJson_InvalidJson() {
        String json = "invalid json";
        JsonParseException exception = assertThrows(JsonParseException.class, () -> {
            TransportUtils.helpFromJson(json, TestObject.class);
        });
        assertEquals("json parse error", exception.getMessage());
    }

    @Test
    public void testHelpToJson() {
        TestObject obj = new TestObject();
        obj.name = "test";
        String json = TransportUtils.helpToJson(obj);
        assertEquals("{\"name\":\"test\"}", json);
    }

    @Test
    public void testSendMsg() throws IOException {
        WebSocketSession session = Mockito.mock(WebSocketSession.class);
        when(session.isOpen()).thenReturn(true);
        doNothing().when(session).sendMessage(any(TextMessage.class));

        TransportUtils.sendMsg(session, "test message");

        verify(session, times(1)).sendMessage(new TextMessage("test message"));
    }

    @Test
    public void testBroadcastMsg() throws IOException {
        WebSocketSession session1 = Mockito.mock(WebSocketSession.class);
        WebSocketSession session2 = Mockito.mock(WebSocketSession.class);

        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(false);

        doNothing().when(session1).sendMessage(any(TextMessage.class));

        List<WebSocketSession> sessions = List.of(session1, session2);

        TransportUtils.broadcastMsg("test message", session1, sessions);

        verify(session1, times(1)).sendMessage(new TextMessage("test message"));
        verify(session2, never()).sendMessage(any(TextMessage.class));
    }

    @Test
    public void testBroadcastMsg_NullSessions() {
        Logger logger = Mockito.mock(Logger.class);
        TransportUtils.logger = logger;

        TransportUtils.broadcastMsg("test message", Mockito.mock(WebSocketSession.class), null);

        verify(logger, times(1)).warning("error: list is null");
    }

    @Test
    public void testNullCheck() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            TransportUtils.nullCheck(null);
        });
        assertEquals("msgObject == null", exception.getMessage());
    }

    static class TestObject {
        String name;
    }
}
