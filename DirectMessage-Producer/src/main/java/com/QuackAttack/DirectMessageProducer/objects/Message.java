package com.QuackAttack.DirectMessageApp.objects;

import lombok.Getter;
import lombok.Setter;

public class Message {
    @Getter @Setter
    private int ID;
    @Getter @Setter
    private int convoID;
    @Getter @Setter
    private String sender;
    @Getter @Setter
    private String receiver;
    @Getter @Setter
    private String message;
}
