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
     * Receives a request to get a conversation, sends it to the message queue where it can
     * be consumed and returned.
     * @param request for a conversation.
     * @return a http ok if succeeds, else error.
     */
    @GetMapping("/getConvo")
    public ResponseEntity requestConvo(@RequestBody GetConvoRequest request) {
        try {
            producerService.addQueue(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed in requesting the timeline: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
