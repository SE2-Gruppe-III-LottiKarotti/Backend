package at.aau.serg.websocketdemoserver.messaging.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StompMessage {

        // FIXME variable naming, no idea what this does or is supposed to hold
        private String from;
        private String text;

        // lombok's Data annotation generates getter, setter, constructor, and Object methods
        // https://www.educative.io/answers/what-is-the-data-annotation-in-lombok

}
