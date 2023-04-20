package com.QuackAttack.FollowApp.web;

import com.QuackAttack.FollowApp.auth.TokenVerifier;
import com.QuackAttack.FollowApp.objects.Following;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class IndexController {

    Logger logger = LoggerFactory.getLogger(IndexController.class);
    private final JdbcTemplate jdbcTemplate;

    private final TokenVerifier verifier;

    public IndexController(JdbcTemplate jdbcTemplate, TokenVerifier verifier) {
        this.jdbcTemplate = jdbcTemplate;
        this.verifier = verifier;
    }


    /**
     * This method is used to unfollow a user
     * @param credentials JWT
     * @param following Following object, id, user_id and following_id
     * @return request failure or success
     */
    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@CookieValue String credentials,@RequestBody Following following) {

        String user_id;
        try {
            user_id = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        //drop row from followings table where following_id = following.getFollowing_id() and user_id = user_id
        String sql = "DELETE FROM followings WHERE following_id = ? AND user_id = ?";
        int rows = jdbcTemplate.update(sql, following.getFollowing_id(), user_id);
        if (rows > 0) {
            //If row has been created
            logger.info("A new row has been inserted.");
            String successMessage = "Unfollow operation successful for sender " + user_id + " to unfollow " + following.getFollowing_id();

            return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
        }
        else {
            //If row has not been created
            String failMessage = "Unfollow operation failed for " + user_id + " to unfollow " + following.getFollowing_id();
            logger.error(failMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failMessage);
        }
    }

    /**
     * This method is used to follow a user
     * @param credentials JWT
     * @param following Following object, id, user_id and following_id
     * @return request failure or success
     */
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@CookieValue String credentials,@RequestBody Following following) {

        String user_id;
        try {
            user_id = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }


        // access following data from sender
        // add target to following data
        String sql = "INSERT INTO followings (user_id, following_id) VALUES (?,?)";

        // return result
        int rows = jdbcTemplate.update(sql, user_id, following.getFollowing_id());
        if (rows > 0) {
            //If row has been created
            logger.info("A new row has been inserted.");
            String successMessage = "Follow operation successful for sender " + user_id + " to follow " + following.getFollowing_id();
            logger.info(successMessage);

            return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
        }
        else {
            //If row has not been created
            String failMessage = "Follow operation failed for " + user_id + " to follow " + following.getFollowing_id();
            logger.error(failMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failMessage);
        }
    }

    /**
     * This method is used to get a list of users that the user is following
     * @param credentials JWT
     * @return bad request or list of users
     */
    @GetMapping("/getFollowing")
    public List<Following> followingList(@CookieValue String credentials) {

        String user_id;
        try {
            user_id = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return null;
//            return ResponseEntity.badRequest().build();
        }

        // select from the database
        String sql = "SELECT * FROM followings WHERE user_id = ?";

        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Following.class), user_id);
    }
}
