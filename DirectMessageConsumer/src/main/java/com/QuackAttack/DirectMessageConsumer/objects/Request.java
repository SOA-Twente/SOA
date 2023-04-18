package com.QuackAttack.DirectMessageConsumer.objects;

public interface Request {
    String correlationID = null;
    String initiator = null;
    String receiver = null;
    boolean requeued = false;
    String response = null;

    String getResponse();

    boolean isRequeued();
    void setRequeued(boolean requeued);

    Object getInitiator();

    Object getReceiver();
}
