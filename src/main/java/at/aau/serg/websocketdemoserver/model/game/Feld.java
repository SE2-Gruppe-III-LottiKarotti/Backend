package at.aau.serg.websocketdemoserver.model.game;

// FIXME Consistent language! Don't mix german and english!
public class Feld {
    private boolean istEsEinMaulwurfLoch;
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

    public Spielfigur getSpielfigur() {
        return spielfigur;
    }

    public void setSpielfigur(Spielfigur spielfigur) {
        this.spielfigur = spielfigur;
    }
}
