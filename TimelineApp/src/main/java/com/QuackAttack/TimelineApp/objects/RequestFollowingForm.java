package com.QuackAttack.TimelineApp.objects;

import lombok.Getter;
import lombok.Setter;

public class RequestFollowingForm {

    @Getter @Setter
    private int user_id;

    public RequestFollowingForm(int user_id) {
    }
}