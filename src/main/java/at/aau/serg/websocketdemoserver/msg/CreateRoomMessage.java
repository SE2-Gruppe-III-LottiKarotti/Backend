package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class CreateRoomMessage {
    String roomName;
    String playerName;
}
