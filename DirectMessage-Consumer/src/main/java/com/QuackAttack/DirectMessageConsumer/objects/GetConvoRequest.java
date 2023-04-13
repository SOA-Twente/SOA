package com.QuackAttack.DirectMessageConsumer.objects;

public class GetConvoRequest {
    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    private String initiator;
    private String receiver;
}
