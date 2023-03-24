package com.QuackAttack.RegisterApp.objects;

public class ResponseObject {


    private boolean jwtCredentials;
    private boolean userExists;
    private String message;
    private UserData data;


    public ResponseObject(boolean jwtCredentials, boolean userExists, String message) {
        this.jwtCredentials = jwtCredentials;
        this.userExists = userExists;
        this.message = message;
    }
    public ResponseObject(boolean jwtCredentials, boolean userExists, String message, UserData data) {
        this.jwtCredentials = jwtCredentials;
        this.userExists = userExists;
        this.message = message;
        this.data = data;
    }


}
