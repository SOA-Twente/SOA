package com.QuackAttack.RegisterApp;




public class Quack {

    private int id;
    private String user_id;
    private String quack;
    private boolean is_reply;
    private int reply_to_quack_id;
    private boolean is_retweet;
    private int retweet_of_quack_id;
    private String created_at;
    public Quack(int id, String user_id, String quack, boolean is_reply, int reply_to_quack_id, boolean is_retweet, int retweet_of_quack_id, String created_at) {
        this.id = id;
        this.user_id = user_id;
        this.quack = quack;
        this.is_reply = is_reply;
        this.reply_to_quack_id = reply_to_quack_id;
        this.is_retweet = is_retweet;
        this.retweet_of_quack_id = retweet_of_quack_id;
        this.created_at = created_at;

    }

    public Quack() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuack() {
        return quack;
    }

    public void setQuack(String quack) {
        this.quack = quack;
    }

    public boolean isIs_reply() {
        return is_reply;
    }

    public void setIs_reply(boolean is_reply) {
        this.is_reply = is_reply;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    @Override
    public String toString() {
        return "Quack{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", quack='" + quack + '\'' +
                ", is_reply=" + is_reply +
                ", reply_to_quack_id=" + reply_to_quack_id +
                ", is_retweet=" + is_retweet +
                ", retweet_of_quack_id=" + retweet_of_quack_id +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
