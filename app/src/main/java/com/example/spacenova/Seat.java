package com.example.spacenova;

import androidx.annotation.Keep;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class Seat implements Serializable {

    public String seatNo;
    public boolean isOccupied;

    public Seat(){}
    @Keep
    public Seat(String seatNo, boolean isOccupied) {
        this.seatNo = seatNo;
        this.isOccupied = isOccupied;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("seatNo", seatNo);
            obj.put("isOccupied", isOccupied);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
