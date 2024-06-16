package at.aau.serg.websocketdemoserver.websocket.handler.defaults;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.msg.MessageType;
import at.aau.serg.websocketdemoserver.msg.TestMessageImpl;
import org.springframework.web.socket.WebSocketSession;
import java.util.logging.Logger;

public class HandlerTestMessage {

    private final static Logger logger = Logger.getLogger(HandlerTestMessage.class.getName());

    public static void handleTestMessage(WebSocketSession session, String payload) {

        TransportUtils.validateSessionAndPayload(session, payload);

        //1 user message aus der payload extrahieren
        TestMessageImpl testMessage = TransportUtils.helpFromJson(payload, TestMessageImpl.class);

        TransportUtils.nullCheck(testMessage);

        logger.info("testmessage received " + testMessage.getText());
        //2 add additional or set information to message with set
        testMessage.setMessageType(MessageType.TEST);
        testMessage.setText("juhu");
        String msgIdentifier = testMessage.getMessageIdentifier();
        testMessage.setMessageIdentifier(msgIdentifier);
        //make a new payload
        String response = TransportUtils.helpToJson(testMessage);
        //print to terminal
        logger.info("testmessage changed and send " + testMessage.getText());
        // Send echo back to the client --> export
        //umbedingt payload verwenden!!!!!!
        TransportUtils.sendMsg(session, response);


    }
}
