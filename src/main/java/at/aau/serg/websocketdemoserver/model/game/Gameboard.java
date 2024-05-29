package at.aau.serg.websocketdemoserver.model.game;

import java.util.Random;

<<<<<<< Gameboard_Logik
public class Gameboard {
    private Feld[] fields;
    private int[] holes;
    private final SecureRandom random = new SecureRandom();
    int holeCounter;
=======

public class Gameboard 
{
  
    private Feld[] felder;
    private final Random random = new Random();
    private int holeCounter1;// = random.nextInt((26/2));
    private int holeCounter2;// = (holeCounter1+(18%26)%26-4);

>>>>>>> development
    private String winner;
    int oldPositionCounter;
    int oldHole;
    private final int maxPosition; // maximale Position des Spielbretts


    public Gameboard() {
<<<<<<< Gameboard_Logik
        this.fields = new Feld[26]; //26 felder inkl. karotte
        this.holes = new int[]{3, 6, 9, 15, 18, 20, 24};
        this.maxPosition = fields.length - 1; // Maximale Position ist die Länge des Arrays-1
        this.holeCounter = holes[random.nextInt(holes.length-1)];
=======
        this.felder = new Feld[26]; //26 felder inkl. karotte
        this.maxPosition = felder.length - 1; // Maximale Position ist die Länge des Arrays-1
        this.holeCounter1 = random.nextInt((14));
        this.holeCounter2 = (holeCounter1 + (18 % 26) % 26 - 4);
>>>>>>> development
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
  
  
  public void twistTheCarrot() {
    felder[oldPositionCounter1].setOpen(false);
    felder[oldPositionCounter2].setOpen(false);
    
    if (holeCounter1 < 15) {
      holeCounter1++;
    } else {
      holeCounter1 = 0;
    }
    if (holeCounter2 > 16) {
      holeCounter2--;
    } else {
      holeCounter2 = 24;
    }
    
    //maulwurfhügel öffnen
    if (felder[holeCounter1].isIstEsEinMaulwurfLoch()) {
      felder[holeCounter1].setOpen(true);
      oldPositionCounter1 = holeCounter1;
    }
    if (felder[holeCounter2].isIstEsEinMaulwurfLoch()) {
      felder[holeCounter2].setOpen(true);
      oldPositionCounter2 = holeCounter2;
    }
    
    specialFieldSwitch(holeCounter1);
  }
  
  private void specialFieldSwitch(int holeCounter1) {
    if(holeCounter1 % 3 == 0) {
      felder[2].setSpecialField(true); // Maulwurfhöhle
      felder[13].setSpecialField(false);
      felder[22].setSpecialField(false);
    }
    if(holeCounter1 % 3 == 1) {
      felder[2].setSpecialField(false);
      felder[13].setSpecialField(true); // Brücke
      felder[22].setSpecialField(false);
    }
    if(holeCounter1 % 3 == 2) {
      felder[2].setSpecialField(false);
      felder[13].setSpecialField(false);
      felder[22].setSpecialField(true); // Gatter
    }
  }
    

    


    private void initFields() {
<<<<<<< Gameboard_Logik
        int j = 0;
        for (int i = 0; i < fields.length; i++) {
=======
        for (int i = 0; i < maxPosition; i++) {
>>>>>>> development
            if (i == 3 || i == 6 || i == 9 || i == 15 || i == 18 || i == 20 || i == 24) {
                fields[i] = new Feld(true); // Maulwurfhügel
                fields[i].setOpen(false);
            } else {
<<<<<<< Gameboard_Logik
                fields[i] = new Feld(false); // Normale Felder
            }
        }

=======
              felder[i] = new Feld(false);
              felder[i].setOpen(false);
              // Normale Felder
            }
        }


        // Initiale Spezialfelder setzen
        felder[2].setSpecialField(false); // Maulwurfhöhle
        felder[13].setSpecialField(false); // Brücke
        felder[22].setSpecialField(false); // Gatter

        //bugfix
        while (true) {
            if (holeCounter1 <= 15 && holeCounter2 <=24) {
                break;
            }
            holeCounter1 = random.nextInt((14));
            holeCounter2 = (holeCounter1 + (18 % 26) % 26 - 4);
        }
>>>>>>> development
        // Initiale Maulwurflöcher öffnen
        oldPositionCounter = holeCounter;
        fields[oldPositionCounter].setOpen(true);
    }

<<<<<<< Gameboard_Logik
    public void twistTheCarrot() {
        System.out.println("twistTheCarrot called");

        // Debug: Ausgabe der alten Positionen und Zustände
        System.out.println("Old Position Counter: " + oldPositionCounter + " (isOpen: " + fields[oldPositionCounter].isOpen() + ")");

        oldHole = oldPositionCounter;

        // Die Löcher, die vormals geöffnet wurden, werden beim nächsten Drehen wieder verschlossen
        fields[oldHole].setOpen(false);

        // Debug: Ausgabe der alten Positionen nach dem Schließen
        System.out.println("Closed old hole at positions " + oldHole + " (isOpen: " + fields[oldHole].isOpen() + ")");

        holeCounter = holes[random.nextInt(holes.length-1)];

        if(holeCounter == oldHole){
            holeCounter = holes[random.nextInt(holes.length-1)];
        }
        System.out.println("New holeCounter: " + holeCounter);

            // Öffne die neuen Löcher, wenn es Maulwurfslöcher sind
            if (fields[holeCounter].isIstEsEinMaulwurfLoch()) {
                fields[holeCounter].setOpen(true);
                oldPositionCounter = holeCounter;
                System.out.println("New Position Counter: " + holeCounter + " (isOpen: " + fields[holeCounter].isOpen() + ")");
            } else {
                System.out.println("Position " + holeCounter + " is not a Maulwurfloch.");
            }
    }

