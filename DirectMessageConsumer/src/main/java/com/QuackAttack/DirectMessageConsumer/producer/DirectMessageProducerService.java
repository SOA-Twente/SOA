package com.QuackAttack.DirectMessageConsumer.producer;

import com.QuackAttack.DirectMessageProducer.objects.GetConvoRequest;
import com.QuackAttack.DirectMessageProducer.objects.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageProducerService {

    @Autowired
    JmsTemplate jmsTemplate;

    // TODO give the actual queue name in app properties
    @Value("${active-mq.direct-message-queue}")
    private String directMessageQueue;



    /**
     * addQueue will send a message to the specified message queue.
     * @param request is received from the UI, this is a RequestTimelineForm
     */
    public void addGetConvoQueue(GetConvoRequest request) {
        try {
            System.out.println("Attempting to send request to queue:" + directMessageQueue);
            jmsTemplate.convertAndSend(directMessageQueue, request);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue: " + e);
        }
    }

    public void addCreateConvoQueue(GetConvoRequest request) {
        try {
            System.out.println("Attempting to send request to queue:" + directMessageQueue);

            jmsTemplate.convertAndSend(directMessageQueue, request);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue: " + e);
        }
    }

    public void addMsgRequestQueue(MessageRequest request) {
        try {
            System.out.println("Attempting to send request to queue:" + directMessageQueue);

            jmsTemplate.convertAndSend(directMessageQueue, request);
        } catch (Exception e) {
            System.out.println("Received Exception during sending to queue: " + e);
        }
    }
}
