package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.model.game.Spieler;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Component
@Repository
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

    /*public Room findRoomByName (String roomName) {
        for (Room room : roomsRepo) {
            if (room.getRoomName().equals(roomName)) {
                return room;
                //gefunden
            }
        }
        return null;
        //nicht gefunden
    }*/


    public Room findRoomByName(String roomName) {
        System.out.println("Searching for room with name: " + roomName);
        for (Room room : roomsRepo) {
            if (room != null && room.getRoomName() != null && room.getRoomName().equals(roomName)) {
                System.out.println("Room found: " + room);
                return room;
            }
        }
        return null;
    }

    public Set<Room> listAllRooms() {
        return roomsRepo;
    }

    public void addPlayerToRoom(String roomID, Spieler spieler) {
        Room room = findRoomById(roomID);
        if (room != null) {
            room.addPlayer(spieler);
        } else {
            System.out.println("Raum nicht gefunden.");
        }
    }


}
