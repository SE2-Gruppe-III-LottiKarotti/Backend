package at.aau.serg.websocketdemoserver.model.game;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FeldTest {
    private Feld feld;
    private Spielfigur spielfigur;

    @Before
    public void setup() {
        feld = new Feld(true);
        spielfigur = new Spielfigur();
    }

    @Test
    public void testInitialMaulwurfLochStatus() {
        assertTrue(feld.isIstEsEinMaulwurfLoch());
    }

    @Test
    public void testSetIstEsEinMaulwurfLoch() {
        feld.setIstEsEinMaulwurfLoch(false);
        assertFalse(feld.isIstEsEinMaulwurfLoch());
    }

    @Test
    public void testInitialOpenStatus() {
        assertFalse(feld.isOpen());
    }

    @Test
    public void testSetOpen() {
        feld.setOpen(true);
        assertTrue(feld.isOpen());
    }

    @Test
    public void testAddAndRemoveSpielfigur() {
        feld.addSpielfigurToField(spielfigur);
        assertEquals(spielfigur, feld.getSpielfigur());
        assertTrue(feld.isOccupiedBySpielfigur());

        feld.removeSpielFigurFromField();
        assertNull(feld.getSpielfigur());
        assertFalse(feld.isOccupiedBySpielfigur());
    }

    @Test
    public void testSpielfigurManagement() {
        assertNull(feld.getSpielfigur());
        assertFalse(feld.isOccupiedBySpielfigur());

        feld.setSpielfigur(spielfigur);
        assertEquals(spielfigur, feld.getSpielfigur());
        assertTrue(feld.isOccupiedBySpielfigur());
    }
}
