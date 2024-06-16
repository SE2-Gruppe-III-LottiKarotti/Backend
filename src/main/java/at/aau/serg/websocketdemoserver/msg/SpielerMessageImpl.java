package at.aau.serg.websocketdemoserver.msg;

public class SpielerMessageImpl implements BaseMessageImpl {

    //private final MessageType messageType = MessageType.SPIELER;
    MessageType messageType;
    private ActionType actionType;
    private String spielerID;
    private String name;

    //constructor or default constructor???
    public SpielerMessageImpl() {

    }

    public enum ActionType {
        LOGIN, LOGIN_OK, LOGIN_ERR
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getSpielerID() {
        return spielerID;
    }

    public void setSpielerID(String spielerID) {
        this.spielerID = spielerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public MessageType getMessageType() {
        return messageType = MessageType.DRAW_CARD;
    }
}
