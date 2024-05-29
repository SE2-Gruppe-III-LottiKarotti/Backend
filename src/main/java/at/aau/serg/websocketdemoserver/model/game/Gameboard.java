package at.aau.serg.websocketdemoserver.model.game;

import java.util.Random;


public class Gameboard 
{
  
    private Feld[] felder;
    private final Random random = new Random();

    // FIXME remove commented out code
    // FIXME naming, are these hole positions?
    private int holeCounter1;// = random.nextInt((26/2));
    private int holeCounter2;// = (holeCounter1+(18%26)%26-4);

    // FIXME why is this not a "Spieler"? Unclear what it is referring to without reading the code
    private String winner;

    // FIXME what are these variables and why are they fields? naming!
    int oldPositionCounter1;
    int oldPositionCounter2;
    //abstand ca 18 Felder, damit sich diese genau gegenüber liegen
    // FIXME should be a static constant, no need for it to be a field
    private final int maxPosition; // Separate Variable für maximale Position des Spielbretts


    public Gameboard() {
        // FIXME magic numbers!
        this.felder = new Feld[26]; //26 felder inkl. karotte
        this.maxPosition = felder.length - 1; // Maximale Position ist die Länge des Arrays-1
        this.holeCounter1 = random.nextInt((14));
        // FIXME redundant modulo calculation, what is this even supposed to do?
        // FIXME latter part of initialization value should be a static constant
        this.holeCounter2 = (holeCounter1 + (18 % 26) % 26 - 4);
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

  // FIXME missing documentation
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
    

    


    //korrigiert
    private void initFields() {
        for (int i = 0; i < maxPosition; i++) {
            // FIXME magic numbers! Should be a Set::contains of known maulwurfshügel indices that are checked here!
            if (i == 3 || i == 6 || i == 9 || i == 15 || i == 18 || i == 20 || i == 24) {
                felder[i] = new Feld(true); // Maulwurfhügel
                // FIXME make default, or at least move out of conditional, as executed in both if and else branch!
                felder[i].setOpen(false);
            } else {
              felder[i] = new Feld(false);
              felder[i].setOpen(false);
              // Normale Felder
            }
        }


        // FIXME magic numbers
        // Initiale Spezialfelder setzen
        felder[2].setSpecialField(false); // Maulwurfhöhle
        felder[13].setSpecialField(false); // Brücke
        felder[22].setSpecialField(false); // Gatter

        //bugfix
        // FIXME Move condition into loop! Do not use while true!
        // FIXME what is this loop even supposed to do? can it be replaced?
        while (true) {
            // FIXME magic numbers
            if (holeCounter1 <= 15 && holeCounter2 <=24) {
                break;
            }
            // FIXME Duplicate code!
            holeCounter1 = random.nextInt((14));
            holeCounter2 = (holeCounter1 + (18 % 26) % 26 - 4);
        }
        // FIXME no need to copy into oldPositionCounter for READ only access!
        // Initiale Maulwurflöcher öffnen
        oldPositionCounter1 = holeCounter1;
        oldPositionCounter2 = holeCounter2;
        felder[oldPositionCounter1].setOpen(true);
        felder[oldPositionCounter2].setOpen(true);

        specialFieldSwitch(holeCounter1); // Spezialfelder initial umschalten
    }

    // FIXME unused
    public int getSpielfigurPosition(Spielfigur spielfigur) {
      for (int i = 0; i < felder.length; i++) {
        if (felder[i].getSpielfigur() == spielfigur) {
          return i;
        }
      }
      return -1;
    }

    // FIXME unused
    //insert figure to gameboard
    public void insertFigureToGameboard(Spieler spieler, String spielerId, String card) {
        Spielfigur addNewSpielfigur = new Spielfigur();
        int beginningPosition = 0;
        felder[beginningPosition].addSpielfigurToField(addNewSpielfigur);

        moveFigureForward(spieler, spielerId, card, beginningPosition);

    }

    // FIXME unused parameter spieler
    // FIXME passing spielerId and spieler is redundant
    //for lottiKarottiExtreme
    public void moveFigureForward(Spieler spieler, String spielerId, String card, int currentPosition) 
    {
        int oldPosition = currentPosition;
        // FIXME card should be a complex object, and not just a string!
        int cardValue = Integer.parseInt(card);
        boolean moveBackward = false;

        // FIXME Refactor and simplify, many magic numbers, duplicate concepts (skipping over other players)
        // FIXME Cyclomatic complexity too high
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
                    currentPosition = newPosition;
                    cardValue --;
                }
                else {
                    currentPosition = newPosition;
                }
              

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

    // FIXME why are there two ways of checking win conditions on the gameboard? winner and checkWinCondition?
  public boolean checkWinCondition(Spieler spieler) {
        return spieler.hasReachedCarrot();
    }

    public Feld[] getFelder() {
        return felder;
    }

    public void setFelder(Feld[] felder) {
        this.felder = felder;
    }

}
