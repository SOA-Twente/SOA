package com.QuackAttack.DirectMessageConsumer.objects;

import lombok.Getter;
import lombok.Setter;

public class Conversation {
    @Getter @Setter
    private int convoID;

    @Getter @Setter
    private int userInitiator;

    @Getter @Setter
    private int userReceiver;
}
