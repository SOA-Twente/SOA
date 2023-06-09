package com.QuackAttack.RegisterApp.web;

import com.QuackAttack.RegisterApp.*;
import com.QuackAttack.RegisterApp.auth.TokenVerifier;
import com.QuackAttack.RegisterApp.database.RegisterAppDb;
import com.QuackAttack.RegisterApp.objects.UserData;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class IndexController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RegisterAppDb registerAppDb;
    @Autowired
    private TokenVerifier verifier;

    private ProfileAppClient profileAppClient = ProfileAppClient.create();



    private static final Gson gson = new Gson();

    //Example of how to handle JWT. This is how you would get the user email from the JWT
    @GetMapping("/")
    public String home(@CookieValue String credentials){
        System.out.println(credentials);
        try {
            System.out.println(verifier.checkToken(credentials));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return gson.toJson("Hello World");
    }

    /**
     * returns wheter a user already has an account or not
     * @param credentials JWT
     * @return true or false
     */
    @GetMapping("/doesUserExist")
    public ResponseEntity<DoesUserExistResult> doesUserExist(@CookieValue String credentials) {
        String username;
        try {
            username = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        List<UserData> user = registerAppDb.getUsers(username);
        return ResponseEntity.ok().body(new DoesUserExistResult(!user.isEmpty()));
    }

    enum RegisterResultEnum {
        USER_EXISTS,
        USER_REGISTERED,
        USER_NOT_REGISTERED
    }
    record RegisterResult(RegisterResultEnum result, String username) { }

    /**
     * Registers a user
     * @param credentials JWT
     * @return true or false
     */
    @PostMapping("/registerUser")
    public ResponseEntity<RegisterResult> register(@CookieValue String credentials){
        String username;
        try {
            username = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        List<UserData> users = registerAppDb.getUsers(username);
        if (users.size() > 0) {
            System.out.println("User already exists");
            return ResponseEntity.ok().body(new RegisterResult(RegisterResultEnum.USER_EXISTS, username));
        }

        //postgres insert into userdata
        String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
        int rows = jdbcTemplate.update(sql, username, username);
        if (rows > 0) {
            //If row has been created

            //add profile
            String sql2 = "SELECT id FROM users WHERE username = ?";
            int id = jdbcTemplate.queryForObject(sql2, Integer.class, username);
            profileAppClient.addUserProfile(new UserProfile(credentials, id));

            return ResponseEntity.ok().body(new RegisterResult(RegisterResultEnum.USER_REGISTERED, username));
        }
        else {
            //If row has not been created
            return ResponseEntity.ok().body(new RegisterResult(RegisterResultEnum.USER_NOT_REGISTERED, username));

        }
    }


    /**
     * Gets user data
     * @param credentials JWT
     * @return User data
     */
    @GetMapping("/getUserData")
    public ResponseEntity getUserData(@CookieValue String credentials){
        String username;
        try {
            username = verifier.checkToken(credentials);
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
    @GetMapping("/searchUsername/{username}/{number}")
    public ResponseEntity<SearchResults> searchUsername(@PathVariable String username, @PathVariable int number) {
        String sql = "SELECT id, username, email FROM users WHERE LOWER(username) LIKE LOWER(?) LIMIT ?";
        List<SearchResults.UserData> user = jdbcTemplate.queryForList(sql, username +"%", number)
                .stream()
                .map(row -> new SearchResults.UserData(
                        (int) row.get("id"),
                        (String) row.get("username"),
                        (String) row.get("email")))
                .toList();

        return ResponseEntity.ok(new SearchResults(user));
    }


}
