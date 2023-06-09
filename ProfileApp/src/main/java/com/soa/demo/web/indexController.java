package com.soa.demo.web;

import com.QuackAttack.RegisterApp.UserProfile;
import com.soa.demo.auth.TokenVerifier;
import com.soa.demo.objects.Message;
import com.soa.demo.objects.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
    Logger logger = LoggerFactory.getLogger(indexController.class);
    private final JdbcTemplate jdbcTemplate;

    private final TokenVerifier verifier;

    public indexController(JdbcTemplate jdbcTemplate, TokenVerifier verifier) {
        this.jdbcTemplate = jdbcTemplate;
        this.verifier = verifier;
    }

    //Example of how to handle JWT. This is how you would get the user email from the JWT
    @GetMapping("/")
    public String home(@CookieValue String credentials){
        System.out.println(credentials);
        try {
            System.out.println(checkToken(credentials));
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        return "Hello World";
    }

    record userDataRecord(int id, String username, String description, int followers, int following, String tags) {}

    /**
     * Returns a list of user data for a specific user
     * @param model the model
     * @param username the username of the user
     * @return a list of user data for a specific user
     */
    @GetMapping("/getUserData/{username}")
    public userDataRecord getUserData(Model model, @PathVariable String username) {
        String sql = "SELECT * FROM userdata WHERE LOWER(username) = LOWER(?)";

        UserData userData = jdbcTemplate.queryForObject(sql,
                BeanPropertyRowMapper.newInstance(UserData.class), username);


       return new userDataRecord(userData.getId(), userData.getUsername(), userData.getDescription(), userData.getFollowers(), userData.getFollowing(), userData.getTags());
    }

    /**
     * Returns all quacks
     * @param model the model
     * @return all quacks
     */
    @GetMapping("/getQuacks")
    public List<Message> getQuacks(Model model){
        String sql = "SELECT * FROM quacks";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Message.class));
    }

    //Need id of user to remove follower from, decrements followers

    /**
     * Removes a follower from a user
     * @param message the message
     * @return 0
     */
    @PostMapping("/removeFollower")
    public int removeFollower(@RequestBody UserData message){
        //postgres decrement follower value from userdata
        String sql= "UPDATE userdata SET followers = followers - 1 WHERE username = ?";
        int rows = jdbcTemplate.update(sql, message.getId());
        if (rows > 0) {
            //If row has been created
            logger.info("A new row has been inserted for user: " + message.getUsername());
        }
        else {
            //If row has not been created
            logger.error("Something went wrong creating a row for user: " + message.getUsername());
        }
        return 0;
    }
    //Need id of user to remove follower from, increments followers

    /**
     * Adds a follower to a user
     * @param message the message
     * @return
     */
    @PostMapping("/addFollower")
    public int addFollower(@RequestBody UserData message){
        //postgres decrement follower value from userdata
        String sql= "UPDATE userdata SET followers = followers + 1 WHERE id = ?";
        int rows = jdbcTemplate.update(sql, message.getId());
        if (rows > 0) {
            //If row has been created
            logger.info("A new row has been inserted for user: " + message.getUsername() + " in addFollower.");
        }
        else {
            //If row has not been created
            logger.error("Something went wrong creating a row for user: " + message.getUsername() + " in addFollower.");
        }
        return 0;
        //Add follower to userdata
    }


    //Need id of user to remove follower from, decrements following/**

    /**
     * Sets tags to a user
     * @param message the tags for the user
     * @param credentials JWT
     * @return the tags for the user
     */
    @PostMapping("/addTag")
    public ResponseEntity<UserData> addTag(@RequestBody UserData message,@CookieValue String credentials){

        String username;
        try {
            username = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        String tagInput = "{";
        for (int i = 0; i < message.getTagsList().toArray().length; i++){
            tagInput += message.getTagsList().toArray()[i];
            if (i != message.getTagsList().toArray().length - 1){
                tagInput += ",";
            }
        }
        tagInput += "}";
        //postgress add userID to array of followers in userdata
//        String sql = "UPDATE userdata SET tags = array_append(tags, ?) WHERE id = ?";
        String sql = "UPDATE userdata SET tags = ? WHERE username = ?";
        int rows = jdbcTemplate.update(sql, tagInput, username);
        if (rows > 0) {
            //If row has been created
            logger.info("A new row has been inserted for user: " + username + " in addTag.") ;
        }
        else {
            //If row has not been created
            logger.error("Something went wrong creating a row for user: " + username + " in addTag.");
        }
        return ResponseEntity.ok(message);
    }

    //Need id of user to add follower to and String of description to add

    /**
     * Adds a description to a user
     * @param message the description for the user
     * @param credentials JWT
     * @return
     */
    @PostMapping("/addDescription")
    public ResponseEntity<UserData> addDescription( @RequestBody UserData message, @CookieValue String credentials){


        String username;
        try {
            username = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        //Change value of description in userdata for certain user.
        String sql = "UPDATE userdata SET description = ? WHERE username = ?";

        int rows = jdbcTemplate.update(sql, message.getDescription(), username);
        if (rows > 0) {
            //If row has been created
            logger.info("A new row has been inserted for user: " + username + " in add Description.") ;
        }
        else {
            //If row has not been created
            logger.error("Something went wrong creating a row for user: " + username + " in add Description.");
        }
        return ResponseEntity.ok(message);
    }

    //Need id of user to add follower to and Username to add

    /**
     * Adds a user profile to the database
     * @param userProfile the user profile
     * @return
     */
    @PostMapping("/addUserProfile")
    public ResponseEntity<Object> addUser(@RequestBody UserProfile userProfile){
        String sql = "INSERT INTO userdata (id, username, followers) VALUES (?,?, ?)";

        String username;
        try {
            username = verifier.checkToken(userProfile.credentials());
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        int rows = jdbcTemplate.update(sql, userProfile.id(), username, 0);
        if (rows > 0) {
            //If row has been created
            logger.info("A new row has been inserted for user: " + username + " in add user profile.") ;
        }
        else {
            //If row has not been created
            logger.error("Something went wrong creating a row for user: " + username + " in add user profile.");
        }
        return ResponseEntity.ok().build();
    }

    /**
     * gets all the users in the database
     * @param model the model
     * @return all the users in the database
     */
    @GetMapping("/users")
    public List<UserData>  getUsers(Model model){
        String sql = "SELECT * FROM userdata";
        List<UserData> listUsers = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserData.class));

        for (UserData user : listUsers) {
            System.out.println(user);
        }

        return listUsers;
    }
}
