package com.example.TimelineProducer.web;

import com.example.TimelineProducer.service.TimelineProducerService;
import com.example.TimelineProducer.objects.RequestTimelineForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    final
    TimelineProducerService timelineProducerService;

    public IndexController(TimelineProducerService timelineProducerService) {
        this.timelineProducerService = timelineProducerService;
    }

    @PostMapping("/timeline")
    public void produceTimeline(@RequestBody RequestTimelineForm requestTimelineForm) {
        timelineProducerService.produceTimeline(requestTimelineForm);
    }


}
