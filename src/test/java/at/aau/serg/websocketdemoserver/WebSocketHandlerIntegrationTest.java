package at.aau.serg.websocketdemoserver;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.msg.TestMessage;
import at.aau.serg.websocketdemoserver.websocket.WebSocketHandlerClientImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketHandlerIntegrationTest {

    @LocalServerPort
    private int port;

    private final String WEBSOCKET_URI = "ws://localhost:%d/websocket-example-handler";

    /**
     * Queue of messages from the server.
     */
    BlockingQueue<String> messages = new LinkedBlockingDeque<>();

    @Test
    public void testWebSocketMessageBroker() throws Exception {
        WebSocketSession session = initStompSession();

        // send a message to the server
        TestMessage testMessage = new TestMessage();
        testMessage.setText("Test message");
        String message = TransportUtils.helpToJson(testMessage);
        session.sendMessage(new TextMessage(message));
        testMessage.setText("juhu");
        message = TransportUtils.helpToJson(testMessage);



        var expectedResponse = message;
        assertThat(messages.poll(1, TimeUnit.SECONDS)).isEqualTo(expectedResponse);
    }

    /**
     * @return The basic session for the WebSocket connection.
     */
    public WebSocketSession initStompSession() throws Exception {
        WebSocketClient client = new StandardWebSocketClient();

        // connect client to the websocket server
        WebSocketSession session = client.execute(new WebSocketHandlerClientImpl(messages), // pass the message list
                        String.format(WEBSOCKET_URI, port))
                // wait 1 sec for the client to be connected
                .get(1, TimeUnit.SECONDS);

        return session;
    }

}
