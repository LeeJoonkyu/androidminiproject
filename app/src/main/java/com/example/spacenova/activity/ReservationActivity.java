package com.example.spacenova.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;
import com.example.spacenova.reservation_activities.Office1_ReservationActivity;
import com.example.spacenova.reservation_activities.Office4_ReservationActivity;
import com.example.spacenova.reservation_activities.Office5_ReservationActivity;

import java.util.Date;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button monOffice1;
    private Button monOffice4;
    private Button monOffice5;
    private Button tueOffice1;
    private Button tueOffice4;
    private Button tueOffice5;
    private Button wedOffice1;
    private Button wedOffice4;
    private Button wedOffice5;
    private Button thuOffice1;
    private Button thuOffice4;
    private Button thuOffice5;
    private Button friOffice1;
    private Button friOffice4;
    private Button friOffice5;
    private Button satOffice1;
    private Button satOffice4;
    private Button satOffice5;
    private Button sunOffice1;
    private Button sunOffice4;
    private Button sunOffice5;

    private Date today;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_layout);
        today = MainActivity.today;
        Log.d(MainActivity.TAG,"하브루타 예약 당시 날짜 : "+today.toString());
        findViews();



    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2019-09-25 20:48:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        monOffice1 = (Button) findViewById(R.id.mon_office1);
        monOffice4 = (Button) findViewById(R.id.mon_office4);
        monOffice5 = (Button) findViewById(R.id.mon_office5);
        tueOffice1 = (Button) findViewById(R.id.tue_office1);
        tueOffice4 = (Button) findViewById(R.id.tue_office4);
        tueOffice5 = (Button) findViewById(R.id.tue_office5);
        wedOffice1 = (Button) findViewById(R.id.wed_office1);
        wedOffice4 = (Button) findViewById(R.id.wed_office4);
        wedOffice5 = (Button) findViewById(R.id.wed_office5);
        thuOffice1 = (Button) findViewById(R.id.thu_office1);
        thuOffice4 = (Button) findViewById(R.id.thu_office4);
        thuOffice5 = (Button) findViewById(R.id.thu_office5);
        friOffice1 = (Button) findViewById(R.id.fri_office1);
        friOffice4 = (Button) findViewById(R.id.fri_office4);
        friOffice5 = (Button) findViewById(R.id.fri_office5);
        satOffice1 = (Button) findViewById(R.id.sat_office1);
        satOffice4 = (Button) findViewById(R.id.sat_office4);
        satOffice5 = (Button) findViewById(R.id.sat_office5);
        sunOffice1 = (Button) findViewById(R.id.sun_office1);
        sunOffice4 = (Button) findViewById(R.id.sun_office4);
        sunOffice5 = (Button) findViewById(R.id.sun_office5);

        monOffice1.setOnClickListener(this);
        monOffice4.setOnClickListener(this);
        monOffice5.setOnClickListener(this);
        tueOffice1.setOnClickListener(this);
        tueOffice4.setOnClickListener(this);
        tueOffice5.setOnClickListener(this);
        wedOffice1.setOnClickListener(this);
        wedOffice4.setOnClickListener(this);
        wedOffice5.setOnClickListener(this);
        thuOffice1.setOnClickListener(this);
        thuOffice4.setOnClickListener(this);
        thuOffice5.setOnClickListener(this);
        friOffice1.setOnClickListener(this);
        friOffice4.setOnClickListener(this);
        friOffice5.setOnClickListener(this);
        satOffice1.setOnClickListener(this);
        satOffice4.setOnClickListener(this);
        satOffice5.setOnClickListener(this);
        sunOffice1.setOnClickListener(this);
        sunOffice4.setOnClickListener(this);
        sunOffice5.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2019-09-25 20:48:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == monOffice1) {
            Intent intent = new Intent(getApplicationContext(), Office1_ReservationActivity.class);
            intent.putExtra("reserveWhich","mon_office1");
            startActivity(intent);
        } else if (v == monOffice4) {
            Intent intent = new Intent(getApplicationContext(), Office4_ReservationActivity.class);
            intent.putExtra("reserveWhich","mon_office4");
            startActivity(intent);
        } else if (v == monOffice5) {
            Intent intent = new Intent(getApplicationContext(), Office5_ReservationActivity.class);
            intent.putExtra("reserveWhich","mon_office5");
            startActivity(intent);
        } else if (v == tueOffice1) {
            Intent intent = new Intent(getApplicationContext(), Office1_ReservationActivity.class);
            intent.putExtra("reserveWhich","tue_office1");
            startActivity(intent);
        } else if (v == tueOffice4) {
            Intent intent = new Intent(getApplicationContext(), Office4_ReservationActivity.class);
            intent.putExtra("reserveWhich","tue_office4");
            startActivity(intent);
        } else if (v == tueOffice5) {
            Intent intent = new Intent(getApplicationContext(), Office5_ReservationActivity.class);
            intent.putExtra("reserveWhich","tue_office5");
            startActivity(intent);
        } else if (v == wedOffice1) {
            Intent intent = new Intent(getApplicationContext(), Office1_ReservationActivity.class);
            intent.putExtra("reserveWhich","wed_office1");
            startActivity(intent);
        } else if (v == wedOffice4) {
            Intent intent = new Intent(getApplicationContext(), Office4_ReservationActivity.class);
            intent.putExtra("reserveWhich","wed_office4");

            startActivity(intent);
        } else if (v == wedOffice5) {
            Intent intent = new Intent(getApplicationContext(), Office5_ReservationActivity.class);
            intent.putExtra("reserveWhich","wed_office5");

            startActivity(intent);
        } else if (v == thuOffice1) {
            Intent intent = new Intent(getApplicationContext(), Office1_ReservationActivity.class);
            intent.putExtra("reserveWhich","thu_office1");

            startActivity(intent);
        } else if (v == thuOffice4) {
            Intent intent = new Intent(getApplicationContext(), Office4_ReservationActivity.class);
            intent.putExtra("reserveWhich","thu_office4");

            startActivity(intent);
        } else if (v == thuOffice5) {
            Intent intent = new Intent(getApplicationContext(), Office5_ReservationActivity.class);
            intent.putExtra("reserveWhich","thu_office5");

            startActivity(intent);
        } else if (v == friOffice1) {
            Intent intent = new Intent(getApplicationContext(), Office1_ReservationActivity.class);
            intent.putExtra("reserveWhich","fri_office1");

            startActivity(intent);
        } else if (v == friOffice4) {
            Intent intent = new Intent(getApplicationContext(), Office4_ReservationActivity.class);
            intent.putExtra("reserveWhich","fri_office4");

            startActivity(intent);
        } else if (v == friOffice5) {
            Intent intent = new Intent(getApplicationContext(), Office5_ReservationActivity.class);
            intent.putExtra("reserveWhich","fri_office5");

            startActivity(intent);
        } else if (v == satOffice1) {
            Intent intent = new Intent(getApplicationContext(), Office1_ReservationActivity.class);
            intent.putExtra("reserveWhich","sat_office1");

            startActivity(intent);
        } else if (v == satOffice4) {
            Intent intent = new Intent(getApplicationContext(), Office4_ReservationActivity.class);
            intent.putExtra("reserveWhich","sat_office4");

            startActivity(intent);
        } else if (v == satOffice5) {
            Intent intent = new Intent(getApplicationContext(), Office5_ReservationActivity.class);
            intent.putExtra("reserveWhich","sat_office5");

            startActivity(intent);
        } else if (v == sunOffice1) {
            Intent intent = new Intent(getApplicationContext(), Office1_ReservationActivity.class);
            intent.putExtra("reserveWhich","sun_office1");

            startActivity(intent);
        } else if (v == sunOffice4) {
            Intent intent = new Intent(getApplicationContext(), Office4_ReservationActivity.class);
            intent.putExtra("reserveWhich","sun_office4");

            startActivity(intent);
        } else if (v == sunOffice5) {
            Intent intent = new Intent(getApplicationContext(), Office5_ReservationActivity.class);
            intent.putExtra("reserveWhich","sun_office5");
            startActivity(intent);
        }
    }

}
