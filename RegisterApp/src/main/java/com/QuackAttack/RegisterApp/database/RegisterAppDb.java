package com.QuackAttack.RegisterApp.database;

import com.QuackAttack.RegisterApp.objects.UserData;

import java.util.List;

public interface RegisterAppDb {
    List<UserData> getUsers(String username);
}
