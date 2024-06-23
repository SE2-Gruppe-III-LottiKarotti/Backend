package at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.PlayingPiece;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.GuessCheaterMessage;
import at.aau.serg.websocketdemoserver.msg.MoveMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static at.aau.serg.websocketdemoserver.model.game.Gameboard.logger;

public class HandlerCheatMessage {
    public static void handleCheatMessage(WebSocketSession session, String payload, List<WebSocketSession> sessions, InMemoryRoomRepo roomRepo) throws Exception {

        TransportUtils.validateSessionAndPayload(session, payload);
        ObjectMapper mapper = new ObjectMapper();

        GuessCheaterMessage cheatMessage = TransportUtils.helpFromJson(payload, GuessCheaterMessage.class);

        String currentPlayerId = cheatMessage.getAccusingPlayerId();
        String playerToBlameName = cheatMessage.getPlayerToBlameName();
        String roomId = cheatMessage.getRoomId();
        Room room = roomRepo.findRoomById(roomId);
        boolean cheater = false;

        String nextPlayerId = room.getNextPlayer(currentPlayerId).getPlayerID();
        String nextPlayerName = room.getNextPlayer(currentPlayerId).getName();
        if(nextPlayerName.equals(playerToBlameName)) {
            cheater = room.searchPlayerIdInCheatList(nextPlayerId);
            cheatMessage.setPlayerToBlameId(nextPlayerId);
        }

        if(cheater) {
            cheatMessage.setCheater("1");
        } else {
            cheatMessage.setCheater("0");
        }

        cheatMessage.setFields(cheatMessage.getFields());

        String payloadExport = mapper.writeValueAsString(cheatMessage);
        TransportUtils.broadcastMsg(payloadExport, session, sessions);

        logger.info("toClient: " + payloadExport);
    }
}
