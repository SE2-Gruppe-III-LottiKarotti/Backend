package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.model.game.Spieler;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Component
//@Repository
public class InMemoryRoomRepo {

    //private static final InMemoryRoomRepo instance = new InMemoryRoomRepo();
    private final Set<Room> roomsRepo = new HashSet();

    public static InMemoryRoomRepo roomRepo2;

    /*public InMemoryRoomRepo() {
        roomRepo2 = this;
    }*/

    /*private InMemoryRoomRepo() {
        //this.roomsRepo = new HashSet<>();
        //initTestRoom();
    }*/

    /*public static Set<Room> getRoomsRepo() {
        return roomsRepo;
    }*/

    /*
    public static InMemoryRoomRepo getInstance() {
        return instance;
    }*/


    //for checking select room and join room
    private void initTestRoom() {
        Room testRoom = new Room(4, "testRoom");
        roomsRepo.add(testRoom);
    }

    public void addRoom(Room room) {
        if (room == null) {
            return;
        }
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
        if (roomName == null) {
            return null;
        }
        System.out.println("Searching for room with name: " + roomName);
        for (Room room : roomsRepo) {
            //assert (room != null && room.getRoomName() != null);
            if (room.getRoomName().equals(roomName)) {
                //room != null && room.getRoomName() != null &&
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
