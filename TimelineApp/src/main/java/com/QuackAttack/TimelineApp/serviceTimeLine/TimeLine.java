package com.QuackAttack.TimelineApp.serviceTimeLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class TimeLine {

    private List<UserData> following = null;
    private static final int TWEET_LIMIT = 10;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // retrieve followings from follow service
    public List<UserData> getFollowingList(String followingUrl, int user_id) throws IOException {
        URL obj = new URL(followingUrl);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        // do construct request to follow with user id
        List<UserData> followingList = null;

        return followingList;
    }

    // retrieve tweets from profile service
    public List<Quack> timeline(List<UserData> followingList) {

        // list to return at the end
        List<Quack> quackList = new ArrayList<>();

        StringBuilder userIDList = new StringBuilder();

        for (int i = 0; i < followingList.size(); i++) {
            if (i == 0) {
                userIDList.append(followingList.get(0).getId());
            } else {
                userIDList.append(", " + followingList.get(i).getId());
            }
        }

        String sql = "SELECT * FROM quacks WHERE user_ID in (" + userIDList + ") ORDER BY createdAt DESC LIMIT + " + TWEET_LIMIT + " ;";

        List<Quack> quacks = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Quack.class));

        return quacks;
    }




}
