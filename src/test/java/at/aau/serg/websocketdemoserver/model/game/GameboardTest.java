package at.aau.serg.websocketdemoserver.model.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameboardTest {
    private Gameboard gameboard;
    private Spieler spieler;

    @Before
    public void setup() {
        gameboard = new Gameboard();
        spieler = new Spieler("TestSpieler");
    }

    @Test
    public void testGameboardInitialization() {
        assertNotNull(gameboard.getFelder());
        assertEquals(26, gameboard.getFelder().length);
    }

    @Test
    public void testInitFields() {
        Feld[] felder = gameboard.getFelder();

        // Überprüfen, ob die Maulwurfhügel korrekt gesetzt wurden
        int[] maulwurfHuegelIndizes = {3, 6, 9, 15, 18, 20, 24};
        for (int index : maulwurfHuegelIndizes) {
            assertTrue(felder[index].isIstEsEinMaulwurfLoch());
        }

        // Initiale Maulwurflöcher überprüfen
        assertTrue(felder[gameboard.oldPositionCounter].isOpen());

        // Andere Maulwurflöcher geschlossen
        for (int i = 0; i < 26; i++) {
            if (i != gameboard.oldPositionCounter && felder[i].isIstEsEinMaulwurfLoch()) {
                assertFalse(felder[i].isOpen());
            }
        }
    }

    @Test
    public void testTwistTheCarrot() {
        Feld[] felder = gameboard.getFelder();
        gameboard.twistTheCarrot();

        // Überprüfen, ob alte Löcher geschlossen und neue geöffnet sind
        assertFalse(gameboard.getFelder()[gameboard.oldHole].isOpen());
        assertTrue(gameboard.getFelder()[gameboard.holeCounter].isOpen() == felder[gameboard.holeCounter].isIstEsEinMaulwurfLoch());
    }

    @Test
    public void testGetSpielfigurPosition() {
        Spielfigur spielfigur = new Spielfigur();
        gameboard.getFelder()[0].addSpielfigurToField(spielfigur);
        int position = gameboard.getSpielfigurPosition(spielfigur);
        assertEquals(0, position);
    }

    @Test
    public void testInsertFigureToGameboard() {
        gameboard.insertFigureToGameboard(spieler, spieler.getSpielerID(), "2");

        // Überprüfen, ob eine Spielfigur zum ersten Feld hinzugefügt wurde
        assertNotNull(gameboard.getFelder()[0].getSpielfigur());

        // Optional: Überprüfen, ob es sich um die korrekte Spielfigur handelt
        Spielfigur addedFigure = gameboard.getFelder()[0].getSpielfigur();
        assertNotNull(addedFigure);
    }

    @Test
    public void testMoveFigureForward() {
        Spielfigur spielfigur = new Spielfigur();
        gameboard.getFelder()[0].addSpielfigurToField(spielfigur);

        gameboard.moveFigureForward(spieler.getSpielerID(), "2", 0);
        // Überprüfen, dass die Spielfigur vom Startfeld entfernt wurde
        assertNull(gameboard.getFelder()[0].getSpielfigur());
        // Überprüfen, dass die Spielfigur auf das neue Feld gesetzt wurde
        assertNotNull(gameboard.getFelder()[2].getSpielfigur());
    }


    @Test
    public void testCheckWinCondition() {
        assertFalse(gameboard.checkWinCondition(spieler));
        spieler.setReachedCarrot(true);
        assertTrue(gameboard.checkWinCondition(spieler));
    }
}

