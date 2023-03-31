package com.QuackAttack.TimelineApp.objects;

import lombok.Getter;
import lombok.Setter;

public class Following {

    @Getter
    @Setter
    private int ID;
    @Getter @Setter
    private int user_ID;
    @Getter @Setter
    private int following_ID;

    public int getID() {
        return this.ID;
    }
}
