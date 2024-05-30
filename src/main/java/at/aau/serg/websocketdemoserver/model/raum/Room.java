package at.aau.serg.websocketdemoserver.model.raum;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Spieler;
import java.util.ArrayList;
import java.util.UUID;

public class Room {
    private String roomID;
    private String roomName;
    private ArrayList<Spieler> listOfPlayers;
    private int maxPlayers;
  private int availablePlayersSpace;
    private Gameboard gameboard;
    private String currentPlayerId; // Spieler der aktuell dran ist
    private ArrayList<String> cheaters;
    private String creatorName;
    private String winner;
    private Spieler currentPlayer; // oder spielerID ...


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
        if (gameboard.hasWinner()) {
            this.winner = gameboard.getWinner();
        }
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Spieler getPlayerById(String spielerID) {
        for (Spieler spieler : listOfPlayers) {
            if (spieler.getSpielerID().equals(spielerID)) {
                return spieler;
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

    public ArrayList<Spieler> getListOfPlayers() {
        return listOfPlayers;
    }

    public void setListOfPlayers(ArrayList<Spieler> listOfPlayers) {
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

    public Spieler getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Spieler currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
  
  public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }
  
  public void addPlayer(Spieler spieler) {
        if (availablePlayersSpace > 0) {
            listOfPlayers.add(spieler);
            availablePlayersSpace--;
        } else {
            System.out.println("raum voll");
            //der punkt sollte eigentlich nie erreicht werden
        }
    }
  
  //cheating operations
    public boolean searchPlayerIdInCheatList(String playerId) {
        return cheaters.contains(playerId);
    }

    public void addPlayerToCheatList(String playerId) {
        if (!cheaters.contains(playerId)) {
            cheaters.add(playerId);
        }
    }

    public void deletePlayerFromCheatList(String playerId) {
        if (cheaters.contains(playerId)) {
            cheaters.remove(playerId);
        }
    }

    public Spieler getNextPlayer(String currentPlayerId) {
        int index = -1;

        for (int i = 0; i < listOfPlayers.size(); i++) {
            if (listOfPlayers.get(i).getSpielerID().equals(currentPlayerId)) {
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
