package at.aau.serg.websocketdemoserver.msg;

import java.util.ArrayList;


import at.aau.serg.websocketdemoserver.model.raum.RoomInfo;
import lombok.Data;

@Data
public class RoomSetupMessage {


    ActionType actionType;

    String messageIdentifier;
    String roomID;
    String roomName;
    String playerID;
    String playerName;
    String numPlayers;
    ArrayList<RoomInfo> roomInfoList;

    /*public RoomSetupMessage() {
        //default constructor
    }

    public RoomSetupMessage(String roomName, String numPlayers, String playerName) {
        this.roomName = roomName;
        this.numPlayers = numPlayers;
        this.playerName = playerName;
    }*/



    /**um eine Nachricht definitiv von allen anderen zu unterscheiden
     * wird im client eine UUID für die Nachricht generiert und beim absenden
     * mitgesendet...
     * der client merkt sich die UUID als lokale varialbe und wenn
     * der client die antwort vom Server bekommt, wird überprüft, ob
     * diese nachricht für den client bestimmt war oder nicht...*/

    public enum ActionType {
        OPEN_ROOM, OPEN_ROOM_OK, OPEN_ROOM_ERR, ROOM_FULL, ASK_FOR_ROOM_LIST, ASK_FOR_JOIN_ROOM, ANSWER_ROOM_LIST, ANSWER_JOIN_ROOM_OK, ANSWER_JOIN_ROOM_ERR
    }


}