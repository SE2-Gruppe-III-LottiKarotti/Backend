package at.aau.serg.websocketdemoserver.model.game;

import java.security.SecureRandom;
import java.util.logging.Logger;

public class Gameboard {
    public static final Logger logger = Logger.getLogger(Gameboard.class.getName());
    private static final String IS_OPEN_STATUS = " (isOpen: ";
    private Feld[] fields;
    private int[] holes;
    private final SecureRandom random = new SecureRandom();
    int holeCounter;
    private String winner;
    int oldPositionCounter;
    int oldHole;

    public Gameboard() {
        this.fields = new Feld[26]; // 26 felder inkl. karotte
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
                fields[i] = new Feld(true); // Maulwurfhügel
                fields[i].setOpen(false);
            } else {
                fields[i] = new Feld(false); // Normale Felder
            }
        }

        // Initiale Maulwurflöcher öffnen
        oldPositionCounter = holeCounter;
        if (oldPositionCounter >= 0 && oldPositionCounter < fields.length) {
            fields[oldPositionCounter].setOpen(true);
        } else {
            logger.warning("Initial hole position is out of bounds: " + oldPositionCounter);
        }
    }

    public void twistTheCarrot() {
        logger.info("twistTheCarrot called");

        // Debug: Ausgabe der alten Positionen und Zustände
        logger.info("Old Position Counter: " + oldPositionCounter + IS_OPEN_STATUS + fields[oldPositionCounter].isOpen() + ")");

        oldHole = oldPositionCounter;

        // Die Löcher, die vormals geöffnet wurden, werden beim nächsten Drehen wieder verschlossen
        if (oldHole >= 0 && oldHole < fields.length) {
            fields[oldHole].setOpen(false);
            logger.info("Closed old hole at positions " + oldHole + IS_OPEN_STATUS + fields[oldHole].isOpen() + ")");
        } else {
            logger.warning("Old hole position is out of bounds: " + oldHole);
        }

        holeCounter = holes[random.nextInt(holes.length - 1)];

        if (holeCounter == oldHole) {
            holeCounter = holes[random.nextInt(holes.length - 1)];
        }
        logger.info("New holeCounter: " + holeCounter);

        // Öffne die neuen Löcher, wenn es Maulwurfslöcher sind
        if (holeCounter >= 0 && holeCounter < fields.length && fields[holeCounter].isIstEsEinMaulwurfLoch()) {
            fields[holeCounter].setOpen(true);
            oldPositionCounter = holeCounter;
            logger.info("New Position Counter: " + holeCounter + IS_OPEN_STATUS + fields[holeCounter].isOpen() + ")");
        } else {
            logger.warning("New hole position is out of bounds or not a Maulwurfloch: " + holeCounter);
        }
    }

    public int getSpielfigurPosition(Spielfigur spielfigur) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getSpielfigur() == spielfigur) {
                return i;
            }
        }
        return -1;
    }

    public void insertFigureToGameboard(Spieler spieler, String spielerId, String card) {
        Spielfigur addNewSpielfigur = new Spielfigur(); // Korrekte KonstruktoraufrufSpielfigur addNewSpielfigur = new Spielfigur();
        int beginningPosition = 0;
        fields[beginningPosition].addSpielfigurToField(addNewSpielfigur);

        // Debugging-Ausgabe
        if (fields[beginningPosition].getSpielfigur() == addNewSpielfigur) {
            logger.info("Spielfigur wurde korrekt hinzugefügt.");
        } else {
            logger.severe("Spielfigur konnte nicht zum Spielfeld hinzugefügt werden.");
        }
    }


    public void moveFigureForward(String spielerId, String card, int currentPosition) {
        int oldPosition = currentPosition;
        int cardValue = Integer.parseInt(card);

        logger.info("Starting move from position: " + oldPosition + " with card value: " + cardValue);

        while (cardValue > 0) {
            int newPosition = currentPosition + 1;
            logger.info("Trying to move to position: " + newPosition);

            if (newPosition < fields.length) {
                if (!fields[newPosition].isOccupiedBySpielfigur()) {
                    // Wenn das Feld frei ist
                    currentPosition = newPosition;
                    cardValue--;
                    logger.info("Moved to position: " + currentPosition + ", remaining card value: " + cardValue);
                } else {
                    // Wenn das Feld besetzt ist, nur die Position aktualisieren
                    currentPosition = newPosition;
                    logger.info("Position " + newPosition + " is occupied. Current position remains: " + currentPosition);
                }
            } else {
                // Wenn die neue Position außerhalb des Spielfeldes liegt, beenden Sie die Schleife
                logger.info("New position out of bounds, breaking loop.");
                break;
            }
        }

        fields[oldPosition].removeSpielFigurFromField();
        fields[currentPosition].addSpielfigurToField(new Spielfigur());

        logger.info("Moved figure from position " + oldPosition + " to " + currentPosition);

        if (currentPosition == 25) {
            // Spieler gewinnt...
            winner = spielerId;
            // Diese öffentliche Variable kann von außen zugegriffen werden
            logger.info("Player " + spielerId + " wins!");
        }
    }

    public boolean checkWinCondition(Spieler spieler) {
        return spieler.hasReachedCarrot();
    }

    public Feld[] getFelder() {
        return fields;
    }

    public void setFields(Feld[] fields) {
        this.fields = fields;
    }
}
