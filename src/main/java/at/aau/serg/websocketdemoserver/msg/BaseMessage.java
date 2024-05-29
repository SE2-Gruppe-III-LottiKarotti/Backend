package at.aau.serg.websocketdemoserver.msg;

import lombok.Data;

// FIXME Introduce an interface for all messages!
@Data
public class BaseMessage {
    private MessageType messageType;
}
