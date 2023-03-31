package com.QuackAttack.RegisterApp.objects;

import java.util.Objects;

public class UserData {


    private String username;
    private String password;
    private String email;
    private int id;

    public UserData(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserData(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    public UserData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
