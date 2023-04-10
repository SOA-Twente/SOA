package com.QuackAttack.FollowApp.objects;

import lombok.Getter;
import lombok.Setter;

public class Following {

    private int id;
    private String user_id;
    private String following_id;


    public Following() {
    }

    public Following(int id, String user_id, String following_id) {
        this.id = id;
        this.user_id = user_id;
        this.following_id = following_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFollowing_id() {
        return following_id;
    }

    public void setFollowing_id(String following_id) {
        this.following_id = following_id;
    }
}
