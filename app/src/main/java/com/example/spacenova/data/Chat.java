package com.example.spacenova.data;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Chat {

    private String id;
    private String nickname;
    private String chatContent;
    private String time;
    private String photoUrl;
    private int who;

    public Chat() {}
    public Chat(String nickname, String chatContent, String time, String photoUrl, int who) {
        this.nickname = nickname;
        this.chatContent = chatContent;
        this.time = time;
        this.photoUrl = photoUrl;
        this.who = who;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getWho() {
        return who;
    }

    public void setWho(int who) {
        this.who = who;
    }
}
