package com.soa.demo.web;

import com.soa.demo.objects.Message;
import com.soa.demo.objects.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.soa.demo.security.GTokenVerify.checkToken;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class indexController {

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

    @GetMapping("/getQuacks")
    public List<Message> getQuacks(Model model){
        String sql = "SELECT * FROM quacks";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Message.class));
    }

    //Need id of user to remove follower from, decrements followers
    @PostMapping("/removeFollower")
    public int removeFollower(@RequestBody UserData message){
        //postgres decrement follower value from userdata
        String sql= "UPDATE userdata SET followers = followers - 1 WHERE username = ?";
        int rows = jdbcTemplate.update(sql, message.getId());
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
        }
        return 0;
    }
    //Need id of user to remove follower from, increments followers
    @PostMapping("/addFollower")
    public int addFollower(@RequestBody UserData message){
        //postgres decrement follower value from userdata
        String sql= "UPDATE userdata SET followers = followers + 1 WHERE id = ?";
        int rows = jdbcTemplate.update(sql, message.getId());
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
        }
        return 0;
        //Add follower to userdata
    }

    //Need id of user to add follower to and id of follower to add
    @PostMapping("/addFollowing")
    public UserData addFollowing(@RequestBody UserData message){
        //postgress add userID to array of followers in userdata
        String sql = "INSERT INTO followings (user_id, following_id) VALUES (?,?)";

        int rows = jdbcTemplate.update(sql, message.getId(), message.getFollowing());
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
        }
        return message;
    }

    //Need id of user to add tag and list of tags to add
    @PostMapping("/addTag")
    public UserData addTag(@RequestBody UserData message){
        //postgress add userID to array of followers in userdata
        String sql = "UPDATE userdata SET tags = array_append(tags, ?) WHERE id = ?";
        int rows = jdbcTemplate.update(sql, message.getTags(), message.getId());
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
        }
        return message;
    }

    //Need id of user to add follower to and String of description to add
    @PostMapping("/addDescription")
    public UserData addDescription( @RequestBody UserData message){
        //Change value of description in userdata for certain user.
        String sql = "UPDATE userdata SET description = ? WHERE id = ?";

        int rows = jdbcTemplate.update(sql, message.getDescription(), message.getId());
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
        }
        return message;
    }

    //Need id of user to add follower to and Username to add
    @PostMapping("/addUserProfile")
    public UserData addUser(@RequestBody UserData user){
        String sql = "INSERT INTO userdata (id, username, followers) VALUES (?,?, ?)";

        int rows = jdbcTemplate.update(sql, user.getId(), user.getUsername(), 0);
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
        }
        return user;
    }

    //Get list of users
//    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/users")
    public List<UserData>  getUsers(Model model){
        String sql = "SELECT * FROM userData";
        List<UserData> listUsers = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserData.class));

        for (UserData user : listUsers) {
            System.out.println(user);
        }

        return listUsers;
    }
}
