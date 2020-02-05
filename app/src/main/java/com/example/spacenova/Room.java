package com.example.spacenova;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Room {
    //하브루타 예약페이지에서 스태틱으로 관리할것 아마
    //private Date date;

    //오늘이 목요일이면,월요일은 당연히 다음주 월요일로 세팅되어야함
    private int roomNo;
    //일단 하드코딩 적으로 MON 13:00 해두고,
    //자동반환은 그날의 그레고리안 캘린더를 받아서, 만약 월요일이고
    //13:00이 넘으면,그때부터 시간 카운트를해서, 지나가면 반환되는 구조로.
    private String roomDate; //요일, 시간(시:분:초)
    private boolean isOccupied;
    private String usedById;
    private String memo;

    public Room(int roomNo, String roomDate, boolean isOccupied, String usedById, String memo) {
        this.roomNo = roomNo;
        this.roomDate = roomDate;
        this.isOccupied = isOccupied;
        this.usedById = usedById;
        this.memo = memo;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("roomNo", roomNo);
            obj.put("roomDate", roomDate);
            obj.put("isOccupied",isOccupied);
            obj.put("usedById",usedById);
            obj.put("memo",memo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(String roomDate) {
        this.roomDate = roomDate;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getUsedById() {
        return usedById;
    }

    public void setUsedById(String usedById) {
        this.usedById = usedById;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
