package com.QuackAttack.DirectMessageProducer.objects;


public class MessageRequest implements Request{

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public Object getInitiator() {
        return null;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String correlationID;
    private int convoID;
    private String sender;
    private String receiver;
    private String message;
}
