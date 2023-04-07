package com.example.TimelineProducer.producer;

import com.example.TimelineProducer.objects.RequestTimelineForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

// sends the request from the UI to the message queue, aka produces the tasks
@Component
@Slf4j
public class TimelineProducerService {

    @Autowired
    JmsTemplate jmsTemplate;

    // TODO give the actual queue name in app properties
    @Value("${active-mq.queue}")
    private String queue;

    /**
     * addQueue will send a message to the specified message queue.
     * @param request is received from the UI, this is a RequestTimelineForm
     */
    public void addQueue(RequestTimelineForm request) {
        try {
            log.info("Attempting to send request to queue:" + queue);

            jmsTemplate.convertAndSend(queue, request);
        } catch (Exception e) {
            log.error("Received Exception during sending to queue: " + e);
        }
    }
}
