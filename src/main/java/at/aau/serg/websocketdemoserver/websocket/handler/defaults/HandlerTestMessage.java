package at.aau.serg.websocketdemoserver.websocket.handler.defaults;

import at.aau.serg.websocketdemoserver.logic.TransportUtils;
import at.aau.serg.websocketdemoserver.msg.MessageType;
import at.aau.serg.websocketdemoserver.msg.TestMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.logging.Logger;

public class HandlerTestMessage {

    private final static Logger logger = Logger.getLogger(HandlerTestMessage.class.getName());

    public static void handleTestMessage(WebSocketSession session, String payload) {

        TransportUtils.validateSessionAndPayload(session, payload);

        //1 extract user message from the payload
        TestMessage testMessage = TransportUtils.helpFromJson(payload, TestMessage.class);

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
        //send echo back to the client --> export
        //important: use payload !!!!!!
        TransportUtils.sendMsg(session, response);


    }
}
