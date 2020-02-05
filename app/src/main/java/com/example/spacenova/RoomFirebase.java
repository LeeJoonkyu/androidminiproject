package com.example.spacenova;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONException;
import org.json.JSONObject;
@IgnoreExtraProperties
public class RoomFirebase {
//    public int roomNo;
//    public String roomDate; //요일, 시간(시:분:초)
    //쓸데없는 값이 들어가버리는 문제?
    public boolean isOccupied;
    public String usedById;
    public String memo;

    public RoomFirebase() {
    }

    public RoomFirebase(boolean isOccupied, String usedById, String memo) {
        this.isOccupied = isOccupied;
        this.usedById = usedById;
        this.memo = memo;
    }

//    public RoomFirebase(String roomDate, boolean isOccupied, String usedById, String memo) {
//        this.roomDate = roomDate;
//        this.isOccupied = isOccupied;
//        this.usedById = usedById;
//        this.memo = memo;
//    }
//
//    public RoomFirebase(int roomNo, String roomDate, boolean isOccupied, String usedById, String memo) {
//        this.roomNo = roomNo;
//        this.roomDate = roomDate;
//        this.isOccupied = isOccupied;
//        this.usedById = usedById;
//        this.memo = memo;
//    }

}
