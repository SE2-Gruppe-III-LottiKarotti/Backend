package at.aau.serg.websocketdemoserver.websocket.handler.gameboardTopic;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.logic.CensorMessageFunction;
import at.aau.serg.websocketdemoserver.logic.CensoredWordsDB;
import at.aau.serg.websocketdemoserver.msg.ChatMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public class HandlerChatMessage {
    public static void handleChatMessage(WebSocketSession session, String payload, List<WebSocketSession> sessions) {

        TransportUtils.validateSessionAndPayload(session, payload);

        ChatMessage chatMessage = TransportUtils.helpFromJson(payload, ChatMessage.class);


        if (chatMessage == null) {
            ChatMessage errorMsg = new ChatMessage();
            errorMsg.setActionTypeChat(ChatMessage.ActionTypeChat.CHAT_MSG_TO_CLIENTS_ERR);
            String exportErrMsg = TransportUtils.helpToJson(errorMsg);//gson.toJson(errorMsg);
            TransportUtils.sendMsg(session, exportErrMsg);
            return;

        }


        String inputText;

        if (chatMessage.getPlayerId() == null || chatMessage.getPlayerName() == null || chatMessage.getText() == null || chatMessage.getRoomID() == null
                || chatMessage.getPlayerId().isEmpty() || chatMessage.getPlayerName().isEmpty() || chatMessage.getText().isEmpty() || chatMessage.getRoomID().isEmpty()) {
            System.out.println("invalid input");
            ChatMessage errorMsg = new ChatMessage();
            errorMsg.setActionTypeChat(ChatMessage.ActionTypeChat.CHAT_MSG_TO_CLIENTS_ERR);
            String exportErrMsg = TransportUtils.helpToJson(errorMsg);//gson.toJson(errorMsg);
            TransportUtils.sendMsg(session, exportErrMsg);
            return;
        }


        inputText = chatMessage.getText();

        String outputText = CensorMessageFunction.censorText(inputText, CensoredWordsDB.censoredWords);

        chatMessage.setText(outputText);
        chatMessage.setActionTypeChat(ChatMessage.ActionTypeChat.CHAT_MSG_TO_CLIENTS_OK);

        String payloadExport = TransportUtils.helpToJson(chatMessage); //gson.toJson(chatMessage);
        //broadcastMsg(payloadExport, session);
        TransportUtils.broadcastMsg(payloadExport, session, sessions);

    }
}
