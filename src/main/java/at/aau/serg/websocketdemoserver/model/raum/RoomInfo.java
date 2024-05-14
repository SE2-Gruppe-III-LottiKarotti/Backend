package at.aau.serg.websocketdemoserver.model.raum;

import java.util.ArrayList;

public class RoomInfo {
    private String roomID;
    private String roomName;
    //private int maxPlayers;
    private String creator;
    private int availablePlayersSpace;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    private ArrayList<Room> availableRooms;

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

    public int getAvailablePlayersSpace() {
        return availablePlayersSpace;
    }

    public void setAvailablePlayersSpace(int availablePlayersSpace) {
        this.availablePlayersSpace = availablePlayersSpace;
    }

    public ArrayList<Room> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(ArrayList<Room> availableRooms) {
        this.availableRooms = availableRooms;
    }
}
