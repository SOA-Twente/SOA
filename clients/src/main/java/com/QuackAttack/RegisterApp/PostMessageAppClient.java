package com.QuackAttack.RegisterApp;

import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;

import java.util.List;


/**
 * This is the client for the PostMessageApp
 */
public interface PostMessageAppClient {

    static PostMessageAppClient create() {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(com.QuackAttack.RegisterApp.PostMessageAppClient.class, "http://post-message-app-sev.production:8081");
    }

    @RequestLine("GET /searchQuacks/{search}/{number}")
    SearchResultsQuack searchMessages(@Param("search") String search, @Param("number") int number);

}
