package at.aau.serg.websocketdemoserver.raum;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Spieler;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoomTest {
    private Room room;
    private Spieler spieler1;
    private Spieler spieler2;
    private Spieler spieler3;

    @Before
    public void setup() {
        room = new Room(3, "TestRoom");
        spieler1 = new Spieler("Spieler1");
        spieler2 = new Spieler("Spieler2");
        spieler3 = new Spieler("Spieler3");
    }

    @Test
    public void testRoomInitialization() {
        assertNotNull(room.getRoomID());
        assertEquals("TestRoom", room.getRoomName());
        assertEquals(3, room.getMaxPlayers());
        assertEquals(3, room.getAvailablePlayersSpace());
        assertNotNull(room.getListOfPlayers());
        assertNotNull(room.getGameboard());
        assertNotNull(room.getCheaters());
    }

    @Test
    public void testAddPlayer() {
        room.addPlayer(spieler1);
        assertEquals(1, room.getListOfPlayers().size());
        assertEquals(2, room.getAvailablePlayersSpace());

        room.addPlayer(spieler2);
        room.addPlayer(spieler3);
        assertEquals(3, room.getListOfPlayers().size());
        assertEquals(0, room.getAvailablePlayersSpace());
    }

    @Test
    public void testAddPlayerToFullRoom() {
        room.addPlayer(spieler1);
        room.addPlayer(spieler2);
        room.addPlayer(spieler3);

        Spieler spieler4 = new Spieler("Spieler4");
        room.addPlayer(spieler4);
        assertEquals(3, room.getListOfPlayers().size());
        assertEquals(0, room.getAvailablePlayersSpace());
    }

    @Test
    public void testSetAndGetWinner() {
        room.setWinner("Spieler1");
        assertEquals("Spieler1", room.getWinner());
    }

    @Test
    public void testGetPlayerById() {
        room.addPlayer(spieler1);
        room.addPlayer(spieler2);
        assertEquals(spieler1, room.getPlayerById(spieler1.getSpielerID()));
        assertEquals(spieler2, room.getPlayerById(spieler2.getSpielerID()));
        assertNull(room.getPlayerById("NonExistentID"));
    }

    @Test
    public void testCheatListOperations() {
        room.addPlayerToCheatList(spieler1.getSpielerID());
        assertTrue(room.searchPlayerIdInCheatList(spieler1.getSpielerID()));

        room.deletePlayerFromCheatList(spieler1.getSpielerID());
        assertFalse(room.searchPlayerIdInCheatList(spieler1.getSpielerID()));
    }

    @Test
    public void testGetNextPlayer() {
        room.addPlayer(spieler1);
        room.addPlayer(spieler2);
        room.addPlayer(spieler3);

        Spieler nextPlayer = room.getNextPlayer(spieler1.getSpielerID());
        assertEquals(spieler2, nextPlayer);

        nextPlayer = room.getNextPlayer(spieler3.getSpielerID());
        assertEquals(spieler1, nextPlayer);
    }
}
