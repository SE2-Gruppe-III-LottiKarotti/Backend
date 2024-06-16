package at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.logic.RandomCardGenerator;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import at.aau.serg.websocketdemoserver.msg.DrawCardMessageImpl;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.logging.Logger;


public class HandlerDrawCard {

    private static final Logger logger = Logger.getLogger(HandlerDrawCard.class.getName());


    public static void handleDrawCard(WebSocketSession session, String payload, List<WebSocketSession> sessions, InMemoryRoomRepo roomRepo)  {

        TransportUtils.validateSessionAndPayload(session, payload);
        DrawCardMessageImpl drawCardMessage = TransportUtils.helpFromJson(payload, DrawCardMessageImpl.class);//gson.fromJson(payload, DrawCardMessage.class);

        //error check1
        if (drawCardMessage == null) {
            logger.info("error draw card");
            DrawCardMessageImpl errorMsg = new DrawCardMessageImpl();
            errorMsg.setActionTypeDrawCard(DrawCardMessageImpl.ActionTypeDrawCard.RETURN_CARD_ERR);
            errorMsg.setCard("error");
            String export = TransportUtils.helpToJson(errorMsg);//gson.toJson(errorMsg);
            TransportUtils.broadcastMsg(export, session, sessions);
            //TransportUtils.sendMsg(session, export);

            //broadcastMsg(export, session);
            return;
        }

        String roomId = drawCardMessage.getRoomID();
        String playerId = drawCardMessage.getPlayerID();
        //DrawCardMessage.ActionTypeDrawCard actionTypeDrawCard = drawCardMessage.getActionTypeDrawCard();
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
            case "random" -> cardReturned = RandomCardGenerator.startCardGenerator();
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
        drawCardMessage.setActionTypeDrawCard(DrawCardMessageImpl.ActionTypeDrawCard.RETURN_CARD_OK);
        drawCardMessage.setCard(cardReturned);
        drawCardMessage.setNextPlayerId(nextPlayerId);
        String exportPayload = TransportUtils.helpToJson(drawCardMessage);//gson.toJson(drawCardMessage);
        //broadcastMsg(exportPayload, session);
        TransportUtils.broadcastMsg(exportPayload, session, sessions);
        //TransportUtils.sendMsg(session, exportPayload);


        logger.info("toClient: " + exportPayload);

    }

}
