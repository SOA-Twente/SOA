package com.QuackAttack.TimelineApp.objects;

import lombok.Getter;
import lombok.Setter;

public class RequestFollowingForm {

    @Setter
    private int user_id;

    public RequestFollowingForm(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return this.user_id;
    }
}