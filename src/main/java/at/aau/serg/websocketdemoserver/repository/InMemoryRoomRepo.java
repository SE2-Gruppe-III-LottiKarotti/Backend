package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.model.raum.Room;

import java.util.HashSet;
import java.util.Set;

public class InMemoryRoomRepo {
    private Set<Room> roomsRepo;

    public InMemoryRoomRepo() {
        this.roomsRepo = new HashSet<>();
    }

    public void addRoom(Room room) {
        roomsRepo.add(room);
    }

    public void removeRoom(Room room) {
        roomsRepo.remove(room);
    }

    public Room findRoomById (String roomID) {
        for (Room room : roomsRepo) {
            if (room.getRoomID().equals(roomID)) {
                return room;
                //raum gefunden
            }
        }
        return null;
        //raum nicht gefunden
    }

    public Room findRoomByName (String roomName) {
        for (Room room : roomsRepo) {
            if (room.getRoomName().equals(roomName)) {
                return room;
                //gefunden
            }
        }
        return null;
        //nicht gefunden
    }

    public Set<Room> listAllRooms() {
        return roomsRepo;
    }


}
