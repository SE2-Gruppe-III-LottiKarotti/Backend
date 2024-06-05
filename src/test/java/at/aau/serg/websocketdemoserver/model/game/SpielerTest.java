package at.aau.serg.websocketdemoserver.model.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SpielerTest {
    private Spieler spieler;

    @Before
    public void setup() {
        spieler = new Spieler("TestSpieler");
    }

    @Test
    public void testInitialSpielerID() {
        assertNotNull(spieler.getSpielerID());
        assertFalse(spieler.getSpielerID().isEmpty());
    }

    @Test
    public void testGetName() {
        assertEquals("TestSpieler", spieler.getName());
    }

    @Test
    public void testSetName() {
        spieler.setName("NeuerName");
        assertEquals("NeuerName", spieler.getName());
    }

    @Test
    public void testInitialFarbe() {
        assertNull(spieler.getFarbe());
    }

    @Test
    public void testSetFarbe() {
        spieler.setFarbe(Spieler.Farbe.BLAU);
        assertEquals(Spieler.Farbe.BLAU, spieler.getFarbe());
    }

    @Test
    public void testInitialCheaterStatus() {
        assertFalse(spieler.isCheater());
    }

    @Test
    public void testSetCheater() {
        spieler.setCheater(true);
        assertTrue(spieler.isCheater());

        spieler.setCheater(false);
        assertFalse(spieler.isCheater());
    }

    @Test
    public void testInitialReachedCarrotStatus() {
        assertFalse(spieler.hasReachedCarrot());
    }

    @Test
    public void testSetReachedCarrot() {
        spieler.setReachedCarrot(true);
        assertTrue(spieler.hasReachedCarrot());

        spieler.setReachedCarrot(false);
        assertFalse(spieler.hasReachedCarrot());
    }
}
