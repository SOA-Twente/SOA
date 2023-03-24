package com.example.TimelineProducer.service;

import com.example.TimelineProducer.objects.RequestTimelineForm;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// sends the request from the UI to the message queue, aka produces the tasks
@Service
public class TimelineProducerService {

    private final RabbitTemplate rabbitTemplate;

    public TimelineProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void produceTimeline(RequestTimelineForm form) {
        rabbitTemplate.convertAndSend(form);
    }
}
