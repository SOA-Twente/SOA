package com.QuackAttack.TimelineApp.objects;

import lombok.Getter;
import lombok.Setter;

public class RequestTimelineForm {

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

    public String getUser_id() {
        return this.user_id;
    }

}
