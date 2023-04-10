package com.QuackAttack.DirectMessageApp.objects;

import lombok.Getter;
import lombok.Setter;

public class GetConvoRequest {
    public int getInitiator() {
        return initiator;
    }

    public void setInitiator(int initiator) {
        this.initiator = initiator;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    private int initiator;
    private int receiver;
}
