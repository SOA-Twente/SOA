package com.QuackAttack.DirectMessageConsumer.websockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Component("myWebSocketHandler")
public class MyWebSocketHandler implements WebSocketHandler {

    public static ConcurrentHashMap<String, WebSocketSession> getRegistry() {
        return registry;
    }

    private static final ConcurrentHashMap<String, WebSocketSession> registry = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String correlationID = Objects.requireNonNull(session.getUri()).getPath().split("/")[3]; // extract correlationId from WebSocket URL

        registry.put(correlationID, session);
        System.out.println("last registry entry:" + correlationID);
        System.out.println("registry: " + registry);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        String receivedMessage = message.getPayload().toString();

        if (receivedMessage.startsWith("received")) {
            String correlationID = Objects.requireNonNull(session.getUri()).getPath().split("/")[3]; // extract correlationId from WebSocket URL
            System.out.println("correlationID found from path by server side: " + correlationID);
            registry.remove(correlationID, session);
            System.out.println("CorrelationID removed: " + correlationID);

            try {
                System.out.println("Closing session for correlationID: " + correlationID);
                session.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        // Handle WebSocket transport error
        // ...
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        // Handle WebSocket connection closure

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
