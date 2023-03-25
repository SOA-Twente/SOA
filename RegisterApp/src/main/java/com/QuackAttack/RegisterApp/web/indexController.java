package com.QuackAttack.RegisterApp.web;

import com.QuackAttack.RegisterApp.objects.ResponseObject;
import com.QuackAttack.RegisterApp.objects.UserData;
import com.google.gson.Gson;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import static com.QuackAttack.RegisterApp.security.GTokenVerify.checkToken;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class indexController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Gson gson = new Gson();

    //Example of how to handle JWT. This is how you would get the user email from the JWT
    @GetMapping("/")
    public String home(@CookieValue String credentials){
        System.out.println(credentials);
        try {
            System.out.println(checkToken(credentials));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return gson.toJson("Hello World");
    }

    //TODO: TEST THIS
    @GetMapping("/doesUserExist")
    public ResponseEntity doesUserExist(@CookieValue String credentials) {
        String username;
        try {
            username = checkToken(credentials);
        } catch (GeneralSecurityException e) {

            return ResponseEntity.badRequest().body(gson.toJson("Invalid JWT"));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(gson.toJson("Invalid JWT"));
        }
        String sql = "SELECT * FROM users WHERE username = ?";
        List<UserData> user = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserData.class), username);
        if (user.size() > 0) {
            return ResponseEntity.ok().body(gson.toJson("User already exists"));
        } else {
            return ResponseEntity.ok().body(gson.toJson("User does not exist"));
        }
    }


    //TODO: TEST THIS
    @PostMapping("/registerUser")
    public ResponseEntity register(@CookieValue String credentials){
        String username;
        try {
            username = checkToken(credentials);
        } catch (GeneralSecurityException e) {
            return ResponseEntity.badRequest().body(gson.toJson("Invalid JWT"));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(gson.toJson("Invalid JWT"));
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        List<Map<String, Object>> user = jdbcTemplate.queryForList(sql, username);
        if (user.size() > 0) {
            System.out.println("User already exists");
            return ResponseEntity.ok().body(gson.toJson("User already exists"));
        }

        //postgres insert into userdata
        sql= "INSERT INTO users (username, email) VALUES (?, ?)";
        int rows = jdbcTemplate.update(sql, username, username);
        if (rows > 0) {
            //If row has been created
            return ResponseEntity.ok().body(gson.toJson("User has been registered"));
        }
        else {
            //If row has not been created
            return ResponseEntity.internalServerError().body(gson.toJson("User has not been registered"));

        }
    }


    //TODO: TEST THIS
    @GetMapping("/getUserData")
    public ResponseEntity getUserData(@CookieValue String credentials){
        String username;
        try {
            username = checkToken(credentials);
        } catch (GeneralSecurityException e) {
            return ResponseEntity.badRequest().body(gson.toJson("Invalid JWT"));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(gson.toJson("Invalid JWT"));
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        List<Map<String,Object>> user = jdbcTemplate.queryForList(sql, username);
        if (user.size() > 0) {
            return ResponseEntity.ok().body(gson.toJson(user.get(0)));
        } else {
            return ResponseEntity.badRequest().body(gson.toJson("User does not exist"));
        }
    }

    /**
     * Search for users by username
     * @param username
     * @return List of users
     */
    @GetMapping("/searchUsername/{username}")
    public ResponseEntity searchUsername(@PathVariable String username) {
        String sql = "SELECT id, username, email FROM users WHERE LOWER(username) LIKE LOWER(?)";
        List<Map<String, Object>> user = jdbcTemplate.queryForList(sql, username +"%");
        return ResponseEntity.ok(gson.toJson(user));
    }


}
