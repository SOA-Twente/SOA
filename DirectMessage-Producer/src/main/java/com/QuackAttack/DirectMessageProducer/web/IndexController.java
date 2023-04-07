package com.QuackAttack.DirectMessageProducer.web;


import com.QuackAttack.DirectMessageApp.objects.GetConvoRequest;
import com.QuackAttack.DirectMessageProducer.producer.DirectMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class IndexController {

    private DirectMessageProducerService producerService;

    /**
     * Takes a conversation request from the message queue and consumes it.
     * @param request from the message queue.
     * @return // TODO implement some identifier to send the information back to
     */
    @GetMapping("/getConvo")
    public ResponseEntity sendGetConversation(@RequestBody GetConvoRequest request) {
        try {
            producerService.addGetConvoQueue(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed in requesting the timeline: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
