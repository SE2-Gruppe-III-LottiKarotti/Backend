package at.aau.serg.websocketdemoserver.model.game;

public class Feld {
    private boolean istEsEinMaulwurfLoch;
    private boolean isOpen;
    private boolean specialField;
    private Spielfigur spielfigur;

    public Feld(boolean istEsEinMaulwurfLoch) {
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
    }

    public boolean isOccupiedBySpielfigur() {
        return this.spielfigur != null;
    }
}