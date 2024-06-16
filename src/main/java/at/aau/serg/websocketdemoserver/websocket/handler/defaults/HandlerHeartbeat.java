package at.aau.serg.websocketdemoserver.websocket.handler.defaults;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.msg.HeartbeatMessageImpl;
import org.springframework.web.socket.WebSocketSession;



public class HandlerHeartbeat {

    public static void handleHeartbeat(WebSocketSession session, String payload) {
        TransportUtils.validateSessionAndPayload(session, payload);

        HeartbeatMessageImpl heartbeatMessage = TransportUtils.helpFromJson(payload, HeartbeatMessageImpl.class);


        TransportUtils.nullCheck(heartbeatMessage);

        if (heartbeatMessage.getText().equals("ping")) {
            heartbeatMessage.setText("pong");
            String response = TransportUtils.helpToJson(heartbeatMessage);

            TransportUtils.sendMsg(session, response);
        }
        else {
            throw new IllegalArgumentException("invalid heartbeat msg");
        }

    }
}
