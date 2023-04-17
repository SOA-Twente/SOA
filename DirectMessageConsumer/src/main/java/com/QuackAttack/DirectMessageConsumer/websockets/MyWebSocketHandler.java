package com.QuackAttack.DirectMessageConsumer.websockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.concurrent.ConcurrentHashMap;


@Component("myWebSocketHandler")
public class MyWebSocketHandler implements WebSocketHandler {

    public static ConcurrentHashMap<String, WebSocketSession> getRegistry() {
        return registry;
    }

    private static ConcurrentHashMap<String, WebSocketSession> registry = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String correlationID = session.getUri().getPath().split("/")[3]; // extract correlationId from WebSocket URL
        registry.put(correlationID, session);
        System.out.println( "last registry entry:"+ correlationID);
        System.out.println("registry: " + registry.toString());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String receivedMessage = message.getPayload().toString();

        if (receivedMessage.startsWith("received")) {
            String correlationID = session.getUri().getPath().split("/")[3]; // extract correlationId from WebSocket URL
            registry.remove(correlationID, session);
            System.out.println("CorrelationID removed: " + correlationID);
            session.close();
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle WebSocket transport error
        // ...
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // Handle WebSocket connection closure

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
