package com.QuackAttack.TimelineApp.web;

import com.QuackAttack.TimelineApp.auth.TokenVerifier;
import com.QuackAttack.TimelineApp.objects.Following;
import com.QuackAttack.TimelineApp.objects.Quack;
import com.QuackAttack.TimelineApp.objects.RequestFollowingForm;
import com.QuackAttack.TimelineApp.objects.RequestTimelineForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class IndexController {
    private static final String GET_FOLLOWING = "http://follow-app-sev.production:8084/getFollowing";
    private static final String GET_QUACKS = "http://post-message-app-sev.production:8081/getQuacksByUsername/";
    private static final int TIMELINE_MAX_SIZE = 10;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private TokenVerifier verifier;

    @GetMapping("/timeline")
    public List<Quack> timeline(@CookieValue String credentials) {

        String username = null;
        try {
            username = verifier.checkToken(credentials);
        } catch (GeneralSecurityException | IOException e) {
            //return ResponseEntity.badRequest().build();
        }

        // request to follow service, handle response which is a list of following objects
        // RequestFollowingForm followRequest = new RequestFollowingForm(form.getUser_id());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "credentials="+ credentials);

        ResponseEntity<List<Following>> response = restTemplate.exchange(
                GET_FOLLOWING,
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<List<Following>>() {
                });

        List<Following> followings = response.getBody();

        // request to profile for quacks for each in the list of followings, add them to this list
        List<Quack> quacks = new ArrayList<>();


        for (Following following : followings) {

            // GET to post a message service to retrieve the quacks per following
            ResponseEntity<List<Quack>> responseQuacks = restTemplate.exchange(
                    GET_QUACKS + following.getUser_id(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Quack>>() {}
            );

            List<Quack> followingQuack = responseQuacks.getBody();
            quacks.addAll(followingQuack);
        }


        // construct timeline, most recent sort (createdAt)
        Collections.sort(quacks, Comparator.comparing(Quack::getCreated_at).reversed());
        List<Quack> timeline = quacks.subList(0, Math.min(quacks.size(), TIMELINE_MAX_SIZE));

        return timeline;
    }


}
