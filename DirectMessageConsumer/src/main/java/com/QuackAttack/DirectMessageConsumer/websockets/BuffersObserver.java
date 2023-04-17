package com.QuackAttack.DirectMessageConsumer.websockets;

import com.QuackAttack.DirectMessageConsumer.consumer.DirectMessageConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
@Component
public class BuffersObserver {

    @Autowired
    private DirectMessageConsumerService consumerService;

    @Scheduled(fixedDelay = 500)
    public void observeBuffers() throws IOException {

        for (String correlationID : MyWebSocketHandler.getRegistry().keySet()) {
            System.out.println("BUFFER COMPARE" + correlationID);
            // if the results contain a correlationID, send the message (from the results) to the session, found in the registry
            if (consumerService.getResults().containsKey(correlationID)) {
                WebSocketSession session = MyWebSocketHandler.getRegistry().get(correlationID);
                if (session.isOpen()) {
                    System.out.println("result found, sending...");
                    String confirmationMessage = "Confirmation: " + consumerService.getResults().get(correlationID);
                    session.sendMessage(new TextMessage(confirmationMessage));
                    consumerService.getResults().remove(correlationID);
                    MyWebSocketHandler.getRegistry().remove(correlationID);
                }
            }
        }
    }
}
