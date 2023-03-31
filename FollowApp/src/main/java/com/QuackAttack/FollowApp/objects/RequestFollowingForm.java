package com.QuackAttack.FollowApp.objects;

import lombok.Getter;
import lombok.Setter;

public class RequestFollowingForm {

    @Getter @Setter
    private int user_id;

    public int getUser_id() {
        return this.user_id;
    }
}
