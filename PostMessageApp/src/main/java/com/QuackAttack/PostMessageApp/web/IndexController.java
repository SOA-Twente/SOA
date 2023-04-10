package com.QuackAttack.PostMessageApp.web;


import com.QuackAttack.PostMessageApp.auth.TokenVerifier;
import com.QuackAttack.RegisterApp.Quack;
import com.QuackAttack.RegisterApp.SearchResultsQuack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class IndexController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TokenVerifier verifier;

    @GetMapping("/")
    public String home(){
        return "Hello World";
    }

    @GetMapping("/getAllQuacks")
    public List<Quack> getQuacks(Model model){
        String sql = "SELECT * FROM quacks";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Quack.class));
    }

    //No need for body message
    @GetMapping("/getQuacksByUsername/{username}")
    public List<Quack> getQuacksByUserId(@PathVariable String username, Model model){
        System.out.println(username);
        String sql = "SELECT * FROM quacks WHERE user_id = ?";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Quack.class), username);
    }





/*    Expected json:
    {
        "quack": "Hello World",
        "is_reply": false,
        "reply_to_quack_id": 0,
        "is_retweet": false,
        "retweet_of_quack_id": 0
    }
*/
    @PostMapping("/postQuack")
    public ResponseEntity postMessage(@CookieValue String credentials,@RequestBody Quack message){

        String username;
        try {
            username = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
        String sql = "INSERT INTO quacks (user_id, quack, is_reply, reply_to_quack_id, is_retweet, retweet_of_quack_id) VALUES (?,?, ?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql, username, message.getQuack(), message.isReply(), message.getReply_to_quack_id(), message.isIs_retweet(), message.getRetweet_of_quack_id());
        if (rows > 0) {
            //If row has been created
            System.out.println("A new row has been inserted.");
        }
        else {
            //If row has not been created
            System.out.println("Something went wrong.");
        }
        return ResponseEntity.ok("message");
    }


    @GetMapping("/searchQuacks/{search}/{number}")
    public ResponseEntity<SearchResultsQuack> searchQuacks(@PathVariable String search, Model model, @PathVariable int number){
        String sql = "SELECT user_id,quack, created_at FROM quacks WHERE LOWER(quack) LIKE LOWER(?) ORDER BY created_at DESC LIMIT ?";
        List<SearchResultsQuack.quackData> user = jdbcTemplate.queryForList(sql, "%" + search +"%", number)
                .stream()
                .map(row -> new SearchResultsQuack.quackData(
                        (String) row.get("user_id"),
                        (String) row.get("quack"),
                        (Timestamp) row.get("created_at")))
                .toList();
        return ResponseEntity.ok(new SearchResultsQuack(user));
    }


}
