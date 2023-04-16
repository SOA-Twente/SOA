package com.QuackAttack.DirectMessageConsumer.websockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.concurrent.ConcurrentHashMap;


@Component
public class MyWebSocketHandler implements WebSocketHandler {

    public static ConcurrentHashMap<String, WebSocketSession> getRegistry() {
        return registry;
    }

    private static ConcurrentHashMap<String, WebSocketSession> registry = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {

            String correlationID = session.getUri().getPath().split("/")[3]; // extract correlationId from WebSocket URL
            registry.put(correlationID, session);

            System.out.println(registry.toString());
            session.sendMessage(new TextMessage(" hello "));
        } catch (Exception e) {
            System.out.println("Failed to get correlationID");
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Consumer does not receive websocket messages
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle WebSocket transport error
        // ...
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // Handle WebSocket connection closure
        // ...
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
