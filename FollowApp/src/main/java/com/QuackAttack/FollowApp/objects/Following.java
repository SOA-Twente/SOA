package com.QuackAttack.FollowApp.objects;

import lombok.Getter;
import lombok.Setter;

public class Following {

    @Getter @Setter
    private int ID;
    @Getter @Setter
    private int user_ID;
    @Getter @Setter
    private int following_ID;

    public int getID() {
        return this.ID;
    }
    public int getUser_ID() {
        return this.user_ID;
    }

}
