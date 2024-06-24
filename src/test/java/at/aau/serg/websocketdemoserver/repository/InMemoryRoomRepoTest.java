package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.model.raum.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InMemoryRoomRepoTest {

    @Autowired
    private InMemoryRoomRepo roomRepo;

    private Room testRoom;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testRoom = new Room(4, "testRoom");
        testPlayer = new Player("testPlayer");
        roomRepo.addRoom(testRoom);
        roomRepo.addPlayerToRoom(testRoom.getRoomID(), testPlayer);
    }

    @Test
    void testAddRoom() {
        Room newRoom = new Room(5, "newRoom");
        roomRepo.addRoom(newRoom);
        assertTrue(roomRepo.getAllRooms().contains(newRoom));
    }

    @Test
    void testRemoveRoom() {
        roomRepo.removeRoom(testRoom);
        assertFalse(roomRepo.getAllRooms().contains(testRoom));
    }

    @Test
    void testFindRoomById() {
        Room foundRoom = roomRepo.findRoomById(testRoom.getRoomID());
        assertEquals(testRoom, foundRoom);
    }

    @Test
    void testFindRoomByName() {
        Room foundRoom = roomRepo.findRoomByName(testRoom.getRoomName());
        assertEquals(testRoom, foundRoom);
    }

    @Test
    void testGetPlayerById() {
        Player foundPlayer = roomRepo.getPlayerById(testPlayer.getPlayerID());
        assertEquals(testPlayer, foundPlayer);
    }

    @Test
    void testGetAllRooms() {
        Set<Room> allRooms = new HashSet<>();
        Set<Room> actualRooms = roomRepo.getAllRooms();
        for (Room room : actualRooms)
            allRooms.add(room);

        System.out.println("Expected: " + allRooms);
        System.out.println("Actual: " + actualRooms);

        List<Room> allRoomsList = new ArrayList<>(allRooms);
        List<Room> actualRoomsList = new ArrayList<>(actualRooms);

        assertEquals(allRoomsList, actualRoomsList);
    }

    @Test
    void testListAllRooms() {
        Set<Room> allRooms = new HashSet<>();

        List<Room> actualRooms = roomRepo.listAllRooms();
        for (Room room : actualRooms)
            allRooms.add(room);

        List<Room> sortedActualRooms = new ArrayList<>(actualRooms);

        Set<Room> sortedSet = new HashSet<>(sortedActualRooms);

        assertEquals(allRooms, sortedSet);
    }

    @Test
    void testAddPlayerToRoom() {
        Player newPlayer = new Player("newPlayer");
        roomRepo.addPlayerToRoom(testRoom.getRoomID(), newPlayer);
        assertEquals(testRoom.getPlayerById(newPlayer.getPlayerID()), newPlayer);
    }

    @AfterEach
    void tearDown() {
        roomRepo.removeRoom(testRoom);
        testRoom = null;
        testPlayer = null;
    }
}


