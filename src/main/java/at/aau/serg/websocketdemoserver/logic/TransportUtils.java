package at.aau.serg.websocketdemoserver.logic;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class TransportUtils {

    public static Logger logger = Logger.getLogger(TransportUtils.class.getName());
    private static final Gson gson = new Gson();

    private static final int timelimit = 7000; //7sec

    private static final int buffersize = 256 * 1024; // 256kb


    public static void validateSessionAndPayload(WebSocketSession session, String payload) {
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

    private static WebSocketSession syncSend(WebSocketSession session) {
        return new ConcurrentWebSocketSessionDecorator(session, timelimit, buffersize);
    }

    public static void sendMsg(WebSocketSession session, String responsePayload) {
        validateSessionAndPayload(session, responsePayload);
        try {
            WebSocketSession sync = syncSend(session);
            sync.sendMessage(new TextMessage(responsePayload));
        }
        catch (IOException e){
            logger.warning( "send error process");
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
