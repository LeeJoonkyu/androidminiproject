package com.example.spacenova;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SeatFirebase {
    public String seatNo;
    public boolean isOccupied;

    public SeatFirebase() {
    }

    public SeatFirebase(String seatNo, boolean isOccupied) {
        this.seatNo = seatNo;
        this.isOccupied = isOccupied;
    }
    public SeatFirebase(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

}
