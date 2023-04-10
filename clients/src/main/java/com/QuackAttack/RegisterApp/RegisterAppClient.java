package com.QuackAttack.RegisterApp;

import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * This is the client for the ProfileApp
 */
public interface RegisterAppClient {
    static RegisterAppClient create() {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(RegisterAppClient.class, "http://localhost:8082");
    }

    @RequestLine("GET /searchUsername/{username}/{number}")
    SearchResults searchUsername(@Param("username") String username, @Param("number") int number);


    @RequestLine("GET /doesUserExist")
    @Headers("Cookie: credentials={credentials}")
    DoesUserExistResult doesUserExist(@Param("credentials") String credentials);
}
