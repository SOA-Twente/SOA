package com.QuackAttack.DirectMessageConsumer.producer;

import com.QuackAttack.DirectMessageConsumer.objects.*;
import com.QuackAttack.DirectMessageConsumer.websockets.MyWebSocketHandler;
import com.QuackAttack.DirectMessageConsumer.websockets.WebSocketConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


import java.io.IOException;
import java.util.List;

@Component
public class DirectMessageConsumerService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MyWebSocketHandler webSocketHandler;

    @Autowired
    private WebSocketConfig webSocketConfig;

    /**
     * This JmsListener listens on the "create a conversation" queue. It takes requests from that queue and first checks
     * if a conversation with the 2 users already exists, if so, it returns a message that the conversation already
     * exists. Else, it will create a new entry in the database for the conversation and return a message that the
     * conversation has been created.
     *
     * @param request it receives from the message queue.
     */
    @RabbitListener(queues = "${createConversation.queue}")
    public void createConvo(CreateConversationRequest request) throws IOException, InterruptedException {
        System.out.println(request.getReceiver() + ":" + request.getInitiator());

        String correlationID = request.getCorrelationID();

        StringBuilder response = new StringBuilder();

        // create a conversation with the request values, but first check if the conversation exists
        List<Conversation> conversations = doesConvoExists(request);

        // if the conversation does not exist, create a new entry with request values
        if (conversations.size() == 0) {
            System.out.println("Conversation does not exist, creating new conversation");
            String sql = "INSERT INTO conversations (UserInitiator, UserReceiver) VALUES (?, ?)";
            Object[] params = {request.getInitiator(), request.getReceiver()};
            int rows = jdbcTemplate.update(sql, params);

            if (rows > 0) {
                response = new StringBuilder("A new conversation has been created");
            } else {
                response = new StringBuilder("An error in creating a conversation occurred");
            }

        } else {
            response = new StringBuilder("Conversation already exists.");
        }

        if (MyWebSocketHandler.getRegistry().containsKey(correlationID)) {
            WebSocketSession session = MyWebSocketHandler.getRegistry().get(correlationID);
            session.sendMessage(new TextMessage(response));
            session.close();

        } else {
            requeueRequest(request);
        }
    }


    /**
     * This JmsListener listens on the "get conversation" queue. It takes requests from that queue and first checks
     * if the conversations exists, if it exists, it will then select the messages from the database with a matching
     * conversation ID. It will then return a list of messages to the original requester. // TODO to be implemented
     *
     * @param request a conversation request.
     */
    @RabbitListener(queues = "${getConversation.queue}")
    public void getConvo(GetConversationRequest request) throws IOException, InterruptedException {

        String correlationID = request.getCorrelationID();
        System.out.println("get convo consumer");

        StringBuilder response = null;
        List<Conversation> conversations = doesConvoExists(request);
        if (conversations.size() > 0) {
            // return list of messages from the messages table
            int conversationID = conversations.get(0).getConvoID();

            String sql = "SELECT * FROM messages WHERE convoID = ?";

            try {
                List<Message> messages = jdbcTemplate.query(sql
                        , BeanPropertyRowMapper.newInstance(Message.class)
                        , conversationID);

                response = new StringBuilder();
                response.append("ConversationID:").append(conversationID).append(", messages:");

                for (Message m : messages) {
                    response.append("~").append(m.getMessage());
                }

            } catch (DataAccessException e) {
                response = new StringBuilder("Error querying messages from request init:" + request.getInitiator()
                        + ", receiver: " + request.getReceiver() + " and convoID: " + conversationID);

                throw new RuntimeException(e);
            }

        } else {
            response = new StringBuilder("Conversation does not yet exist, please first make a conversation");

        }

        if (MyWebSocketHandler.getRegistry().containsKey(correlationID)) {
            WebSocketSession session = MyWebSocketHandler.getRegistry().get(correlationID);
            session.sendMessage(new TextMessage(response));
            session.close();

        } else {
            requeueRequest(request);
        }

    }

    @RabbitListener(queues = "${sendMessage.queue}")
    public void sendMsg(MessageRequest request) throws IOException {
        String correlationID = request.getCorrelationID();

        StringBuilder response = null;

        String sql = "INSERT INTO messages (convoID, sender, receiver, message) VALUES ( ?, ?, ?, ?)";

        try {
            int rows = jdbcTemplate.update(sql, request.getConvoID(), request.getSender(), request.getReceiver(), request.getMessage());
            if (rows > 0) {
                // message was send successfully
                System.out.println("message was sent");
                response = new StringBuilder("message was sent");

            } else {
                response = new StringBuilder("message was not sent, error location is direct quack");
            }

        } catch (DataAccessException e) {
            response = new StringBuilder(
                    "Error sending the message for request: " + request.getReceiver() +
                            ", sender: " + request.getSender() +
                            ", receiver: " + request.getReceiver() +
                            ", message : " + request.getMessage());
        }

        if (MyWebSocketHandler.getRegistry().containsKey(correlationID)) {
            WebSocketSession session = MyWebSocketHandler.getRegistry().get(correlationID);
            session.sendMessage(new TextMessage(response));
            session.close();

        } else {
            requeueRequest(request);
        }
    }


    /**
     * Help function to check if the conversation already exists in the database.
     *
     * @param request for a conversation with a conversation ID.
     * @return a list containing the conversation.
     */
    public List<Conversation> doesConvoExists(Request request) {


        String sqlIfExist = "SELECT convoID FROM conversations WHERE " +
                "(UserInitiator = ? AND UserReceiver = ?) OR (UserInitiator = ? AND UserReceiver = ?)";

        return jdbcTemplate.query(sqlIfExist
                , BeanPropertyRowMapper.newInstance(Conversation.class)
                , request.getInitiator(), request.getReceiver(), request.getReceiver(), request.getInitiator());
    }

    /**
     * Help function to requeue if the websocket connection does not exist in registry.
     * Checks the class it belongs to and re-queues the request.
     *
     * @param request to requeue
     */
    public void requeueRequest(Request request) {

        String jsonPayload = toJson(request); // Convert object to JSON string

        // Set content-type to application/json, this is necessary for consuming
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");

        org.springframework.amqp.core.Message message = new org.springframework.amqp.core.Message(jsonPayload.getBytes(), messageProperties);

        if (request.getClass() == CreateConversationRequest.class) {
            rabbitTemplate.convertAndSend("${createConversation.queue}", message);
        } else if (request.getClass() == GetConversationRequest.class) {
            rabbitTemplate.convertAndSend("${getConversation.queue}", message);
        } else {
            rabbitTemplate.convertAndSend("${sendMessage.queue}", message);
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
