package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.model.game.Player;

import java.util.HashSet;
import java.util.Set;

public class InMemoryPlayerRepo {
    private final Set<Player> playerRepo;

    public InMemoryPlayerRepo() {
        this.playerRepo = new HashSet<>();
    }

    public void addPlayer (Player player) {
        playerRepo.add(player);

    }

    public void removePlayer (Player player) {
        playerRepo.remove(player);
    }

    public Player findPlayerById (String playerID) {
        for (Player player : playerRepo) {
            if (player.getPlayerID().equals(playerID)) {
                return player;
            }
        }
        return null;
        //spieler nicht gefunden
    }

    public Player findPlayerByName (String name) {
        for (Player player : playerRepo) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
        //nicht gefunden

    }

    public Set<Player> listAllPlayers() {
        return playerRepo;
    }
}
