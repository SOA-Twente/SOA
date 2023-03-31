package com.QuackAttack.TimelineApp.objects;

import lombok.Getter;
import lombok.Setter;

public class RequestTimelineForm {

    @Getter @Setter
    private int user_id;

    public int getUser_id() {
        return this.user_id;
    }

}
