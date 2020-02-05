package com.example.spacenova.data;

import java.util.Date;

public class ChatDTO {

    private String Id;
    private String userNickname;
    private String message;
    private Date date;
    private String photoUrl;
    private int who;


    public ChatDTO() {}
    public ChatDTO(String userNickname, String message, Date date, String photoUrl, int who) {
        this.userNickname = userNickname;
        this.message = message;
        this.date = date;
        this.photoUrl = photoUrl;
        this.who = who;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getMessage() {
        return message;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWho() {
        return who;
    }

    public void setWho(int who) {
        this.who = who;
    }
}
