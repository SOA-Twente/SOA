package com.QuackAttack.DirectMessageProducer.web;

import com.QuackAttack.DirectMessageProducer.objects.GetConvoRequest;
import com.QuackAttack.DirectMessageProducer.objects.MessageRequest;
import com.QuackAttack.DirectMessageProducer.producer.DirectMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller accepts requests on 3 endpoints, (1) for creating a conversation, (2) for getting a conversation,
 * (3) and for sending a message
 */
@RestController
@Slf4j
public class IndexController {

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
            log.error("Failed in sending the request to the get conversation queue: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Sends a create a conversation request its message queue.
     * @param request for a conversation with an initiator and receiver.
     * @return // TODO implement some identifier to send the information back to
     */
    @GetMapping("/createConvo")
    public ResponseEntity sendCreateConvo(@RequestBody GetConvoRequest request) {
        try {
            producerService.addGetConvoQueue(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed in sending the request to the create conversation queue: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Sends a message request to its message queue
     * @param request for a message with a conversation ID,
     * @return // TODO implement some identifier to send the information back to
     */
    @GetMapping("/sendMsg")
    public ResponseEntity sendMessageRequest(@RequestBody MessageRequest request) {
        try {
            producerService.addMsgRequestQueue(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed in requesting the timeline: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
