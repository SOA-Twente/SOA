package com.QuackAttack.TimelineApp.objects;


public class RequestFollowingForm {

    private String user_id;

    public RequestFollowingForm(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }
}