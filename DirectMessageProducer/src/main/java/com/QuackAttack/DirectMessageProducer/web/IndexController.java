package com.QuackAttack.DirectMessageProducer.web;

import com.QuackAttack.DirectMessageProducer.objects.GetConvoRequest;
import com.QuackAttack.DirectMessageProducer.objects.MessageRequest;
import com.QuackAttack.DirectMessageProducer.producer.DirectMessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private DirectMessageProducerService producerService;

    /**
     * Sends a get conversation request to its message queue.
     * @param request for a conversation with an initiator and receiver.
     * @return // TODO implement some identifier to send the information back to
     */
    @GetMapping("/getConvo")
    public ResponseEntity sendGetConversation(@RequestBody GetConvoRequest request) {
        try {
            producerService.addGetConvoQueue(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Failed in sending the request to the get conversation queue: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Sends a create a conversation request its message queue.
     * @param request for a conversation with an initiator and receiver.
     * @return // TODO implement some identifier to send the information back to
     */
    @PostMapping("/createConvo")
    public ResponseEntity sendCreateConvo(@RequestBody GetConvoRequest request) {
        try {
            producerService.addGetConvoQueue(request);
            return ResponseEntity.ok().body("Conversation created");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed in creating a conversation" + e);
        }
    }

    /**
     * Sends a message request to its message queue
     * @param request for a message with a conversation ID,
     * @return // TODO implement some identifier to send the information back to
     */
    @PostMapping("/sendMsg")
    public ResponseEntity sendMessageRequest(@RequestBody MessageRequest request) {
        try {
            producerService.addMsgRequestQueue(request);
            return ResponseEntity.ok().body("Message sent successfully");
        } catch (Exception e) {
            System.out.println("Failed in requesting the timeline: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
