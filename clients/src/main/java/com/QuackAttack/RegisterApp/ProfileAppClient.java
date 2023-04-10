package com.QuackAttack.RegisterApp;

import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;

/**
 * This is the client for the ProfileApp
 */
public interface ProfileAppClient {

    static ProfileAppClient create() {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(com.QuackAttack.RegisterApp.ProfileAppClient.class, "http://localhost:8080");
    }

    @RequestLine("POST /addUserProfile")
    @Headers("Content-Type: application/json")
    void addUserProfile(UserProfile userProfile);

}
