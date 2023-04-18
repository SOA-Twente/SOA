package com.QuackAttack.DirectMessageConsumer.objects;


public class Message {

    private int ID;
    private int convoID;
    private String sender;
    private String receiver;
    private String message;
    private String createdAt;
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getConvoID() {
        return convoID;
    }

    public void setConvoID(int convoID) {
        this.convoID = convoID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
