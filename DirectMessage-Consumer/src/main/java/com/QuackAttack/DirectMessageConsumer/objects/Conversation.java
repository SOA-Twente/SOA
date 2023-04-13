package com.QuackAttack.DirectMessageConsumer.objects;


public class Conversation {

    public int getConvoID() {
        return convoID;
    }

    public void setConvoID(int convoID) {
        this.convoID = convoID;
    }

    public int getUserInitiator() {
        return userInitiator;
    }

    public void setUserInitiator(int userInitiator) {
        this.userInitiator = userInitiator;
    }

    public int getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(int userReceiver) {
        this.userReceiver = userReceiver;
    }

    private int convoID;


    private int userInitiator;


    private int userReceiver;
}
