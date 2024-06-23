package at.aau.serg.websocketdemoserver.model.game;

import java.security.SecureRandom;
import java.util.logging.Logger;

public class Gameboard {
    public static final Logger logger = Logger.getLogger(Gameboard.class.getName());
    private static final String IS_OPEN_STATUS = " (isOpen: ";
    private Field[] fields;
    private int[] holes;
    private final SecureRandom random = new SecureRandom();
    int holeCounter;
    private String winner;
    int oldPositionCounter;
    int oldHole;

    public Gameboard() {
        this.fields = new Field[27]; // 27 fields incl. the carrot
        this.holes = new int[]{3, 6, 9, 15, 18, 20, 24};
        this.holeCounter = holes[random.nextInt(holes.length - 1)];
        winner = null;
        initFields();
    }

    public boolean hasWinner() {
        return winner != null;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    private void initFields() {
        for (int i = 0; i < fields.length; i++) {
            if (i == 3 || i == 6 || i == 9 || i == 15 || i == 18 || i == 20 || i == 24) {
                //mole hole
                fields[i] = new Field(true);
                fields[i].setOpen(false);
            } else {
                //normal field
                fields[i] = new Field(false);
            }
        }

        // init mole holes open
        oldPositionCounter = holeCounter;
        if (oldPositionCounter >= 0 && oldPositionCounter < fields.length) {
            fields[oldPositionCounter].setOpen(true);
        } else {
            logger.warning("Initial hole position is out of bounds: " + oldPositionCounter);
        }
    }

    public void twistTheCarrot() {
        logger.info("twistTheCarrot called");

        // Debug: output of the old positions and state
        logger.info("Old Position Counter: " + oldPositionCounter + IS_OPEN_STATUS + fields[oldPositionCounter].isOpen() + ")");

        oldHole = oldPositionCounter;

        // the holes that were previous opened, and that will closed on the next twist of the carrot
        if (oldHole >= 0 && oldHole < fields.length) {
            fields[oldHole].setOpen(false);
            logger.info("Closed old hole at positions " + oldHole + IS_OPEN_STATUS + fields[oldHole].isOpen() + ")");
        } else {
            logger.warning("Old hole position is out of bounds: " + oldHole);
        }

        do {
            holeCounter = holes[random.nextInt(holes.length)];
        } while (holeCounter == oldHole);

        logger.info("New holeCounter: " + holeCounter);

            fields[holeCounter].setOpen(true);
            oldPositionCounter = holeCounter;
            logger.info("New Position Counter: " + holeCounter + IS_OPEN_STATUS + fields[holeCounter].isOpen() + ")");
    }

    public int getPlayingPiecePosition(PlayingPiece playingPiece) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getPlayingPiece() != null && fields[i].getPlayingPiece().equals(playingPiece)) {
                return i;
            }
        }
        return -1;
    }

    public void insertFigureToGameboard(PlayingPiece newPlayingPiece, String card, int currenPosition) {
        int cardValue = Integer.parseInt(card);
        int newPosition = currenPosition + cardValue;

        while(!(fields[newPosition].getPlayingPiece() == null)) {
            newPosition++;
        }

        fields[newPosition].addPlayingPieceToField(newPlayingPiece);
    }


    public void moveFigureForward(String playerID, String card, int currentPosition, PlayingPiece playingPiece) {
        int cardValue = Integer.parseInt(card);
        int newPosition = currentPosition + cardValue;

        if(currentPosition+cardValue < fields.length) {
            while (!(fields[newPosition].getPlayingPiece() == null)) {
                newPosition++;
            }

            fields[currentPosition].removePlayingPieceFromField();
            fields[newPosition].addPlayingPieceToField(playingPiece);
        }

        if (newPosition == 26) {
            winner = playerID;
        }
    }

    public boolean checkWinCondition(Player player) {
        return player.hasReachedCarrot();
    }

    public Field[] getFelder() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }
}
