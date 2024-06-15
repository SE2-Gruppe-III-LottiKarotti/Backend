package at.aau.serg.websocketdemoserver.websocket.handler.defaults;

import at.aau.serg.websocketdemoserver.msg.HeartbeatMessage;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;



public class HandlerHeartbeat {

    private final static Gson gson = new Gson();
    public static void handleHeartbeat(WebSocketSession session, String payload) {
        if (session == null) {
            throw new IllegalArgumentException("session == null");
        }
        if (payload == null) {
            throw new IllegalArgumentException("payload == null");
        }
        HeartbeatMessage heartbeatMessage;
        try {
            heartbeatMessage = gson.fromJson(payload, HeartbeatMessage.class);
        } catch (JsonSyntaxException e) {
            throw new JsonIOException("json error", e);
        }

        if (heartbeatMessage == null || heartbeatMessage.getText() == null) {
            throw new IllegalArgumentException("invalid message");
        }

        if (heartbeatMessage.getText().equals("ping")) {
            heartbeatMessage.setText("pong");
            String response = gson.toJson(heartbeatMessage);
            try {
                session.sendMessage(new TextMessage(response));
            } catch (Exception e) {
                throw new RuntimeException("failed send process", e);
            }
        }
        else {
            throw new IllegalArgumentException("invalid heartbeat msg");
        }

    }
}
