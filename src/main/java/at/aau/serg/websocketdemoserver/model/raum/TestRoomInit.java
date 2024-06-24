package at.aau.serg.websocketdemoserver.model.raum;

import at.aau.serg.websocketdemoserver.model.game.Player;
import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;

public class TestRoomInit {


    public static void initTestRooms(InMemoryRoomRepo roomRepo) {
        String playerName = "FranzSissi";
        String playerName2 = "Daniel";
        Player player1 = new Player(playerName);
        Player player2 = new Player(playerName2);
        Room testRoom1 = new Room(2, "TestRoom");
        testRoom1.setCreatorName(playerName);
        testRoom1.addPlayer(player1);
        testRoom1.addPlayer(player2);
        Room testRoom2 = new Room(3, "TestRo2");
        testRoom2.setCreatorName(playerName);
        testRoom2.addPlayer(player1);
        Room testRoom3 = new Room(2, "TestRo3");
        testRoom3.setCreatorName(playerName);
        testRoom3.addPlayer(player1);
        testRoom3.addPlayer(player2);
        //testRoom3 is used, ob a full room is transferred or not



        //add
        roomRepo.addRoom(testRoom1);
        roomRepo.addRoom(testRoom2);
        roomRepo.addRoom(testRoom3);
    }
}
