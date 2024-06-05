package at.aau.serg.websocketdemoserver.model.raum;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RoomSetupMessageTest {

    @Test
    public void testGettersAndSetters() {
        String roomID = "123";
        String roomName = "Test Room";
        String creator = "Daniel";
        int availablePlayersSpace = 4;

        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setRoomID(roomID);
        roomInfo.setRoomName(roomName);
        roomInfo.setCreator(creator);
        roomInfo.setAvailablePlayersSpace(availablePlayersSpace);

        assertEquals(roomID, roomInfo.getRoomID());
        assertEquals(roomName, roomInfo.getRoomName());
        assertEquals(creator, roomInfo.getCreator());
        assertEquals(availablePlayersSpace, roomInfo.getAvailablePlayersSpace());

        // Testing availableRooms
        ArrayList<Room> availableRooms = new ArrayList<>();
        Room room1 = new Room(1, "Room 1");
        Room room2 = new Room(2, "Room 2");
        availableRooms.add(room1);
        availableRooms.add(room2);

        roomInfo.setAvailableRooms(availableRooms);
        assertEquals(availableRooms, roomInfo.getAvailableRooms());
    }
}
