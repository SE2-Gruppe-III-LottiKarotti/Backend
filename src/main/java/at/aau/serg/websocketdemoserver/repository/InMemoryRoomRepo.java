package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class InMemoryRoomRepo {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Set<Room> roomsRepo = new HashSet<>();


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
                //room found --> return room
            }
        }
        return null;
        //room not found --> return null
    }


    public Player getPlayerById(String spielerID) {
        for (Room room : roomsRepo) {
            Player player = room.getPlayerById(spielerID);
            if (player != null) {
                return player;
            }
        }
        return null;
    }

    public Set<Room> getAllRooms() {
        return roomsRepo;
    }


    public Room findRoomByName(String roomName) {
        if (roomName == null) {
            return null;
        }
        logger.info("Searching for room with name: " + roomName);
        for (Room room : roomsRepo) {
            if (room.getRoomName().equals(roomName)) {
                logger.info("Room found: " + room);
                return room;
            }
        }
        return null;
    }


    public ArrayList<Room> listAllRooms() {
        return new ArrayList<>(roomsRepo);
    }

    public void addPlayerToRoom(String roomID, Player player) {
        Room room = findRoomById(roomID);
        if (room != null) {
            room.addPlayer(player);
        } else {
            logger.info("room not found, when trying to add a player to a room.");
        }
    }


}
