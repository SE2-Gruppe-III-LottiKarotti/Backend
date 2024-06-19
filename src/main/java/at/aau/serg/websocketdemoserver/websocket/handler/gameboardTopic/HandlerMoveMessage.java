package at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.PlayingPiece;
import at.aau.serg.websocketdemoserver.msg.MoveMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static at.aau.serg.websocketdemoserver.model.game.Gameboard.logger;

public class HandlerMoveMessage {

    public static void handleMoveMessage(WebSocketSession session, String payload, List<WebSocketSession> sessions, InMemoryRoomRepo roomRepo) throws Exception {

        TransportUtils.validateSessionAndPayload(session, payload);

        MoveMessage moveMessage = TransportUtils.helpFromJson(payload, MoveMessage.class);

        ObjectMapper mapper = new ObjectMapper();

        HandlerGameMessage.gameboard.setFields(moveMessage.getFields());
        String playerId = moveMessage.getSpielerId();
        String card = moveMessage.getCard();
        PlayingPiece playingpiece = moveMessage.getPlayingPiece();

        if (card.equals("CARROT")) {
            HandlerGameMessage.gameboard.twistTheCarrot();
        } else {
            int currentPosition = HandlerGameMessage.gameboard.getPlayingPiecePosition(playingpiece);

            if (currentPosition == -1) {
                HandlerGameMessage.gameboard.insertFigureToGameboard(playingpiece, card, currentPosition);
            }
            if (currentPosition >= 0) {
                HandlerGameMessage.gameboard.moveFigureForward(playerId, card, currentPosition, playingpiece);
            }
        }
        moveMessage.setFields(HandlerGameMessage.gameboard.getFelder());
        moveMessage.setPlayingPiece(playingpiece);

        String payloadExport = mapper.writeValueAsString(moveMessage);
        TransportUtils.broadcastMsg(payloadExport, session, sessions);

        logger.info("toClient: " + payloadExport);
    }
}

