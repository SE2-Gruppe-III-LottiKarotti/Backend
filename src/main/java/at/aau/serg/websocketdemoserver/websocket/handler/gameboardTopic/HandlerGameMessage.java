package at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.msg.GameMessageImpl;
import at.aau.serg.websocketdemoserver.msg.MessageType;
import at.aau.serg.websocketdemoserver.msg.MoveMessageImpl;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static at.aau.serg.websocketdemoserver.model.game.Gameboard.logger;

public class HandlerGameMessage {
    public static Gameboard gameboard;
    public static void handleGameMessage(WebSocketSession session, String payload, List<WebSocketSession> sessions, InMemoryRoomRepo roomRepo) throws Exception {
        TransportUtils.validateSessionAndPayload(session, payload);

        ObjectMapper mapper = new ObjectMapper();

        gameboard = new Gameboard();

        GameMessageImpl gameMessage = new GameMessageImpl();
        gameMessage.setFields(gameboard.getFelder());
        gameMessage.setMessageType(MessageType.GAME);

        String payloadExport = mapper.writeValueAsString(gameMessage);
        TransportUtils.broadcastMsg(payloadExport, session, sessions);

        logger.info("toClient: " + payloadExport);
    }
}
