package at.aau.serg.websocketdemoserver.model.game;

public class Field {
    private boolean isMoleHole;
    private boolean isOpen;
    private PlayingPiece playingPiece;

    public Field(boolean isMoleHole) {
        this.isMoleHole = isMoleHole;
    }

    public boolean isMoleHole() {
        return isMoleHole;
    }

    public void setIsMoleHole(boolean isMoleHole) {
        this.isMoleHole = isMoleHole;
    }

    public void removePlayingPieceFromField() {
        this.playingPiece = null;
    }

    public void addPlayingPieceToField(PlayingPiece playingPiece) {
        this.playingPiece = playingPiece;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public PlayingPiece getPlayingPiece() {
        return playingPiece;
    }

    public void setPlayingPiece(PlayingPiece playingPiece) {
        this.playingPiece = playingPiece;
    }

    public boolean isOccupiedByPlayingPiece() {
        return this.playingPiece != null;
    }

}
