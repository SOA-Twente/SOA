package com.QuackAttack.RegisterApp.web;

import com.QuackAttack.RegisterApp.objects.ResponseObject;
import com.QuackAttack.RegisterApp.objects.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.QuackAttack.RegisterApp.security.GTokenVerify.checkToken;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class indexController {

    //TODO: TEST THIS
    @Autowired
    private JdbcTemplate jdbcTemplate;

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

        return "Hello World";
    }

    //TODO: TEST THIS
    @GetMapping("/doesUserExist")
    public ResponseObject doesUserExist(@CookieValue String credentials) {
        String username;
        try {
            username = checkToken(credentials);
        } catch (GeneralSecurityException e) {
            return new ResponseObject(false, false, "Invalid JWT");
        } catch (IOException e) {
            return new ResponseObject(false, false, "Invalid JWT");
        }
        String sql = "SELECT * FROM users WHERE username = ?";
        List<UserData> user = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserData.class), username);
        if (user.size() > 0) {
            return new ResponseObject(true, true, "User already exists");
        } else {
            return new ResponseObject(true, false, "User does not exist");
        }
    }


    //TODO: TEST THIS
    @PostMapping("/registerUser")
    public ResponseObject register(@CookieValue String credentials){
        String username;
        try {
            username = checkToken(credentials);
        } catch (GeneralSecurityException e) {
            return new ResponseObject(false, false, "Invalid JWT");
        } catch (IOException e) {
            return new ResponseObject(false, false, "Invalid JWT");
        }

        //postgres insert into userdata
        String sql= "INSERT INTO users (username, email) VALUES (?, ?)";
        int rows = jdbcTemplate.update(sql, username, username);
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
            return new ResponseObject(true, true, "User has been registered");
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
        return new ResponseObject(true, false, "User has not been registered");
        }
    }


    //TODO: TEST THIS
    @GetMapping("/getUserData")
    public ResponseObject getUserData(@CookieValue String credentials){
        String username;
        try {
            username = checkToken(credentials);
        } catch (GeneralSecurityException e) {
            return new ResponseObject(false, false, "Invalid JWT");
        } catch (IOException e) {
            return new ResponseObject(false, false, "Invalid JWT");
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        List<UserData> user = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserData.class), username);
        if (user.size() > 0) {
            return new ResponseObject(true, true, "Here is the user data", user.get(0));
        } else {
            return new ResponseObject(true, false, "User does not exist");
        }
    }


    //TODO: TEST THIS
    @GetMapping("/searchUsername/{username}")
    public List<String> searchUsername(@PathVariable String username) {
        String sql = "SELECT username FROM users WHERE username LIKE ?";
        List<String> user = jdbcTemplate.queryForList(sql, String.class, username + "%");
        return user;
    }


}
