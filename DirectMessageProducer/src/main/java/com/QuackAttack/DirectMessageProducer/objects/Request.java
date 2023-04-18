package com.QuackAttack.DirectMessageProducer.objects;

public interface Request {
    String correlationID = null;
    String initiator = null;
    String receiver = null;
    boolean requeued = false;

    Object getInitiator();

    Object getReceiver();
}
