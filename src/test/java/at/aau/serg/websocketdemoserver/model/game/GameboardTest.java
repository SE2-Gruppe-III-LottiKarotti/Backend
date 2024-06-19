package at.aau.serg.websocketdemoserver.model.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameboardTest {
    private Gameboard gameboard;
    private Player player;

    @Before
    public void setup() {
        gameboard = new Gameboard();
        player = new Player("TestPlayer");
    }

    @Test
    public void testGameboardInitialization() {
        assertNotNull(gameboard.getFelder());
        assertEquals(27, gameboard.getFelder().length);
    }

    @Test
    public void testInitFields() {
        Field[] felder = gameboard.getFelder();

        // Überprüfen, ob die Maulwurfhügel korrekt gesetzt wurden
        int[] maulwurfHuegelIndizes = {3, 6, 9, 15, 18, 20, 24};
        for (int index : maulwurfHuegelIndizes) {
            assertTrue(felder[index].isMoleHole());
        }

        // Initiale Maulwurflöcher überprüfen
        assertTrue(felder[gameboard.oldPositionCounter].isOpen());

        // Andere Maulwurflöcher geschlossen
        for (int i = 0; i < 26; i++) {
            if (i != gameboard.oldPositionCounter && felder[i].isMoleHole()) {
                assertFalse(felder[i].isOpen());
            }
        }
    }

    @Test
    public void testTwistTheCarrot() {
        Field[] felder = gameboard.getFelder();
        gameboard.twistTheCarrot();

        // Überprüfen, ob alte Löcher geschlossen und neue geöffnet sind
        assertFalse(gameboard.getFelder()[gameboard.oldHole].isOpen());
        assertEquals(gameboard.getFelder()[gameboard.holeCounter].isOpen(),felder[gameboard.holeCounter].isMoleHole());
    }

    @Test
    public void testGetPlayingPiecePosition() {
        PlayingPiece playingPiece = new PlayingPiece();
        gameboard.getFelder()[0].addPlayingPieceToField(playingPiece);
        int position = gameboard.getPlayingPiecePosition(playingPiece);
        assertEquals(0, position);
    }

    @Test
    public void testInsertFigureToGameboard() {
        PlayingPiece playingPiece = new PlayingPiece();
        gameboard.insertFigureToGameboard(playingPiece, "3", -1);

        // Überprüfen, ob eine Spielfigur zum ersten Feld hinzugefügt wurde
        assertNotNull(gameboard.getFelder()[2].getPlayingPiece());

        // Optional: Überprüfen, ob es sich um die korrekte Spielfigur handelt
        PlayingPiece addedFigure = gameboard.getFelder()[2].getPlayingPiece();
        assertNotNull(addedFigure);
    }

    @Test
    public void testMoveFigureForward() {
        PlayingPiece playingPiece = new PlayingPiece();
        gameboard.getFelder()[0].addPlayingPieceToField(playingPiece);

        gameboard.moveFigureForward(player.getPlayerID(), "2", 0, playingPiece );
        // Überprüfen, dass die Spielfigur vom Startfeld entfernt wurde
        assertNull(gameboard.getFelder()[0].getPlayingPiece());
        // Überprüfen, dass die Spielfigur auf das neue Feld gesetzt wurde
        assertNotNull(gameboard.getFelder()[2].getPlayingPiece());
    }


    @Test
    public void testCheckWinCondition() {
        assertFalse(gameboard.checkWinCondition(player));
        player.setReachedCarrot(true);
        assertTrue(gameboard.checkWinCondition(player));
    }
}

