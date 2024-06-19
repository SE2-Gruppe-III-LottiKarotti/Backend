package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class DrawCardMessage implements BaseMessageImpl {
    MessageType messageType;
    String roomID;
    String playerID;
    String card = "random";
    String nextPlayerId;
    ActionTypeDrawCard actionTypeDrawCard;

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.DRAW_CARD;
    }

    public enum ActionTypeDrawCard {
        ASK_FOR_CARD,
        RETURN_CARD_OK,
        RETURN_CARD_ERR
    }
}
