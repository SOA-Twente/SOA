package com.example.TimelineConsumer.service;

import com.example.TimelineConsumer.objects.Following;
import com.example.TimelineConsumer.objects.Quack;
import com.example.TimelineConsumer.objects.RequestFollowingForm;
import com.example.TimelineConsumer.objects.RequestTimelineForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The TimelineConsumer listen for requests on the queue, take them, create a timeline, and return this timeline.
 * The calls to other services are made in createTimeline and the URLs can be found within app properties.
 */
@Component
@Slf4j
public class TimelineConsumer {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${service-profile-url}")
    private String profileServiceURL;

    @Value("${service-postmessage-url}")
    private String postMessageServiceURL;


    /**
     * This consumer will listen (with help of the JmsListener annotation) for requests on the queue. It does not need
     * further information to listen except for the annotation and the queue name. It will then construct a timeline for
     * the user_id in the request by making calls to the profile database.
     * @param request form to create a timeline for a specific user_id
     */
    @JmsListener(destination = "${active-mq.queue}")
    public void createTimeline(RequestTimelineForm request) {

        int user = request.getUser_id();

        // make calls Profile service to get followings with the request
        List<Following> followingList = getFollowingList(user);

        // with returned followings, call Post message service
        List<Quack> listQuacks = getQuacksByID(followingList);

        // sort the quacks with some method (currently sorted at time, createdAt field of quacks)
        Collections.sort(listQuacks, Comparator.comparing(Quack::getCreatedAt));

        // TODO send the quacks back to the requester

    }

    /**
     * This method makes a call to the profile service to retrieve the following of a user.
     * This is needed to later construct the timeline by requesting the quacks per following.
     * @param user_id of the requester of a timeline.
     * @return a list of following.
     */
    public List<Following> getFollowingList(int user_id) {

        RequestFollowingForm requestFollowingForm = new RequestFollowingForm();
        requestFollowingForm.setUser_id(user_id);

        ResponseEntity<List<Following>> response = restTemplate.exchange(
                profileServiceURL,
                HttpMethod.GET,
                new HttpEntity<>(requestFollowingForm),
                new ParameterizedTypeReference<List<Following>>() {}
        );

        return response.getBody();
    }

    /**
     * This method collects the quacks from all followings and returns a list of the collected quacks.
     * @param followingList to be given. Can be created by calling the profile service.
     * @return a list of Quacks.
     */
    public List<Quack> getQuacksByID(List<Following> followingList) {

        List<Quack> allQuacks = new ArrayList<>();

        // loop over all following
        for (Following following : followingList) {

            // make request to get mapping without request body, so null is given as request entity
            ResponseEntity<List<Quack>> response = restTemplate.exchange(
                    postMessageServiceURL.concat(String.valueOf(following.getUser_ID())),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Quack>>() {}
            );

            // join the two lists
            allQuacks.addAll(response.getBody());

        }

        return allQuacks;
    }
}
