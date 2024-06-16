package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Player;
import lombok.Data;

import java.util.ArrayList;

@Data
public class RoomMessage implements BaseMessageImpl {

   // private final MessageType messageType = MessageType.GAMEBOARD;
 MessageType messageType;

 private RoomMessage.ActionType actionType;


 private String roomID;
 private String roomName;
 private ArrayList<Player> listOfPlayers;
 private int maxPlayers;
 private Gameboard gameboard;
 private Player currentPlayer; // oder spielerID ...
 private Player nextPlayer;
 private Player addPlayer;
 private String randomCart;
 private int playerIndex;

 public RoomMessage() {
  //default
 }

 public enum ActionType {
  OPENROOM, JOINROOM, GAMEPLAY, DRAWCARD, CHAT, SETUPFIELD, GUESSCHEATER, NEXTPlAYER,
 }

 @Override
 public MessageType getMessageType() {
  return messageType = MessageType.GAMEBOARD;
 }

}
