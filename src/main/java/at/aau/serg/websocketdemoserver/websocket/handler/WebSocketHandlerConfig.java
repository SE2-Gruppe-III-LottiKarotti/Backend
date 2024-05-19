package at.aau.serg.websocketdemoserver.websocket.handler;

import at.aau.serg.websocketdemoserver.repository.InMemoryRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketHandlerConfig implements WebSocketConfigurer {


    /*
    private InMemoryRoomRepo roomRepo;

    @Autowired
    public WebSocketHandlerConfig(InMemoryRoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }*/

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //registry.addHandler(new WebSocketHandlerImpl(roomRepo), "/websocket-example-handler")
        registry.addHandler(new WebSocketHandlerImpl(), "/websocket-example-handler")
                .setAllowedOrigins("*");
    }
}

