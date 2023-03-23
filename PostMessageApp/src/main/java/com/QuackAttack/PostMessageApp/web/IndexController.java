package com.QuackAttack.PostMessageApp.web;


import com.QuackAttack.PostMessageApp.objects.Quack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    @GetMapping("/getQuacksByUserId/{id}")
    public List<Quack> getQuacksByUserId(@PathVariable int id, Model model){
        String sql = "SELECT * FROM quacks WHERE user_id = ?";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Quack.class), id);
    }
    @GetMapping("/getQuacksByUserId/{id}/{number}}")
    public List<Quack> getQuacksByUserId(@PathVariable int id,@PathVariable int number, Model model){
        String sql = "SELECT TOP ? FROM quacks WHERE user_id = ? ORDER BY created_at";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Quack.class), number, id);
    }


    @PostMapping("/postQuack")
    public Quack postMessage(@RequestBody Quack message){
        String sql = "INSERT INTO quacks (user_id, message, is_reply, reply_to_quack_id, is_retweet, retweet_to_quack_id) VALUES (?,?, ?, ?, ?, ?)";

        int rows = jdbcTemplate.update(sql, message.getUserId(), message.getMessage(), message.isReply(), message.getRepliedQuackId(), message.isRetweet(), message.getRetweetedQuackId());
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
}
