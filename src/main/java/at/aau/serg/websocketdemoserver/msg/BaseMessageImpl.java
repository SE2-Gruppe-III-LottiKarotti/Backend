package at.aau.serg.websocketdemoserver.msg;

/*@Data
public class BaseMessage {
    private MessageType messageType;
}*/

public interface BaseMessageImpl {
    MessageType getMessageType();

}
