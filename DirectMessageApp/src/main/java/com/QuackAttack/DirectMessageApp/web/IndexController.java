package com.QuackAttack.DirectMessageApp.web;

import com.QuackAttack.DirectMessageApp.auth.GoogleTokenVerifier;
import com.QuackAttack.DirectMessageApp.objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class IndexController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GoogleTokenVerifier verifier;

    @PostMapping("/startQuacking")
    public ResponseEntity startQuacking(@RequestParam GetConvoRequest request) {

        // create a conversation with the request values, but first check if the convo exists
        List<Conversation> conversations = doesConvoExists(request);

        // if the conversation does not exist, create a new entry with request values
        if (conversations.size() == 0) {
            String sql = "INSERT INTO conversations (user_initiator, user_receiver) VALUES (?, ?)";

            int rows = jdbcTemplate.update(sql, request.getInitiator(), request.getReceiver());

            if (rows > 0) {
                System.out.println("A new conversation has been created");
                return ResponseEntity.ok().build();

            } else {
                String errorResponse = "Error in creating a conversation with conversation initiator: " + request.getInitiator()
                        + ", and receiver: " + request.getReceiver();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } else {
            String errorResponse = "Error in creating a conversation. Conversation already exists!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/getQuacking")
    public ResponseEntity<List<Message>> getQuacking(@RequestParam GetConvoRequest request) {

        List<Conversation> conversations = doesConvoExists(request);

        if (conversations.size() == 1) {
            int conversationID = conversations.get(0).getConvoID();
            System.out.println(conversationID);

            String sql = "SELECT * FROM messages WHERE convoID = ?";

            try {
                List<Message> messages = jdbcTemplate.query(sql
                        , BeanPropertyRowMapper.newInstance(Message.class), conversationID);

                return ResponseEntity.ok(messages);
            } catch (DataAccessException e) {
                System.err.println("Error querying messages from request init:" + request.getInitiator()
                        + ", receiver: " + request.getReceiver()
                        + " and convoID: " + conversationID);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
            }

        } else {
            System.err.println("Conversation does not yet exist, please first make a conversation");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @PostMapping("/directQuack")
    public ResponseEntity directQuack(@RequestParam MessageRequest request) {
        String sql = "INSERT INTO messages (convo_id, sender, receiver, message) VALUES ( ?, ?, ?, ?)";

        try {
            int rows = jdbcTemplate.update(sql, request.getConvoID(), request.getSender(), request.getReceiver(), request.getMessage());
            if (rows > 0) {
                // message was send successfully
                System.out.println("message was sent");
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                System.out.println("message was not sent, error location is directquack");
                return ResponseEntity.internalServerError().build();
            }

        } catch (DataAccessException e) {
            System.err.println("Error sending the message for request: " + request.getReceiver() + ", sender: " + request.getSender()
                    + ", receiver: " + request.getReceiver() + ", message : " + request.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // TODO change the @RequestBody to @CookieValue String credentials to get the username
    @GetMapping("/listConversations")
    public ResponseEntity<List<Message>> getConversations(@RequestBody GetConvoRequest request) {

        // TODO enable this part if @CookieValue is used
        /*
        String user_id;
        try {
            user_id = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
        */

        // request the messages with the found convoID
        List<Conversation> conversations = doesConvoExists(request);

        if (conversations.size() == 1) {
            // return list of messages from the messages table
            int conversationID = conversations.get(0).getConvoID();
            System.out.println(conversationID);

            String sql = "SELECT * FROM messages WHERE (sender = ?) OR (receiver = ?)";

            try {
                List<Message> messageList = jdbcTemplate.query(sql
                        , BeanPropertyRowMapper.newInstance(Message.class), request.getInitiator(), request.getInitiator());

                Collections.sort(messageList, Comparator.comparing(Message::getConvoID)
                        .thenComparing(Message::getCreatedAt));

                return ResponseEntity.ok(messageList);
            } catch (DataAccessException e) {
                System.err.println("Error querying messages from request init:" + request.getInitiator()
                        + ", receiver: " + request.getReceiver() + " and convoID: " + conversationID);
                return ResponseEntity.internalServerError().build();
            }

        } else {
            System.out.println("Conversation does not yet exist, please first make a conversation");
            return ResponseEntity.notFound().build();
        }
    }

    public List<Conversation> doesConvoExists(GetConvoRequest request) {

        String sqlIfExist = "SELECT convo_id FROM conversations WHERE " +
                "(user_initiator = ? AND user_receiver = ?) OR (user_initiator = ? AND user_receiver = ?)";

        List<Conversation> conversationList = jdbcTemplate.query(sqlIfExist
                , BeanPropertyRowMapper.newInstance(Conversation.class)
                , new Object[]{request.getInitiator(), request.getReceiver(), request.getReceiver(), request.getInitiator()});

        return conversationList;
    }
}
