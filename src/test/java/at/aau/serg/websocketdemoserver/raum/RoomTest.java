package at.aau.serg.websocketdemoserver.raum;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoomTest {
    private Room room;
    private Player player1;
    private Player player2;
    private Player player3;

    @Before
    public void setup() {
        room = new Room(3, "TestRoom");
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        player3 = new Player("Player3");
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
        room.addPlayer(player1);
        assertEquals(1, room.getListOfPlayers().size());
        assertEquals(2, room.getAvailablePlayersSpace());

        room.addPlayer(player2);
        room.addPlayer(player3);
        assertEquals(3, room.getListOfPlayers().size());
        assertEquals(0, room.getAvailablePlayersSpace());
    }

    @Test
    public void testAddPlayerToFullRoom() {
        room.addPlayer(player1);
        room.addPlayer(player2);
        room.addPlayer(player3);

        Player player4 = new Player("Player4");
        room.addPlayer(player4);
        assertEquals(3, room.getListOfPlayers().size());
        assertEquals(0, room.getAvailablePlayersSpace());
    }

    @Test
    public void testSetAndGetWinner() {
        room.setWinner("Player1");
        assertEquals("Player1", room.getWinner());
    }

    @Test
    public void testGetPlayerById() {
        room.addPlayer(player1);
        room.addPlayer(player2);
        assertEquals(player1, room.getPlayerById(player1.getPlayerID()));
        assertEquals(player2, room.getPlayerById(player2.getPlayerID()));
        assertNull(room.getPlayerById("NonExistentID"));
    }

    @Test
    public void testCheatListOperations() {
        room.addPlayerToCheatList(player1.getPlayerID());
        assertTrue(room.searchPlayerIdInCheatList(player1.getPlayerID()));

        room.deletePlayerFromCheatList(player1.getPlayerID());
        assertFalse(room.searchPlayerIdInCheatList(player1.getPlayerID()));
    }

    @Test
    public void testGetNextPlayer() {
        room.addPlayer(player1);
        room.addPlayer(player2);
        room.addPlayer(player3);

        Player nextPlayer = room.getNextPlayer(player1.getPlayerID());
        assertEquals(player2, nextPlayer);

        nextPlayer = room.getNextPlayer(player3.getPlayerID());
        assertEquals(player1, nextPlayer);
    }
}
