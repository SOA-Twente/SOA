package com.QuackAttack.PostMessageApp.web;


import com.QuackAttack.PostMessageApp.auth.TokenVerifier;
import com.QuackAttack.RegisterApp.Quack;
import com.QuackAttack.RegisterApp.SearchResultsQuack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(IndexController.class);
    private final JdbcTemplate jdbcTemplate;

    private final TokenVerifier verifier;

    public IndexController(JdbcTemplate jdbcTemplate, TokenVerifier verifier) {
        this.jdbcTemplate = jdbcTemplate;
        this.verifier = verifier;
    }

    @GetMapping("/")
    public String home(){
        return "Hello World";
    }

    @GetMapping("/getAllQuacks")
    public List<Quack> getQuacks(Model model){
        String sql = "SELECT * FROM quacks ORDER BY created_at DESC";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Quack.class));
    }

    /**
     * Returns a list of quacks by a specific user
     * @param username the username of the user
     * @param model the model
     * @return a list of quacks by a specific user
     */
    @GetMapping("/getQuacksByUsername/{username}")
    public List<Quack> getQuacksByUserId(@PathVariable String username, Model model){
        String sql = "SELECT * FROM quacks WHERE user_id = ? ORDER BY created_at DESC";
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

    /**
     * Posts a quack to the database
     * @param credentials JWT
     * @param message the quack, is_reply, reply_to_quack_id, is_retweet, retweet_of_quack_id
     * @return the quack or failure
     */
    @PostMapping("/postQuack")
    public ResponseEntity postMessage(@CookieValue String credentials,@RequestBody Quack message){

        String username;
        try {
            username = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        logger.info(message.toString());
        String sql = "INSERT INTO quacks (user_id, quack, is_reply, reply_to_quack_id, is_retweet, retweet_of_quack_id) VALUES (?,?, ?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql, username, message.getQuack(), message.isIs_reply(), message.getReply_to_quack_id(), message.isIs_retweet(), message.getRetweet_of_quack_id());
        if (rows > 0) {
            //If row has been created
            logger.info("A new row has been inserted.");
        }
        else {
            //If row has not been created
            logger.error("Something went wrong posting a message.");
        }
        return ResponseEntity.ok("message");
    }


    /**
     * Searches quacks by message content
     * @param search the search term
     * @param model the model
     * @param number the number of quacks to return
     * @return a list of quacks or failure
     */
    @GetMapping("/searchQuacks/{search}/{number}")
    public ResponseEntity<SearchResultsQuack> searchQuacks(@PathVariable String search, Model model, @PathVariable int number){
        String sql = "SELECT id, user_id,quack, created_at FROM quacks WHERE LOWER(quack) LIKE LOWER(?) ORDER BY created_at DESC LIMIT ?";
        List<SearchResultsQuack.quackData> user = jdbcTemplate.queryForList(sql, "%" + search +"%", number)
                .stream()
                .map(row -> new SearchResultsQuack.quackData(
                        (int) row.get("id"),
                        (String) row.get("user_id"),
                        (String) row.get("quack"),
                        (Timestamp) row.get("created_at")))
                .toList();

        logger.info("Successfully searched and returned:" + user);
        return ResponseEntity.ok(new SearchResultsQuack(user));
    }

    @GetMapping("/getQuackById/{id}")
    public List<Quack> getQuackById(@PathVariable int id){
        String sql = "SELECT * FROM quacks WHERE id = ?";
        Quack originalQuack = jdbcTemplate.queryForObject(sql,
                BeanPropertyRowMapper.newInstance(Quack.class), id);
        String sql2 = "SELECT * FROM quacks WHERE reply_to_quack_id = ? ORDER BY created_at ASC";
        List<Quack> replies = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(Quack.class), id);
        replies.add(0, originalQuack);
        logger.info("Successfully returned: " + replies);
        return replies;
    }

}
