package at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.logic.RandomCardGenerator;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.DrawCardMessage;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.logging.Logger;


public class HandlerDrawCard {

    private static final Logger logger = Logger.getLogger(HandlerDrawCard.class.getName());


    public static void handleDrawCard(WebSocketSession session, String payload, List<WebSocketSession> sessions, InMemoryRoomRepo roomRepo) throws Exception {

        TransportUtils.validateSessionAndPayload(session, payload);
        DrawCardMessage drawCardMessage = TransportUtils.helpFromJson(payload, DrawCardMessage.class);

        ObjectMapper mapper = new ObjectMapper();

        //error check1
        if (drawCardMessage == null) {
            logger.info("error draw card");
            DrawCardMessage errorMsg = new DrawCardMessage();
            errorMsg.setActionTypeDrawCard(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_ERR);
            errorMsg.setCard("error");
            String export = TransportUtils.helpToJson(errorMsg);
            TransportUtils.broadcastMsg(export, session, sessions);

            return;
        }

        String roomId = drawCardMessage.getRoomID();
        String playerId = drawCardMessage.getPlayerID();
        String inputCard = drawCardMessage.getCard();

        Room room = roomRepo.findRoomById(roomId);
        if (room == null) {
            logger.info("room not found" + roomId);
            return;
        }

        Player player = room.getPlayerById(playerId);
        if (player == null) {
            logger.info("player not found" + playerId);
            return;
        }

        //now handle the return of card

        //first reset if player == cheater
        //catching is only possible, if it not the players turn again
        room.deletePlayerFromCheatList(playerId);

        String nextPlayerId = room.getNextPlayer(playerId).getPlayerID();
        String cardReturned;

        switch (inputCard) {
            case "RANDOM" -> cardReturned = RandomCardGenerator.startCardGenerator();
            case "ONE", "TWO", "THREE", "CARROT" -> {
                room.addPlayerToCheatList(playerId);
                cardReturned = inputCard;
            }
            default -> {
                logger.info("invalid input: " + inputCard);
                return;
            }
        }

        //now send it... finally
        drawCardMessage.setActionTypeDrawCard(DrawCardMessage.ActionTypeDrawCard.RETURN_CARD_OK);
        drawCardMessage.setCard(cardReturned);
        drawCardMessage.setNextPlayerId(nextPlayerId);
        String payloadExport;
        try {
            payloadExport = mapper.writeValueAsString(drawCardMessage);
        }catch (JsonProcessingException e) {
            throw new Exception("Json processing exception", e);
        }
        TransportUtils.broadcastMsg(payloadExport, session, sessions);

        logger.info("toClient: " + payloadExport);
    }

}
