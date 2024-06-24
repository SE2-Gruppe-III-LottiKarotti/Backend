package at.aau.serg.websocketdemoserver.model.raum;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Player;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public class Room {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private String roomID;
    private String roomName;
    private ArrayList<Player> listOfPlayers;
    private int maxPlayers;
    private int availablePlayersSpace;
    private Gameboard gameboard;
    private String currentPlayerId;
    private ArrayList<String> cheaters;
    private String creatorName;
    private String winner;
    private Player currentPlayer;

    public Room(int maxPlayers, String roomName) {
        this.roomID = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.maxPlayers = maxPlayers;
        this.availablePlayersSpace = maxPlayers;
        this.listOfPlayers = new ArrayList<>();
        this.cheaters = new ArrayList<>();
        this.gameboard = new Gameboard();
    }

    //method to check at the beginning or the end, if someone has one the game --> maybe at the end
    public void checkAndSetWinner() {
        // FIXME feature envy
        if (gameboard.hasWinner()) {
            this.winner = gameboard.getWinner();
        }
    }

    // FIXME should not exist, winner should never be set if detemined by Gameboard
    public void setWinner(String winner) {
        this.winner = winner;
    }


    public String getWinner() {
        return this.winner;
    }

    public Player getPlayerById(String playerID) {
        for (Player player : listOfPlayers) {
            if (player.getPlayerID().equals(playerID)) {
                return player;

            }
        }
        return null;
    }

    public int getAvailablePlayersSpace() {
        return availablePlayersSpace;
    }

    public void setAvailabePlayersSpace(int availabePlayersSpace) {
        this.availablePlayersSpace = availabePlayersSpace;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public void setListOfPlayers(ArrayList<Player> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Gameboard getGameboard() {
        return gameboard;
    }

    public void setGameboard(Gameboard gameboard) {
        this.gameboard = gameboard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public void setCreatorName(String playerName) {
        this.creatorName = playerName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void addPlayer(Player player) {
        if (availablePlayersSpace > 0) {
            listOfPlayers.add(player);
            availablePlayersSpace--;
        } else {
            logger.info("room full!");
            //this point should never be reached
        }
    }

    //cheating operations
    public ArrayList<String> getCheaters() {
        return this.cheaters;
    }

    public boolean searchPlayerIdInCheatList(String playerID) {
        return cheaters.contains(playerID);
    }

    public void addPlayerToCheatList(String playerID) {
        if (!cheaters.contains(playerID)) {
            cheaters.add(playerID);

        }
    }

    public void deletePlayerFromCheatList(String playerId) {
        // FIXME (optional) could be a Set instead of a list based on your usage
        if (cheaters.contains(playerId)) {
            cheaters.remove(playerId);
        }
    }

    public Player getNextPlayer(String currentPlayerId) {
        int index = -1;

        // FIXME duplicate code getPlayerById, consider listOfPlayers.indexOf?
        for (int i = 0; i < listOfPlayers.size(); i++) {
            if (listOfPlayers.get(i).getPlayerID().equals(currentPlayerId)) {
                index = i;
                break;
            }
        }

        //this should never happen
        if (index == -1) {
            return null;
        }

        int nextPlayer = (index + 1) % listOfPlayers.size();
        return listOfPlayers.get(nextPlayer);
    }
}
