package com.QuackAttack.DirectMessageConsumer.objects;


public class MessageRequest {
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getCallBackURL() {
        return callBackURL;
    }

    public void setCallBackURL(String callBackURL) {
        this.callBackURL = callBackURL;
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

    private String requestType;
    private String callBackURL;
    private int convoID;
    private String sender;
    private String receiver;
    private String message;
}
