package com.QuackAttack.TimelineApp.objects;

public class Quack {

    private int id;
    private String user_id;
    private String quack;
    private boolean is_reply;
    private int reply_to_quack_id;
    private boolean is_retweet;
    private int retweet_of_quack_id;
    private String created_at;

    public Quack(int id, String message, String userId, boolean isReply, int repliedQuackId, boolean isRetweet, int retweetedQuackId, String createdAt) {
        this.id = id;
        this.quack = message;
        this.user_id = userId;
        this.is_reply = isReply;
        this.reply_to_quack_id = repliedQuackId;
        this.is_retweet = isRetweet;
        this.retweet_of_quack_id = retweetedQuackId;
        this.created_at = createdAt;
    }

    public Quack() {
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuack() {
        return quack;
    }

    public void setQuack(String quack) {
        this.quack = quack;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isReply() {
        return is_reply;
    }

    public void setReply(boolean reply) {
        is_reply = reply;
    }

    public int getReply_to_quack_id() {
        return reply_to_quack_id;
    }

    public void setReply_to_quack_id(int reply_to_quack_id) {
        this.reply_to_quack_id = reply_to_quack_id;
    }

    public boolean isIs_retweet() {
        return is_retweet;
    }

    public void setIs_retweet(boolean is_retweet) {
        this.is_retweet = is_retweet;
    }

    public int getRetweet_of_quack_id() {
        return retweet_of_quack_id;
    }

    public void setRetweet_of_quack_id(int retweet_of_quack_id) {
        this.retweet_of_quack_id = retweet_of_quack_id;
    }
}
