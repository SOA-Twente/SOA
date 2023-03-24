package com.QuackAttack.TimelineApp.web;

import com.QuackAttack.TimelineApp.serviceTimeLine.Following;
import com.QuackAttack.TimelineApp.serviceTimeLine.Quack;
import com.QuackAttack.TimelineApp.serviceTimeLine.RequestFollowingForm;
import com.QuackAttack.TimelineApp.serviceTimeLine.RequestTimelineForm;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class IndexController {

    private static final String GET_FOLLOWING = "https://dummy.url/getfollowing";
    private static final String GET_QUACKS = "https://dummy.url//getQuacksByUserId/";

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/timeline")
    public List<Quack> timeline(@RequestBody RequestTimelineForm form) {

        // request to follow service, handle response which is a list of following objects
        RequestFollowingForm followRequest = new RequestFollowingForm(form.getUser_id());

        ResponseEntity<List<Following>> response = restTemplate.exchange(
                GET_FOLLOWING,
                HttpMethod.POST,
                new HttpEntity<>(followRequest),
                new ParameterizedTypeReference<List<Following>>() {
                });

        List<Following> followings = response.getBody();

        // request to profile for quacks for each in the list of followings, add them to this list
        List<Quack> quacks = new ArrayList<>();

        for (Following following : followings) {

            // GET to post a message service to retrieve the quacks per following
            ResponseEntity<List<Quack>> responseQuacks = restTemplate.exchange(
                    GET_QUACKS + following.getID(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Quack>>() {}
            );

            List<Quack> followingQuack = responseQuacks.getBody();
            quacks.addAll(followingQuack);
        }


        // construct timeline out of 10 most recent quacks (createdAt)
        List<Quack> list = null;


        return list;
    }


}
