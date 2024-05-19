package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

@Data
public class BaseMessage {
    private MessageType messageType;
}
