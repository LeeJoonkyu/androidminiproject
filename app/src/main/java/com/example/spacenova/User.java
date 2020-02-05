package com.example.spacenova;

public class User {
    private String userId;
    private String userPw;
    private String userName;
    private String userNickname;
    private String userPhoneNo;
    private String userEmail;
    private String userSeatInfo;
    private String userRoomInfo;
    private String userPermission;
    private String photoUrl;

    public User(String userId, String userPw, String userName, String userNickname, String userPhoneNo, String userEmail) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userPhoneNo = userPhoneNo;
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserSeatInfo() {
        return userSeatInfo;
    }

    public void setUserSeatInfo(String userSeatInfo) {
        this.userSeatInfo = userSeatInfo;
    }

    public String getUserRoomInfo() {
        return userRoomInfo;
    }

    public void setUserRoomInfo(String userRoomInfo) {
        this.userRoomInfo = userRoomInfo;
    }

    public String getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(String userPermission) {
        this.userPermission = userPermission;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
