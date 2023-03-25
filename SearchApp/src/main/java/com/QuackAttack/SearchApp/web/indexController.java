package com.QuackAttack.SearchApp.web;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;




@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class indexController {


    private static final String SEARCH_USERNAME = "http://localhost:8082/searchUsername/";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static Gson gson = new Gson();

    //Example of how to handle JWT. This is how you would get the user email from the JWT
    @GetMapping("/search/{search}")
    public ResponseEntity<Object> searchUsername(@PathVariable String search) throws IOException {

        System.out.println("");
        Object response = new RestTemplate().exchange(SEARCH_USERNAME + search, HttpMethod.GET, new HttpEntity<>("Search request"), new ParameterizedTypeReference<Object>() {
        }).getBody();
        return ResponseEntity.ok(gson.toJson(response));
    }
}
