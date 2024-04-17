package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.model.game.Spieler;

import java.util.HashSet;
import java.util.Set;

public class InMemorySpielerRepo {
    private final Set<Spieler> spielerRepo;

    public InMemorySpielerRepo() {
        this.spielerRepo = new HashSet<>();
    }

    public void addSpieler (Spieler spieler) {
        spielerRepo.add(spieler);

    }

    public void removeSpieler (Spieler spieler) {
        spielerRepo.remove(spieler);
    }

    public Spieler findSpielerById (String spielerID) {
        for (Spieler spieler : spielerRepo) {
            if (spieler.getSpielerID().equals(spielerID)) {
                return spieler;
            }
        }
        return null;
        //spieler nicht gefunden
    }

    public Spieler findSpielerByName (String name) {
        for (Spieler spieler : spielerRepo) {
            if (spieler.getName().equals(name)) {
                return spieler;
            }
        }
        return null;
        //nicht gefunden

    }

    public Set<Spieler> listAllSpieler() {
        return spielerRepo;
    }
}
