package com.QuackAttack.DirectMessageConsumer.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Component("myWebSocketHandler")
public class MyWebSocketHandler implements WebSocketHandler {

    Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    public static ConcurrentHashMap<String, WebSocketSession> getRegistry() {
        return registry;
    }
    private static final ConcurrentHashMap<String, WebSocketSession> registry = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String correlationID = Objects.requireNonNull(session.getUri()).getPath().split("/")[3]; // extract correlationId from WebSocket URL

        registry.put(correlationID, session);
        logger.info("Last registry entry: " + correlationID);
        logger.info("Current registry: " + registry);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        String receivedMessage = message.getPayload().toString();

        if (receivedMessage.startsWith("received")) {
            String correlationID = Objects.requireNonNull(session.getUri()).getPath().split("/")[3]; // extract correlationId from WebSocket URL
            logger.info("CorrelationID found from path by server side: " + correlationID);
            registry.remove(correlationID, session);
            logger.info("CorrelationID removed: " + correlationID);
            System.out.println("CorrelationID removed: " + correlationID);

            try {
                logger.info("Closing session for correlationID: " + correlationID);
                session.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("Transport error in session: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        logger.info("Closed session: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