    public int getSpielfigurPosition(Spielfigur spielfigur) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getSpielfigur() == spielfigur) {
                return i;
            }
=======
    public int getSpielfigurPosition(Spielfigur spielfigur) {
      for (int i = 0; i < felder.length; i++) {
        if (felder[i].getSpielfigur() == spielfigur) {
          return i;
>>>>>>> development
        }
      }
      return -1;
    }

    public void insertFigureToGameboard(Spieler spieler, String spielerId, String card) {
        Spielfigur addNewSpielfigur = new Spielfigur();
        int beginningPosition = 0;
        fields[beginningPosition].addSpielfigurToField(addNewSpielfigur);

        // Debugging-Ausgabe
        if (fields[beginningPosition].getSpielfigur() == addNewSpielfigur) {
            System.out.println("Spielfigur wurde korrekt hinzugefügt.");
        } else {
            System.err.println("Spielfigur konnte nicht zum Spielfeld hinzugefügt werden.");
        }
    }
<<<<<<< Gameboard_Logik

    public void moveFigureForward(Spieler spieler, String spielerId, String card, int currentPosition) {
=======
      
    //for lottiKarottiExtreme
    public void moveFigureForward(Spieler spieler, String spielerId, String card, int currentPosition) 
    {
>>>>>>> development
        int oldPosition = currentPosition;
        int cardValue = Integer.parseInt(card);

        System.out.println("Starting move from position: " + oldPosition + " with card value: " + cardValue);

<<<<<<< Gameboard_Logik
        while (cardValue > 0) {
            int newPosition = currentPosition + 1;
            System.out.println("Trying to move to position: " + newPosition);

            if (newPosition < fields.length) {
                if (!fields[newPosition].isOccupiedBySpielfigur()) {
                    // Wenn das Feld frei ist
=======
        while (cardValue > 0) 
        {
            int newPosition = currentPosition +1;
            if (newPosition < felder.length) {
                //wenn die brücke oben ist...
                if (newPosition == 13 && felder[13].isSpecialField() == true) {
                  newPosition = 12;  
                  while (cardValue > 0) {
                        if (!felder[newPosition].isOccupiedBySpielfigur()) {
                            //wenn das feld frei ist
                            currentPosition--;
                            cardValue --;
                        }
                        else {
                            currentPosition--;
                        }
                    }
                    moveBackward = true;
                } else {
                    //die brücke ist kein feld, sondern ein gegenstand... 
                    //aber für eine leichtere implementierung... ein feld
                    newPosition = 14;
                }
                if (!felder[newPosition].isOccupiedBySpielfigur() && moveBackward == false) {
                    //wenn das feld frei ist
>>>>>>> development
                    currentPosition = newPosition;
                    cardValue--;
                    System.out.println("Moved to position: " + currentPosition + ", remaining card value: " + cardValue);
                } else {
                    // Wenn das Feld besetzt ist, nur die Position aktualisieren
                    currentPosition = newPosition;
                    System.out.println("Position " + newPosition + " is occupied. Current position remains: " + currentPosition);
                }
<<<<<<< Gameboard_Logik
            } else {
                // Wenn die neue Position außerhalb des Spielfeldes liegt, beenden Sie die Schleife
                System.out.println("New position out of bounds, breaking loop.");
                break;
            }
        }

        fields[oldPosition].removeSpielFigurFromField();
        fields[currentPosition].addSpielfigurToField(new Spielfigur());

        System.out.println("Moved figure from position " + oldPosition + " to " + currentPosition);

        if (currentPosition == 25) {
            // Spieler gewinnt...
            winner = spielerId;
            // Diese öffentliche Variable kann von außen zugegriffen werden
            System.out.println("Player " + spielerId + " wins!");
        }
    }


    public boolean checkWinCondition(Spieler spieler) {
        // Überprüfe, ob der Spieler genug Karotten gesammelt hat
=======
              

        }
    }
      if (felder[currentPosition].isIstEsEinMaulwurfLoch() == true && felder[currentPosition].isOpen() == true) {
        felder[oldPosition].removeSpielFigurFromField();
      }
      //moveBackward = false;
      felder[oldPosition].removeSpielFigurFromField();
      felder[currentPosition].addSpielfigurToField(new Spielfigur());
      //maulwurf
      if (currentPosition == 2 && felder[currentPosition].isSpecialField() == true) {
        felder[currentPosition].removeSpielFigurFromField();
      }
        //gatter
      if (currentPosition == 22 && felder[currentPosition].isSpecialField() == true) {
        felder[oldPosition].removeSpielFigurFromField();
        felder[8].addSpielfigurToField(new Spielfigur());
      }
      if (currentPosition == 25) {
        //player wins...
        winner = spielerId;
        //this public variable can be accessed from outside
      }
      
    }
  
  public boolean checkWinCondition(Spieler spieler) {
>>>>>>> development
        return spieler.hasReachedCarrot();
    }

    public Feld[] getFelder() {
        return fields;
    }

<<<<<<< Gameboard_Logik
    public void setFields(Feld[] fields) {
        this.fields = fields;}
}
=======
    public void setFelder(Feld[] felder) {
        this.felder = felder;
    }

}
>>>>>>> development
