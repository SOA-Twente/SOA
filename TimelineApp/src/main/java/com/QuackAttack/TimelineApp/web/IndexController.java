package com.QuackAttack.TimelineApp.web;

import com.QuackAttack.TimelineApp.serviceTimeLine.Quack;
import com.QuackAttack.TimelineApp.serviceTimeLine.UserData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RestController
public class IndexController {

    private static final String GET_FOLLOWING = "https://dummy.url/getfollowing";

    @GetMapping("/timeline")
    public List<Quack> timeline(UserData userData) {

        // request to follow service, get following for user_id
        // return list of followings


        // request to profile for quacks for each in the list of followings

        // construct timeline out of 10 most recent quacks (createdAt)
        List<Quack> list = null;


        return list;
    }


}
