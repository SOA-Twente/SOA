package com.QuackAttack.DirectMessageProducer.web;

import com.QuackAttack.DirectMessageProducer.objects.CreateConversationRequest;
import com.QuackAttack.DirectMessageProducer.objects.GetConversationRequest;
import com.QuackAttack.DirectMessageProducer.objects.MessageRequest;
import com.QuackAttack.DirectMessageProducer.producer.DirectMessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID; // Import UUID for generating correlation ID

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class IndexController {

    @Autowired
    private DirectMessageProducerService producerService;

    /**
     * Sends a get conversation request to its message queue.
     *
     * @return ResponseEntity with correlation ID as a response header
     */
    @GetMapping("/getConvo")
    public ResponseEntity sendGetConversation(@RequestHeader String initiator,
                                              @RequestHeader String receiver,
                                              @RequestHeader int conversationID) {
        try {
            String correlationId = generateCorrelationId(); // Generate correlation ID

            GetConversationRequest request = new GetConversationRequest(initiator, receiver, correlationId);
            request.setInitiator(initiator);
            request.setReceiver(receiver);
            request.setConvoID(conversationID);
            request.setCorrelationID(correlationId);

            producerService.addGetConversationQueue(request); // Pass correlation ID to producer service
            return ResponseEntity.ok().body(correlationId);
        } catch (Exception e) {
            System.out.println("Failed in sending the request to the get conversation queue: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Sends a create a conversation request to its message queue.
     *
     * @param request for a conversation with an initiator and receiver.
     * @return ResponseEntity with correlation ID as a response header
     */
    @PostMapping("/createConvo")
    public ResponseEntity sendCreateConversation(@RequestBody CreateConversationRequest request) {
        try {
            String correlationId = generateCorrelationId(); // Generate correlation ID
            request.setCorrelationID(correlationId);
//
//
//            System.out.println(correlationId);
            producerService.addCreateConversationQueue(request); // Pass correlation ID to producer service
            System.out.println(request.toString());

            return ResponseEntity.ok().body(correlationId);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed in creating a conversation" + e);
        }
    }

    /**
     * Sends a message request to its message queue.
     *
     * @param request for a message with a conversation ID.
     * @return ResponseEntity with correlation ID as a response header
     */
    @PostMapping("/sendMsg")
    public ResponseEntity sendMessageRequest(@RequestBody MessageRequest request) {
        try {
            String correlationId = generateCorrelationId(); // Generate correlation ID
            request.setCorrelationID(correlationId); // Put correlationID into request
            producerService.addMsgRequestQueue(request); // Pass correlation ID to producer service
            return ResponseEntity.ok().body(correlationId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Generates a unique correlation ID using UUID.
     *
     * @return String representing the correlation ID
     */
    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
