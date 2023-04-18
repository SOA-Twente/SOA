package com.QuackAttack.DirectMessageConsumer.objects;
public class CreateConversationRequest implements Request{

    private String correlationID;

    private String initiator;
    private String receiver;
    private int convoID;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private String response;
    private boolean requeued;

    public int getConvoID() {
        return convoID;
    }

    public void setConvoID(int convoID) {
        this.convoID = convoID;
    }

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

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public boolean isRequeued() {
        return requeued;
    }

    public void setRequeued(boolean requeued) {
        this.requeued = requeued;
    }

}
