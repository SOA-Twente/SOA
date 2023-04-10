package com.QuackAttack.DirectMessageProducer.producer;

import com.QuackAttack.DirectMessageProducer.objects.GetConvoRequest;
import com.QuackAttack.DirectMessageProducer.objects.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DirectMessageProducerService {

    @Autowired
    JmsTemplate jmsTemplate;

    // TODO give the actual queue name in app properties
    @Value("${active-mq.direct-message-queue}")
    private String directMessageQueue;

    @Value("${active-mq.create-conversation-queue}")
    private String createConvoQueue;

    @Value("${active-mq.send-msg-queue}")
    private String sendMsgQueue;

    /**
     * addQueue will send a message to the specified message queue.
     * @param request is received from the UI, this is a RequestTimelineForm
     */
    public void addGetConvoQueue(GetConvoRequest request) {
        try {
            log.info("Attempting to send request to queue:" + directMessageQueue);

            jmsTemplate.convertAndSend(directMessageQueue, request);
        } catch (Exception e) {
            log.error("Received Exception during sending to queue: " + e);
        }
    }

    public void addCreateConvoQueue(GetConvoRequest request) {
        try {
            log.info("Attempting to send request to queue:" + createConvoQueue);

            jmsTemplate.convertAndSend(createConvoQueue, request);
        } catch (Exception e) {
            log.error("Received Exception during sending to queue: " + e);
        }
    }

    public void addMsgRequestQueue(MessageRequest request) {
        try {
            log.info("Attempting to send request to queue:" + sendMsgQueue);

            jmsTemplate.convertAndSend(sendMsgQueue, request);
        } catch (Exception e) {
            log.error("Received Exception during sending to queue: " + e);
        }
    }
}
