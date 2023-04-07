package com.example.TimelineProducer.web;

import com.example.TimelineProducer.producer.TimelineProducerService;
import com.example.TimelineProducer.objects.RequestTimelineForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class IndexController {

    private TimelineProducerService timelineProducerService;

    public IndexController(TimelineProducerService timelineProducerService) {
        this.timelineProducerService = timelineProducerService;
    }

    /**
     * Receives a request and sends this to the producer.
     * @param requestTimelineForm to be sent to the producer.
     * @return an ok response entity or an internal server error response entity.
     */
    @PostMapping("/timeline")
    public ResponseEntity requestTimeline(@RequestBody RequestTimelineForm requestTimelineForm) {
        try {
            timelineProducerService.addQueue(requestTimelineForm);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed in requesting the timeline: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
