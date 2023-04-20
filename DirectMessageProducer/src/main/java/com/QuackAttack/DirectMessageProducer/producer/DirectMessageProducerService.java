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

/**
 * This component handles the incoming requests and sends them to their corresponding message queues.
 */
@Component
public class DirectMessageProducerService {

    final RabbitTemplate rabbitTemplate;

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
     * Adds a GetConversationRequest to the GetConversationQueue.
     * @param request to put onto the queue.
     */
    public void addGetConversationQueue(GetConversationRequest request) {
        sendToQueue(request, getConversationQueue);
    }
    /**
     * Adds a CreateConversationRequest to the CreateConversationQueue.
     * @param request to put onto the queue.
     */
    public void addCreateConversationQueue(CreateConversationRequest request) {
        sendToQueue(request, createConversationQueue);
    }
    /**
     * Adds a MessageRequest to the sendMessageQueue.
     * @param request to put onto the queue.
     */
    public void addMsgRequestQueue(MessageRequest request) {
        sendToQueue(request, sendMessageQueue);
    }

    /**
     * Adds a request a RabbitMQ message queue.
     * @param request to put onto the queue.
     * @param queue to send to request to.
     */
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


    /**
     * Utility method to convert an object to a JSON string. Necessary to send objects to message queues.
     * @param obj to be converted to a JSON string.
     * @return a JSON string.
     */
    private String toJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
