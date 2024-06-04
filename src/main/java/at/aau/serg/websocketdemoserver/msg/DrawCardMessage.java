package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class DrawCardMessage {
    MessageType messageType;
    String roomID;
    String playerID;
    String card = "random";
    ActionTypeDrawCard actionTypeDrawCard;

    /*message maybe need to be broadcasted*/


    public enum ActionTypeDrawCard {
        ASK_FOR_CARD,
        RETURN_CARD_OK,
        RETURN_CARD_ERR
    }
}
