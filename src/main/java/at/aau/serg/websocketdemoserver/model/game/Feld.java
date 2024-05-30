package at.aau.serg.websocketdemoserver.model.game;

public class Feld {
    private boolean istEsEinMaulwurfLoch;
    private boolean isOpen;
    private Spielfigur spielfigur;

    private boolean isOpen;

    private boolean specialField;

    /*public Feld(boolean istEsEinMaulwurfLoch) {
        this.istEsEinMaulwurfLoch = istEsEinMaulwurfLoch;
    }

    public boolean isIstEsEinMaulwurfLoch() {
        return istEsEinMaulwurfLoch;
    }

    public void setIstEsEinMaulwurfLoch(boolean istEsEinMaulwurfLoch) {
        this.istEsEinMaulwurfLoch = istEsEinMaulwurfLoch;
    }

    public void removeSpielFigurFromField() {
        this.spielfigur = null;
    }

    public void addSpielfigurToField(Spielfigur spielfigur) {
        this.spielfigur = spielfigur;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Spielfigur getSpielfigur() {
        return spielfigur;
    }

    public void setSpielfigur(Spielfigur spielfigur) {
        this.spielfigur = spielfigur;
    }*/

    public Feld(boolean istEsEinMaulwurfLoch) {
        this.istEsEinMaulwurfLoch = istEsEinMaulwurfLoch;
    }



    public boolean isIstEsEinMaulwurfLoch() {
        return istEsEinMaulwurfLoch;
    }

    public void setIstEsEinMaulwurfLoch(boolean istEsEinMaulwurfLoch) {
        this.istEsEinMaulwurfLoch = istEsEinMaulwurfLoch;
    }


    public boolean isSpecialField() {
        return specialField;
    }

    public void setSpecialField(boolean specialField) {
        this.specialField = specialField;
    }

    public void removeSpielFigurFromField() {
        this.spielfigur = null;
    }

    public void addSpielfigurToField(Spielfigur spielfigur) {
        this.spielfigur = spielfigur;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Spielfigur getSpielfigur() {
        return spielfigur;
    }

    public void setSpielfigur(Spielfigur spielfigur) {
        this.spielfigur = spielfigur;
    }

    public boolean isOccupiedBySpielfigur() {
        return this.spielfigur != null;
    }

    public boolean isOccupiedBySpielfigur() {
        return this.spielfigur != null;
    }
}
