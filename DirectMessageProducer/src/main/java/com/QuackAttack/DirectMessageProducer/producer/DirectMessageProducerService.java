package com.QuackAttack.DirectMessageProducer.producer;

import com.QuackAttack.DirectMessageProducer.objects.CreateConversationRequest;
import com.QuackAttack.DirectMessageProducer.objects.GetConversationRequest;
import com.QuackAttack.DirectMessageProducer.objects.MessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageProducerService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    // TODO give the actual queue name in app properties
    @Value("${createConversation.queue}")
    private String createConvoQ;
    @Value("${getConversation.queue}")
    private String getConvoQ;
    @Value("${sendMessage.queue}")
    private String sendMsgQ;


    /**
     * addQueue will send a message to the specified message queue.
     * @param request is received from the UI, this is a RequestTimelineForm
     */
    public void addGetConvoQueue(GetConversationRequest request, String correlationID) {
        try {
            System.out.println("Attempting to send request to queue:" + getConvoQ);
            String jsonPayload = toJson(request); // Convert object to JSON string

            // Set content-type to application/json, this is necessary for consuming
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");

            Message message = new Message(jsonPayload.getBytes(), messageProperties);
            rabbitTemplate.convertAndSend(getConvoQ, message);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue: " + e);
        }
    }

    public void addCreateConvoQueue(CreateConversationRequest request, String correlationID) {
        try {
            System.out.println("Attempting to send request to queue:" + createConvoQ);
            String jsonPayload = toJson(request); // Convert object to JSON string

            // Set content-type to application/json, this is necessary for consuming
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");

            Message message = new Message(jsonPayload.getBytes(), messageProperties);
            rabbitTemplate.convertAndSend(createConvoQ, message);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue: " + e);
        }
    }

    public void addMsgRequestQueue(MessageRequest request, String correlationID) {
        System.out.println("Attempting to send request to queue:" + sendMsgQ);
        String jsonPayload = toJson(request); // Convert object to JSON string

        // Set content-type to application/json, this is necessary for consuming
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");

        Message message = new Message(jsonPayload.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend(sendMsgQ, message);
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
