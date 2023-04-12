package com.QuackAttack.SearchApp.web;


import com.QuackAttack.RegisterApp.PostMessageAppClient;
import com.QuackAttack.RegisterApp.RegisterAppClient;
import com.QuackAttack.RegisterApp.SearchResults;
import com.QuackAttack.RegisterApp.SearchResultsQuack;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class indexController {


//    private static final String SEARCH_USERNAME = "http://localhost:8082/searchUsername/";

    private RegisterAppClient registerAppClient = RegisterAppClient.create();
    private PostMessageAppClient postMessageAppClient = PostMessageAppClient.create();


    private static Gson gson = new Gson();

    /**
     * Searches with a specific number of requests for users and messages
     * @param search the search term
     * @param number the number of requests
     * @return a list of search results
     * @throws IOException
     */
    @GetMapping("/search/{search}/{number}")
    public ResponseEntity<Object> searchUsername(@PathVariable String search,@PathVariable int number) throws IOException {
        System.out.println("searching for " + search);

        SearchResults searchResults = registerAppClient.searchUsername(search, number);
        SearchResultsQuack searchResultsQuacks = postMessageAppClient.searchMessages(search, number);

        return ResponseEntity.ok(List.of(searchResults, searchResultsQuacks));
    }
}
