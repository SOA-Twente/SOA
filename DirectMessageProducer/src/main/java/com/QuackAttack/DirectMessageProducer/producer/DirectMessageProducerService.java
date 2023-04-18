package com.QuackAttack.DirectMessageProducer.producer;

import com.QuackAttack.DirectMessageProducer.objects.CreateConversationRequest;
import com.QuackAttack.DirectMessageProducer.objects.GetConversationRequest;
import com.QuackAttack.DirectMessageProducer.objects.MessageRequest;
import com.QuackAttack.DirectMessageProducer.objects.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageProducerService {

    final RabbitTemplate rabbitTemplate;

    // TODO give the actual queue name in app properties
    @Value("${createConversation.queue}")
    private String createConversationQueue;
    @Value("${getConversation.queue}")
    private String getConversationQueue;
    @Value("${sendMessage.queue}")
    private String sendMessageQueue;

    public DirectMessageProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    /**
     * addQueue will send a message to the specified message queue.
     * @param request is received from the UI, this is a RequestTimelineForm
     */
    public void addGetConversationQueue(GetConversationRequest request) {
        sendToQueue(request, getConversationQueue);
    }

    public void addCreateConversationQueue(CreateConversationRequest request) {
        sendToQueue(request, createConversationQueue);
    }

    public void addMsgRequestQueue(MessageRequest request) {
        sendToQueue(request, sendMessageQueue);
    }

    private void sendToQueue(Request request, String queue) {
        try {
            System.out.println("Attempting to send request to queue:" + queue);
            String jsonPayload = toJson(request); // Convert object to JSON string

            // Set content-type to application/json, this is necessary for consuming
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");

            Message message = new Message(jsonPayload.getBytes(), messageProperties);
            rabbitTemplate.convertAndSend(queue, message);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue" + queue + ": " + e);
        }
    }

    // Utility method to convert object to JSON string, RabbitMQ doesn't like sending objects straight up
    private String toJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
