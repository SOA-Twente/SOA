package com.QuackAttack.DirectMessageProducer.objects;

import lombok.Getter;
import lombok.Setter;

public class MessageRequest {
    @Getter @Setter
    private int convoID;
    @Getter @Setter
    private int sender;
    @Getter @Setter
    private int receiver;
    @Getter @Setter
    private String message;
}
