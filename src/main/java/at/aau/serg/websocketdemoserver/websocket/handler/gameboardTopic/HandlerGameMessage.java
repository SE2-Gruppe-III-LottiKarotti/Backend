package at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.DrawCardMessage;
import at.aau.serg.websocketdemoserver.msg.GameMessage;
import at.aau.serg.websocketdemoserver.msg.MessageType;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static at.aau.serg.websocketdemoserver.model.game.Gameboard.logger;

public class HandlerGameMessage {
    static Gameboard gameboard;
    public static void handleGameMessage(WebSocketSession session, String payload, List<WebSocketSession> sessions, InMemoryRoomRepo roomRepo) throws Exception {
        TransportUtils.validateSessionAndPayload(session, payload);
        GameMessage gameMessage = TransportUtils.helpFromJson(payload, GameMessage.class);

        ObjectMapper mapper = new ObjectMapper();

        gameboard = new Gameboard();

        String playerId = gameMessage.getPlayerId();
        String roomId = gameMessage.getRoomId();

        Room room = roomRepo.findRoomById(roomId);

        String nextPlayerId = room.getNextPlayer(playerId).getPlayerID();
        String nextPlayerName = room.getNextPlayer(playerId).getName();
        String nextPlayerName2 = room.getNextPlayer(nextPlayerId).getName();

        String[] playerNames = {nextPlayerName, nextPlayerName2};

        gameMessage.setPlayerNames(playerNames);
        gameMessage.setFields(gameboard.getFelder());
        gameMessage.setMessageType(MessageType.GAME);

        String payloadExport = mapper.writeValueAsString(gameMessage);
        TransportUtils.broadcastMsg(payloadExport, session, sessions);

        logger.info("toClient: " + payloadExport);
    }
}
