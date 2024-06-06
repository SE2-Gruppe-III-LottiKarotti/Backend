package at.aau.serg.websocketdemoserver.model.game;

public class PlayingPiece {
    //playingPiece == spielfigur
    private int playingPiece;
    private Player player;


    public PlayingPiece() {
    }

    public int getPlayingPiece() {
        return playingPiece;
    }

    public void setPlayingPiece(int piece) {
        this.playingPiece = playingPiece;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
