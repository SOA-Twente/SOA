package com.QuackAttack.FollowApp.web;

import com.QuackAttack.FollowApp.objects.FollowUserForm;
import com.QuackAttack.FollowApp.objects.Following;
import com.QuackAttack.FollowApp.objects.RequestFollowingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestBody FollowUserForm form) {

        // access following data from sender
        // add target to following data
        String sql = "INSERT INTO followings (user_id, following_id) VALUES (?,?)";

        // return result
        int rows = jdbcTemplate.update(sql, form.getSender(), form.getTarget());
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
            String successMessage = "Follow operation successful for sender " + form.getSender() + " to follow " + form.getTarget();

            return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
            String failMessage = "Follow operation failed for " + form.getSender() + " to follow " + form.getTarget();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failMessage);
        }
    }

    @GetMapping("/getFollowing")
    public List<Following> followingList(@RequestBody RequestFollowingForm form) {

        // select from the database
        String sql = "SELECT * FROM followings WHERE user_id = " + form.getUser_id();

        List<Following> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Following.class));

        return list; // do something with this later
    }


}
