package com.QuackAttack.DirectMessageConsumer.consumer;

import com.QuackAttack.DirectMessageConsumer.objects.*;
import com.QuackAttack.DirectMessageConsumer.websockets.MyWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This service consumes requests from the message queue and sends the results back to the original requester. This
 * original requester is identified through a correlationID generated by the producer. This correlationID is used to
 * find the Websocket session of the original requester, the service sends the results and then closes the Websocket
 * session.
 */
@Component
@Order(2)
@DependsOn("myWebSocketHandler")
public class DirectMessageConsumerService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${createConversation.queue}")
    private String createConvoQ;
    @Value("${getConversation.queue}")
    private String getConvoQ;
    @Value("${sendMessage.queue}")
    private String sendMsgQ;
    @Value("${requeue.queue}")
    private String requeueQ;

    public Map<String, String> getResults() {
        return results;
    }

    private Map<String, String> results = new ConcurrentHashMap<>();

    /**
     * This JmsListener listens on the "create a conversation" queue. It takes requests from that queue and first checks
     * if a conversation with the 2 users already exists, if so, it returns a message that the conversation already
     * exists. Else, it will create a new entry in the database for the conversation and return a message that the
     * conversation has been created.
     * @param request it receives from the message queue.
     */
    @RabbitListener(queues = "${createConversation.queue}")
    public void createConvo(CreateConversationRequest request) throws IOException {
        System.out.println(request.getReceiver() + ":" + request.getInitiator());

        String correlationID = request.getCorrelationID();

        StringBuilder response;

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

        // addToResultBuffer(request, correlationID, response);
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
        System.out.println("get convo consumer first part");
        System.out.println("In registry: "  + MyWebSocketHandler.getRegistry().containsKey(correlationID));



        StringBuilder response;
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

        System.out.println("response: " + response);
        /*

        System.out.println("consumer second part");
        System.out.println("In registry: "  + MyWebSocketHandler.getRegistry().containsKey(correlationID));
        addToResultBuffer(request, correlationID, response);
        */

        if (MyWebSocketHandler.getRegistry().containsKey(correlationID)) {
            WebSocketSession session = MyWebSocketHandler.getRegistry().get(correlationID);
            session.sendMessage(new TextMessage("Confirmation:" + response));
            System.out.println("Confirmation:" + response);

        } else {
            System.out.println("requeue");
            requeueRequest(request);
        }
    }

    @RabbitListener(queues = "${sendMessage.queue}")
    public void sendMsg(MessageRequest request) throws IOException, InterruptedException {
        String correlationID = request.getCorrelationID();

        StringBuilder response;

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
            System.out.println();
            response = new StringBuilder(
                    "Error sending the message for request: " + request.getReceiver() +
                            ", sender: " + request.getSender() +
                            ", receiver: " + request.getReceiver() +
                            ", message : " + request.getMessage());
        }



    }


    /**
     * Utility method to check if the conversation already exists in the database.
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
     * Utility method to requeue if the websocket connection does not exist in registry.
     * Checks the class it belongs to and re-queues the request.
     * @param request to requeue
     */
    public void requeueRequest(Request request) {

        String jsonPayload = toJson(request); // Convert object to JSON string

        // Set content-type to application/json, this is necessary for consuming
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");

        org.springframework.amqp.core.Message message = new org.springframework.amqp.core.Message(jsonPayload.getBytes(), messageProperties);
        System.out.println("in requeue method, before check");

        if (request instanceof CreateConversationRequest) {
            System.out.println("in requeue method, in check");

            rabbitTemplate.convertAndSend(requeueQ, message);

        } else if (request instanceof GetConversationRequest) {
            System.out.println("in requeue method, in check");

            rabbitTemplate.convertAndSend(requeueQ, message);
        } else {
            System.out.println("in requeue method, in check");

            rabbitTemplate.convertAndSend(requeueQ, message);
        }
        System.out.println("in requeue method, after check");

    }


    /**
     * Utility method to convert an object to JSON string, necessary for RabbitMQ.
     * @param obj to convert to a JSON string.
     * @return JSON string.
     */
    private String toJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Utility method to send the response to the correct websocket session.
     * @param request to get the correlationID from.
     * @param correlationID to locate the websocket session with.
     * @param response to send to the websocket session.
     * @throws IOException
     */
    private void addToResultBuffer(Request request, String correlationID, StringBuilder response) throws IOException {

            StringBuilder confirmation = new StringBuilder("Confirmation");
            confirmation.append(response);
            results.put(correlationID, String.valueOf(confirmation));
            System.out.println("last result entry:" + correlationID);
            System.out.println("results:"+ results.toString());
    }
}
