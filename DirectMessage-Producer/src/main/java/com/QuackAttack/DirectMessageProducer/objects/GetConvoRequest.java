package com.QuackAttack.DirectMessageApp.objects;

import lombok.Getter;
import lombok.Setter;

public class GetConvoRequest {
    @Getter @Setter
    private int initiator;
    @Getter @Setter
    private int receiver;
}
