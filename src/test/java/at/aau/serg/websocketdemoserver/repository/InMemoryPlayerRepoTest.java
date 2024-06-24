package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.model.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryPlayerRepoTest {

    private InMemoryPlayerRepo playerRepo;

    @BeforeEach
    public void setUp() {
        playerRepo = new InMemoryPlayerRepo();
    }

    @Test
    public void testAddPlayer() {
        Player player = new Player("Player1");
        playerRepo.addPlayer(player);
        assertTrue(playerRepo.listAllPlayers().contains(player));
    }

    @Test
    public void testRemovePlayer() {
        Player player = new Player("Player1");
        playerRepo.addPlayer(player);
        playerRepo.removePlayer(player);
        assertFalse(playerRepo.listAllPlayers().contains(player));
    }

    @Test
    public void testFindPlayerById() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);

        Player foundPlayer = playerRepo.findPlayerById(player1.getPlayerID());
        assertNotNull(foundPlayer);
        assertEquals(player1.getPlayerID(), foundPlayer.getPlayerID());
    }

    @Test
    public void testFindPlayerById_NotFound() {
        Player foundPlayer = playerRepo.findPlayerById("nonexistent");
        assertNull(foundPlayer);
    }

    @Test
    public void testFindPlayerByName() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);

        Player foundPlayer = playerRepo.findPlayerByName("Player1");
        assertNotNull(foundPlayer);
        assertEquals("Player1", foundPlayer.getName());
    }

    @Test
    public void testFindPlayerByName_NotFound() {
        Player foundPlayer = playerRepo.findPlayerByName("nonexistent");
        assertNull(foundPlayer);
    }

    @Test
    public void testListAllPlayers() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player2);

        Set<Player> players = playerRepo.listAllPlayers();
        assertEquals(2, players.size());
        assertTrue(players.contains(player1));
        assertTrue(players.contains(player2));
    }

    @Test
    public void testAddDuplicatePlayer() {
        Player player1 = new Player("Player1");
        playerRepo.addPlayer(player1);
        playerRepo.addPlayer(player1);  // Adding the same player object again

        Set<Player> players = playerRepo.listAllPlayers();
        assertEquals(1, players.size());  // Set should contain only one instance of player1
        assertTrue(players.contains(player1));
    }
}
