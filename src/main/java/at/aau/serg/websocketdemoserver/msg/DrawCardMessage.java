package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class DrawCardMessage {
    MessageType messageType;
    String roomID;
    String playerID;
    String card;
    ActionTypeDrawCard actionTypeDrawCard;

    /*message maybe need to be broadcasted*/


    enum ActionTypeDrawCard {
        ASK_FOR_CARD,
        RETURN_CARD
    }
}
