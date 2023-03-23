package com.soa.demo.objects;



public class UserData {

    private int id;
    private String username;
    private String description;
    private int followers;
    private int following;
    private String tags;


    public UserData(int id, String username, String description, int followers, int following, String tags) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.followers = followers;
        this.following = following;
        this.tags = tags;
    }

    public UserData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
