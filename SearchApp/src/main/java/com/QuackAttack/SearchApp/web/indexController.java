package com.QuackAttack.SearchApp.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;



@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class indexController {


    private static final String SEARCH_USERNAME = "https://localhost:8082/searchUsername/";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Example of how to handle JWT. This is how you would get the user email from the JWT
    @GetMapping("/searchUsername")
    public List<String> searchUsername(@RequestParam String username) throws IOException {

        List<String> response = new RestTemplate().exchange(SEARCH_USERNAME + username, HttpMethod.GET, new HttpEntity<>("Search request"), new ParameterizedTypeReference<List<String>>() {
        }).getBody();

        System.out.println(response);
        return response;

//
//        String urlString = "http://localhost:8081/searchusername/" + username;
//        // Manual HTTP request
//        StringBuffer content = new StringBuffer();
//        HttpURLConnection con = null;
//        String inputLine;
//        try {
//            // Build HTTP request
//            URL url = new URL(urlString);
//            con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Content-Type", "application/json");
//
//            // Transform the response InputStream into a String
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (con != null) {
//                con.disconnect();
//            }
//        }
//        return inputLine;


        //make get request to localhost:8081/searchusername/username
        //make simple get request


    }

    //GET rest request
    // to localhost:8081/searchusername/username





}
