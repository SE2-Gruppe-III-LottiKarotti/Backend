package at.aau.serg.websocketdemoserver.websocket.handler.defaults;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.msg.HeartbeatMessage;
import org.springframework.web.socket.WebSocketSession;



public class HandlerHeartbeat {

    public static void handleHeartbeat(WebSocketSession session, String payload) {
        TransportUtils.validateSessionAndPayload(session, payload);

        HeartbeatMessage heartbeatMessage = TransportUtils.helpFromJson(payload, HeartbeatMessage.class);


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
