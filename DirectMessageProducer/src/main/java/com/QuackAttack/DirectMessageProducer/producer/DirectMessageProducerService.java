package com.QuackAttack.DirectMessageProducer.producer;

import com.QuackAttack.DirectMessageProducer.objects.GetConvoRequest;
import com.QuackAttack.DirectMessageProducer.objects.MessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageProducerService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    // TODO give the actual queue name in app properties
    @Value("${directmessage.queue}")
    private String directMessageQueue;



    /**
     * addQueue will send a message to the specified message queue.
     * @param request is received from the UI, this is a RequestTimelineForm
     */
    public void addGetConvoQueue(GetConvoRequest request) {
        try {
            System.out.println("Attempting to send request to queue:" + directMessageQueue);
            String jsonPayload = toJson(request); // Convert object to JSON string
            rabbitTemplate.convertAndSend(directMessageQueue, jsonPayload);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue: " + e);
        }
    }

    public void addCreateConvoQueue(GetConvoRequest request) {
        try {
            System.out.println("Attempting to send request to queue:" + directMessageQueue);
            String jsonPayload = toJson(request); // Convert object to JSON string
            rabbitTemplate.convertAndSend(directMessageQueue, jsonPayload);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue: " + e);
        }
    }

    public void addMsgRequestQueue(MessageRequest request) {
        try {
            System.out.println("Attempting to send request to queue:" + directMessageQueue);
            String jsonPayload = toJson(request); // Convert object to JSON string
            rabbitTemplate.convertAndSend(directMessageQueue, jsonPayload);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue: " + e);
        }
    }

    // Utility method to convert object to JSON string, RabbitMQ doesn't like me sending objects straight up
    private String toJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}