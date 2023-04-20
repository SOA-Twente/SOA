package com.QuackAttack.SearchApp.web;


import com.QuackAttack.RegisterApp.PostMessageAppClient;
import com.QuackAttack.RegisterApp.RegisterAppClient;
import com.QuackAttack.RegisterApp.SearchResults;
import com.QuackAttack.RegisterApp.SearchResultsQuack;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class indexController {

    Logger logger = LoggerFactory.getLogger(indexController.class);

//    private static final String SEARCH_USERNAME = "http://localhost:8082/searchUsername/";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RegisterAppClient registerAppClient = RegisterAppClient.create();
    private PostMessageAppClient postMessageAppClient = PostMessageAppClient.create();


    private static Gson gson = new Gson();

    /**
     * Searches with a specific number of requests for users and messages
     * @param search the search term
     * @param number the number of requests
     * @return a list of search results
     * @throws IOException to handle
     */
    @GetMapping("/search/{search}/{number}")
    public ResponseEntity<Object> searchUsername(@PathVariable String search,@PathVariable int number) throws IOException {
        logger.info("searching for " + search);

        SearchResults searchResults = registerAppClient.searchUsername(search, number);
        SearchResultsQuack searchResultsQuacks = postMessageAppClient.searchMessages(search, number);

        return ResponseEntity.ok(List.of(searchResults, searchResultsQuacks));
    }
}
