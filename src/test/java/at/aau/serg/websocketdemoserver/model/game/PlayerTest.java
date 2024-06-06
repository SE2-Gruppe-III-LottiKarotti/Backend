package at.aau.serg.websocketdemoserver.model.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;

    @Before
    public void setup() {
        player = new Player("TestPlayer");
    }

    @Test
    public void testInitialPlayerID() {
        assertNotNull(player.getPlayerID());
        assertFalse(player.getPlayerID().isEmpty());
    }

    @Test
    public void testGetName() {
        assertEquals("TestPlayer", player.getName());
    }

    @Test
    public void testSetName() {
        player.setName("NewName");
        assertEquals("NewName", player.getName());
    }

    @Test
    public void testInitialColour() {
        assertNull(player.getColour());
    }

    @Test
    public void testSetColourBLUE() {
        player.setColour(Player.Colour.BLUE);
        assertEquals(Player.Colour.BLUE, player.getColour());
    }

    @Test
    public void testSetColourYELLOW() {
        player.setColour(Player.Colour.YELLOW);
        assertEquals(Player.Colour.YELLOW, player.getColour());
    }

    @Test
    public void testSetColourBLACK() {
        player.setColour(Player.Colour.BLACK);
        assertEquals(Player.Colour.BLACK, player.getColour());
    }

    @Test
    public void testSetColourRED() {
        player.setColour(Player.Colour.RED);
        assertEquals(Player.Colour.RED, player.getColour());
    }

    @Test
    public void testSetColourGREEN() {
        player.setColour(Player.Colour.GREEN);
        assertEquals(Player.Colour.GREEN, player.getColour());
    }

    @Test
    public void testInitialCheaterStatus() {
        assertFalse(player.isCheater());
    }

    @Test
    public void testSetCheater() {
        player.setCheater(true);
        assertTrue(player.isCheater());

        player.setCheater(false);
        assertFalse(player.isCheater());
    }

    @Test
    public void testInitialReachedCarrotStatus() {
        assertFalse(player.hasReachedCarrot());
    }

    @Test
    public void testSetReachedCarrot() {
        player.setReachedCarrot(true);
        assertTrue(player.hasReachedCarrot());

        player.setReachedCarrot(false);
        assertFalse(player.hasReachedCarrot());
    }
}
