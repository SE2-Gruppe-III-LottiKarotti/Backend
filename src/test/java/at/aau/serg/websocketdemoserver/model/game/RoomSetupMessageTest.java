package at.aau.serg.websocketdemoserver.model.game;

import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;
import at.aau.serg.websocketdemoserver.msg.RoomSetupMessage;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;

public class RoomSetupMessageTest {

    /*
    @Test
    public void testParameterizedConstructorAndGetters() {
        String roomName = "Test Room";
        String roomID = "123";
        String playerName = "Daniel";
        String playerID = "456";
        String numPlayers = "4";
        String messageIdentifier = "789";
        RoomSetupMessage.ActionType actionType = RoomSetupMessage.ActionType.OPEN_ROOM;

        //RoomSetupMessage message = new RoomSetupMessage(roomName, roomID, playerName, playerID, numPlayers, messageIdentifier, actionType);

        assertEquals(actionType, message.getActionType());
        assertEquals(messageIdentifier, message.getMessageIdentifier());
        assertEquals(roomID, message.getRoomID());
        assertEquals(roomName, message.getRoomName());
        assertEquals(playerID, message.getPlayerID());
        assertEquals(playerName, message.getPlayerName());
        assertEquals(numPlayers, message.getNumPlayers());
        assertNull(message.getRoomInfoList());
    }
    */

    @Test
    public void testSettersAndGettersForRoomInfoList() {
        RoomSetupMessage message = new RoomSetupMessage();
        ArrayList<RoomInfo> roomInfoList = new ArrayList<>();

        message.setRoomInfoList(roomInfoList);

        assertEquals(roomInfoList, message.getRoomInfoList());
    }
}
