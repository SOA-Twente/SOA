package com.QuackAttack.PostMessageApp.objects;

public class Quack {
    private int id;
    private int userId;
    private String message;
    private boolean isReply;
    private int repliedQuackId;
    private boolean isRetweet;
    private int retweetedQuackId;
    private String createdAt;

    public Quack(int id, String message, int userId, boolean isReply, int repliedQuackId, boolean isRetweet, int retweetedQuackId, String createdAt) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.isReply = isReply;
        this.repliedQuackId = repliedQuackId;
        this.isRetweet = isRetweet;
        this.retweetedQuackId = retweetedQuackId;
        this.createdAt = createdAt;
    }

    public Quack() {
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public int getRepliedQuackId() {
        return repliedQuackId;
    }

    public void setRepliedQuackId(int repliedQuackId) {
        this.repliedQuackId = repliedQuackId;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public void setRetweet(boolean retweet) {
        isRetweet = retweet;
    }

    public int getRetweetedQuackId() {
        return retweetedQuackId;
    }

    public void setRetweetedQuackId(int retweetedQuackId) {
        this.retweetedQuackId = retweetedQuackId;
    }
}
