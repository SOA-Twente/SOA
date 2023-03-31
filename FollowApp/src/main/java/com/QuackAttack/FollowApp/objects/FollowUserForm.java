package com.QuackAttack.FollowApp.objects;

import lombok.Getter;
import lombok.Setter;

public class FollowUserForm {

    @Setter
    private int sender;
    @Setter
    private int target;

    public int getSender() {
        return this.sender;
    }

    public int getTarget() {
        return this.target;
    }


}
