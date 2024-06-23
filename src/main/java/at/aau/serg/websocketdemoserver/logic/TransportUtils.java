package at.aau.serg.websocketdemoserver.logic;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.logging.Logger;

public class TransportUtils {

    public static Logger logger = Logger.getLogger(TransportUtils.class.getName());
    private static final Gson gson = new Gson();


    public static void validateSessionAndPayload(Object session, String payload) {
        if (session == null) {
            throw new NullPointerException("session == null");
        }
        if (payload == null) {
            throw new NullPointerException("payload == null");
        }
    }


    public static <T> T helpFromJson(String payload, Class<T> classOfT) {
        try {
            return gson.fromJson(payload, classOfT);
        } catch (JsonParseException f) {
            throw new JsonParseException("json parse error", f);
        }
    }

    public static String helpToJson(Object obj) {
        return gson.toJson(obj);
    }

    public static void sendMsg(WebSocketSession session, String responsePayload) {
        validateSessionAndPayload(session, responsePayload);
        try {
            session.sendMessage(new TextMessage(responsePayload));
        }
        catch (Exception e){
            throw new RuntimeException("error send process", e);

        }
    }

    public static void broadcastMsg(String responsePayload, WebSocketSession sender, List<WebSocketSession> sessions) {
        if (sessions == null) {
            logger.warning("error: list is null");
            return;
        }
        validateSessionAndPayload(sender, responsePayload);

        for (WebSocketSession client : sessions) {
            //sessionsToBroadcast
            if (client.isOpen()) {
                sendMsg(client, responsePayload);

            } else {
                logger.info("Session " + client.getId() + " is closed, skipping message send");
            }
        }
    }

    public static <T> void nullCheck (T msgObject) {
        if (msgObject == null) {
            throw new IllegalArgumentException("msgObject == null");
        }

    }

}
