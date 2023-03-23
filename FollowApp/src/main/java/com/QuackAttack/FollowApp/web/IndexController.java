package com.QuackAttack.FollowApp.web;

import com.QuackAttack.FollowApp.objects.Following;
import com.QuackAttack.FollowApp.objects.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(UserData sender, UserData target) {

        // access following data from sender
        // add target to following data
        String sql = "INSERT INTO followings (user_id, following_id) VALUES (?,?)";

        // return result
        int rows = jdbcTemplate.update(sql, sender.getId(), sender.getFollowing());
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
            String successMessage = "Follow operation successful for " + sender.getUsername() + " to follow " + target.getUsername();

            return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
            String failMessage = "Follow operation failed for " + sender.getUsername() + " to follow " + target.getUsername();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failMessage);

        }


    }

    @GetMapping("/getFollowing")
    public List<Following> followingList(int user_id) {
        String sql = "SELECT * FROM followings WHERE user_id = " + user_id;

        List<Following> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Following.class));

        return list; // do something with this later
    }


}
