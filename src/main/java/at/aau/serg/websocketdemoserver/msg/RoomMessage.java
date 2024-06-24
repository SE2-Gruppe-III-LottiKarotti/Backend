package at.aau.serg.websocketdemoserver.msg;

import at.aau.serg.websocketdemoserver.model.game.Gameboard;
import at.aau.serg.websocketdemoserver.model.game.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

//TODO: Delete this unused class in next refactoring

@Data
@EqualsAndHashCode(callSuper = true)
public class RoomMessage extends BaseMessage {


 private RoomMessage.ActionType actionType;


 private String roomID;
 private String roomName;
 private ArrayList<Player> listOfPlayers;
 private int maxPlayers;
 private Gameboard gameboard;
 private Player currentPlayer; //or player id both can be used
 private Player nextPlayer;
 private Player addPlayer;
 private String randomCart;
 private int playerIndex;

 public RoomMessage() {
  //default
  this.messageType = MessageType.ROOM;
 }

 public enum ActionType {
  OPENROOM, JOINROOM, GAMEPLAY, DRAWCARD, CHAT, SETUPFIELD, GUESSCHEATER, NEXTPlAYER,
 }
}
