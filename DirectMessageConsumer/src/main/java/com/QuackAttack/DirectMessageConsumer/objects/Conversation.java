package com.QuackAttack.DirectMessageConsumer.objects;


public class Conversation {

    private int convoID;

    private String userInitiator;

    private String userReceiver;


    public int getConvoID() {
        return convoID;
    }

    public void setConvoID(int convoID) {
        this.convoID = convoID;
    }

    public String getUserInitiator() {
        return userInitiator;
    }

    public void setUserInitiator(String userInitiator) {
        this.userInitiator = userInitiator;
    }

    public String getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(String userReceiver) {
        this.userReceiver = userReceiver;
    }

}