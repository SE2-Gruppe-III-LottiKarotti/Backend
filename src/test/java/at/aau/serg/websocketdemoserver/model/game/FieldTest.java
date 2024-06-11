package at.aau.serg.websocketdemoserver.model.game;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldTest {
    private Field field;
    private PlayingPiece playingPiece;

    @Before
    public void setup() {
        field = new Field(true);
        playingPiece = new PlayingPiece();
    }

    @Test
    public void testInitialMoleHoleStatus() {
        assertTrue(field.isMoleHole());
    }

    @Test
    public void testSetIsMoleHole() {
        field.setIsMoleHole(false);
        assertFalse(field.isMoleHole());
    }

    @Test
    public void testInitialOpenStatus() {
        assertFalse(field.isOpen());
    }

    @Test
    public void testSetOpen() {
        field.setOpen(true);
        assertTrue(field.isOpen());
    }

    @Test
    public void testAddAndRemovePlayingPiece() {
        field.addPlayingPieceToField(playingPiece);
        assertEquals(playingPiece, field.getPlayingPiece());
        assertTrue(field.isOccupiedByPlayingPiece());

        field.removePlayingPieceFromField();
        assertNull(field.getPlayingPiece());
        assertFalse(field.isOccupiedByPlayingPiece());
    }

    @Test
    public void testPlayingPieceManagement() {
        assertNull(field.getPlayingPiece());
        assertFalse(field.isOccupiedByPlayingPiece());

        field.setPlayingPiece(playingPiece);
        assertEquals(playingPiece, field.getPlayingPiece());
        assertTrue(field.isOccupiedByPlayingPiece());

    }
}
