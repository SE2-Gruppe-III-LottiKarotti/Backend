package at.aau.serg.websocketdemoserver.model.game;

import at.aau.serg.websocketdemoserver.msg.ChatMessage;
import at.aau.serg.websocketdemoserver.msg.ChatMessage.ActionTypeChat;
import at.aau.serg.websocketdemoserver.msg.MessageType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

// FIXME add tests!
public class ChatTest {
    private ChatMessage message;

    @Before
    public void setup() {
        message = new ChatMessage();
    }

    @Test
    public void testConstructorAndGetters() {
        String playerName = "Daniel";
        String playerId = "player123";
        String text = "Hello, Daniel!";
        String roomID = "room123";

        ChatMessage message = new ChatMessage(playerName, playerId, text, roomID);

        assertEquals(playerName, message.getPlayerName());
        assertEquals(playerId, message.getPlayerId());
        assertEquals(text, message.getText());
        assertEquals(roomID, message.getRoomID());
        assertEquals(MessageType.CHAT, message.getMessageType());
    }

    @Test
    public void testSetters() {
        String playerName = "Daniel";
        String playerId = "player123";
        String text = "Hello, Daniel!";
        String roomID = "room123";

        message.setPlayerName(playerName);
        message.setPlayerId(playerId);
        message.setText(text);
        message.setRoomID(roomID);

        assertEquals(playerName, message.getPlayerName());
        assertEquals(playerId, message.getPlayerId());
        assertEquals(text, message.getText());
        assertEquals(roomID, message.getRoomID());
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(message.getPlayerName());
        assertNull(message.getPlayerId());
        assertNull(message.getText());
        assertNull(message.getRoomID());
    }

    @Test
    public void testActionTypeChat() {
        message.setActionTypeChat(ActionTypeChat.CHAT_MSG_TO_SERVER);
        assertEquals(ActionTypeChat.CHAT_MSG_TO_SERVER, message.getActionTypeChat());

        message.setActionTypeChat(ActionTypeChat.CHAT_MSG_TO_CLIENTS_OK);
        assertEquals(ActionTypeChat.CHAT_MSG_TO_CLIENTS_OK, message.getActionTypeChat());

        message.setActionTypeChat(ActionTypeChat.CHAT_MSG_TO_CLIENTS_ERR);
        assertEquals(ActionTypeChat.CHAT_MSG_TO_CLIENTS_ERR, message.getActionTypeChat());
    }
}
