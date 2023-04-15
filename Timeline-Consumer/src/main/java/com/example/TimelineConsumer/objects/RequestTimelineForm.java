package com.example.TimelineConsumer.objects;

import lombok.Getter;
import lombok.Setter;

public class RequestTimelineForm {

    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
