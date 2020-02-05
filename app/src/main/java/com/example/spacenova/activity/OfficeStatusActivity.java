package com.example.spacenova.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;
import com.example.spacenova.RoomFirebase;
import com.example.spacenova.SeatFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OfficeStatusActivity extends AppCompatActivity {

    private TextView office6Button;
    private TextView office1Button;

    private TextView office6seatstatus;
    private TextView office6seatremained;

    private SharedPreferences office6_seatInfo;
    private SharedPreferences roomData;
    private SharedPreferences userData;
    private SharedPreferences userLogined;

    private String seatStatus;
    private String seatRemained;

    private JSONArray jsonarr;
    private int seatNow;
    private int seatTotal;

    private boolean isOncreated;

    private JSONArray office_roomInfo;

    private Date havrutaTimeNow;
    private Date havrutaTimeToCompare;
    private JSONObject object;

    private TextView officeNo;
    private TextView officeStatus;
    private TextView spaceRemained;
    private TextView office1status;
    private TextView office1remained;
    private TextView office4status;
    private TextView office4remained;
    private TextView office5status;
    private TextView office5remained;

    private Date today;


    private FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabaseReference = mfirebaseDatabase.getReference();

    public static final String ROOM_CHILD = "roomInfo";
    public static final String OFFICE6_CHILD = "office6_seatInfo";
    public static final String USERLOGINED_CHILD = "userLogined";
    public static final String USERS_CHILD = "users";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.office_status_layout);
        roomData = getSharedPreferences("roomData", MODE_PRIVATE);
        userLogined = getSharedPreferences("userLogined", MODE_PRIVATE);
        userData = getSharedPreferences("userData", MODE_PRIVATE);


        office6Button = (TextView) findViewById(R.id.office6Button);
        office1Button = (TextView) findViewById(R.id.office1Button);
        office6seatstatus = (TextView) findViewById(R.id.office6_seatstatus);
        office6seatremained = (TextView) findViewById(R.id.office6_seatremained);
        findViews();


        checkOffice6Status();
        checkOfficeStatus();


        office6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Office6Activity.class);
                //전 화면에서 넘어온 인텐트 값의 아이디 를 기반으로, 로그인유저 데이터를 생성
                startActivity(intent);
            }
        });

        office1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LecturingActivity.class);
                startActivity(intent);
            }
        });
        isOncreated = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseDatabaseReference.child(OFFICE6_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int seatNowOnFirebase = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d("????", dataSnapshot.getChildren().toString());
                    Log.d("????", childSnapshot.toString());

                    SeatFirebase seatFirebase = childSnapshot.getValue(SeatFirebase.class);
                    if (seatFirebase.isOccupied) {
                        seatNowOnFirebase++;
                    }
                }
                seatTotal = 53;
                seatStatus = seatNowOnFirebase + "/" + seatTotal;
                seatRemained = String.valueOf(seatTotal - seatNowOnFirebase);
                office6seatstatus.setText(seatStatus);
                office6seatremained.setText(seatRemained);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        checkOfficeStatus();
    }

    @Override
    protected void onRestart() { //6사무실 액티비티로 갈때는 스탑되엇다가 다시오면 스타트되니까 이게 곧 온스타트 포즈에서 리줌이아님.
        super.onRestart();
        //첫 리스타트떄는 실행안되도록
        if (isOncreated) { //onCreate가 한번이라도 실행되었다면, 다시 세는걸로 실행되도록. 예를들면 6사무실 들어갓다가 나왓을때.
            //두번 세지 않기위해 초기화 시켜
            seatNow = 0;
            checkOffice6StatusOnRestart();

        }

    }


    public JSONArray stringToJSONArray() {

        String strJsonArray = office6_seatInfo.getString("office6_seatInfo", "");
        if (strJsonArray != null) {
            try {
                JSONArray response = new JSONArray(strJsonArray);
                return response;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public JSONArray stringToJSONArrayInRoomData1() {
        String strJsonArray = null;
        //오늘날짜와의 비교가 이뤄져
        //MainActivity today가 뭐뭐 라면, 뭘 가져온다는 식
        today = MainActivity.today;
        if (today.toString().split(" ")[0].equals("Mon")) {
            strJsonArray = roomData.getString("mon_office1_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Tue")) {
            strJsonArray = roomData.getString("tue_office1_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Wed")) {
            strJsonArray = roomData.getString("wed_office1_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Thu")) {
            strJsonArray = roomData.getString("thu_office1_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Fri")) {
            strJsonArray = roomData.getString("fri_office1_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Sat")) {
            strJsonArray = roomData.getString("sat_office1_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Sun")) {
            strJsonArray = roomData.getString("sun_office1_roomInfo", "");
        }

        if (strJsonArray != null) {
            try {
                JSONArray response = new JSONArray(strJsonArray);
                return response;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public JSONArray stringToJSONArrayInRoomData4() {
        String strJsonArray = null;
        //오늘날짜와의 비교가 이뤄져
        //MainActivity today가 뭐뭐 라면, 뭘 가져온다는 식
        today = MainActivity.today;
        if (today.toString().split(" ")[0].equals("Mon")) {
            strJsonArray = roomData.getString("mon_office4_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Tue")) {
            strJsonArray = roomData.getString("tue_office4_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Wed")) {
            strJsonArray = roomData.getString("wed_office4_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Thu")) {
            strJsonArray = roomData.getString("thu_office4_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Fri")) {
            strJsonArray = roomData.getString("fri_office4_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Sat")) {
            strJsonArray = roomData.getString("sat_office4_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Sun")) {
            strJsonArray = roomData.getString("sun_office4_roomInfo", "");
        }

        if (strJsonArray != null) {
            try {
                JSONArray response = new JSONArray(strJsonArray);
                return response;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public JSONArray stringToJSONArrayInRoomData5() {
        String strJsonArray = null;
        //오늘날짜와의 비교가 이뤄져
        //MainActivity today가 뭐뭐 라면, 뭘 가져온다는 식
        today = MainActivity.today;
        if (today.toString().split(" ")[0].equals("Mon")) {
            strJsonArray = roomData.getString("mon_office5_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Tue")) {
            strJsonArray = roomData.getString("tue_office5_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Wed")) {
            strJsonArray = roomData.getString("wed_office5_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Thu")) {
            strJsonArray = roomData.getString("thu_office5_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Fri")) {
            strJsonArray = roomData.getString("fri_office5_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Sat")) {
            strJsonArray = roomData.getString("sat_office5_roomInfo", "");
        } else if (today.toString().split(" ")[0].equals("Sun")) {
            strJsonArray = roomData.getString("sun_office5_roomInfo", "");
        }

        if (strJsonArray != null) {
            try {
                JSONArray response = new JSONArray(strJsonArray);
                return response;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //TODO : 오늘 날짜를 받아서, 해당요일의 사무실이 점유중이라면, 하브루타중인걸로 바꾸기
    private void checkOfficeStatus() {
        //월요일
        //1사무실현황 관리
        //오늘이 월요일이고,
        //지금시간을 받아왓는데 그게 09:00:00이다.
        //그때 월요일 룸인포 순회를 해봣더니 룸데이트가 지금시간 +2시간 안쪽이다?
        //그리고 오큐파이드다?

        //자동반환은 비슷한 로직으로, 룸인포 순회를 먼저하면서
        //그때 시간이 룸인포보다 2시간 오바되엇으면,
        //자동반환되는 로직으로.
        office_roomInfo = stringToJSONArrayInRoomData1();
        if (MainActivity.today.toString().split(" ")[0].equals("Mon")) {
            if (office_roomInfo != null) {
                checkOffice1();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Tue")) {
            if (office_roomInfo != null) {
                checkOffice1();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Wed")) {
            if (office_roomInfo != null) {
                checkOffice1();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Thu")) {
            if (office_roomInfo != null) {
                checkOffice1();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Fri")) {
            if (office_roomInfo != null) {
                checkOffice1();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Sat")) {
            if (office_roomInfo != null) {
                checkOffice1();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Sun")) {
            if (office_roomInfo != null) {
                checkOffice1();
            }
        }
        //1사무실 처리
        //이제 4사무실 처리

        office_roomInfo = stringToJSONArrayInRoomData4();
        if (MainActivity.today.toString().split(" ")[0].equals("Mon")) {
            if (office_roomInfo != null) {
                checkOffice4();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Tue")) {
            if (office_roomInfo != null) {
                checkOffice4();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Wed")) {
            if (office_roomInfo != null) {
                checkOffice4();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Thu")) {
            if (office_roomInfo != null) {
                checkOffice4();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Fri")) {
            if (office_roomInfo != null) {
                checkOffice4();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Sat")) {
            if (office_roomInfo != null) {
                checkOffice4();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Sun")) {
            if (office_roomInfo != null) {
                checkOffice4();
            }
        }

        Log.d("5사무실 체크", " 0");
        office_roomInfo = stringToJSONArrayInRoomData5();
        if (MainActivity.today.toString().split(" ")[0].equals("Mon")) {
            if (office_roomInfo != null) {
                checkOffice5();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Tue")) {
            if (office_roomInfo != null) {
                checkOffice5();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Wed")) {
            if (office_roomInfo != null) {
                checkOffice5();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Thu")) {
            if (office_roomInfo != null) {
                checkOffice5();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Fri")) {
            if (office_roomInfo != null) {
                checkOffice5();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Sat")) {
            if (office_roomInfo != null) {
                checkOffice5();
            }
        } else if (MainActivity.today.toString().split(" ")[0].equals("Sun")) {
            if (office_roomInfo != null) {
                checkOffice5();
            }
        }


    }

    private void checkOffice6Status() {
        office6_seatInfo = getSharedPreferences("office6_seatInfo", MODE_PRIVATE);
        //아예 처음에 아무 자리 정보가 없을경우 일종의 예외처리가 필요
        if (office6_seatInfo.getAll().toString().equals("{}")) { //seatInfo == null일경우 안걸리네?
            seatStatus = "0/53";
            seatRemained = "53";
        } else {
            jsonarr = stringToJSONArray();
            seatTotal = jsonarr.length();
            for (int i = 0; i < jsonarr.length(); i++) {
                try {
                    JSONObject object = jsonarr.getJSONObject(i);
                    if (object.getBoolean("isOccupied")) {
                        seatNow++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            seatStatus = seatNow + "/" + seatTotal;
            seatRemained = String.valueOf(seatTotal - seatNow);
        }


        office6seatstatus.setText(seatStatus);
        office6seatremained.setText(seatRemained);

    }

    private void checkOffice6StatusOnRestart() {
        office6_seatInfo = getSharedPreferences("office6_seatInfo", MODE_PRIVATE);
        //아예 처음에 아무 자리 정보가 없을경우 일종의 예외처리가 필요
        Log.d(MainActivity.TAG, "리스타트 로그 1");
        if (office6_seatInfo.getAll().toString().equals("{}")) { //seatInfo == null일경우 안걸리네?
            seatStatus = "0/53";
            seatRemained = "53";
            Log.d(MainActivity.TAG, "리스타트 로그 싯인포 널인경우");

        } else { //TODO : 한번이라도 6사무실 진입하면 널아닌경우로 들어오게됨. 처리할것
            jsonarr = stringToJSONArray();
            seatTotal = jsonarr.length();
            Log.d(MainActivity.TAG, "리스타트 싯 인포 널아닌경우");

            for (int i = 0; i < jsonarr.length(); i++) {
                try {
                    JSONObject object = jsonarr.getJSONObject(i);
                    if (object.getBoolean("isOccupied")) {
                        seatNow++;
                        Log.d(MainActivity.TAG, "리스타트 싯나우 증가중" + seatNow);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            seatStatus = seatNow + "/" + seatTotal;
            seatRemained = String.valueOf(seatTotal - seatNow);
        }
        office6seatstatus.setText(seatStatus);
        office6seatremained.setText(seatRemained);


    }

    private void findViews() {
        officeNo = (TextView) findViewById(R.id.officeNo);
        officeStatus = (TextView) findViewById(R.id.officeStatus);
        spaceRemained = (TextView) findViewById(R.id.spaceRemained);
        office1Button = (TextView) findViewById(R.id.office1Button);
        office1status = (TextView) findViewById(R.id.office1Status);
        office1remained = (TextView) findViewById(R.id.office1remained);
        office4status = (TextView) findViewById(R.id.office4status);
        office4remained = (TextView) findViewById(R.id.office4remained);
        office5status = (TextView) findViewById(R.id.office5status);
        office5remained = (TextView) findViewById(R.id.office5remained);
    }

    private void checkOffice1() {
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            String idOfRoomOccupier = null;
            //Mon Sep 01 09:00:00이런식
            havrutaTimeNow = sdf.parse(MainActivity.today.toString().split(" ")[3]);
            Log.d(MainActivity.TAG, "지금 시간" + havrutaTimeNow);
            Log.d(MainActivity.TAG, "잘들어왓네1");

            for (int i = 0; i < office_roomInfo.length(); i++) {
                object = office_roomInfo.getJSONObject(i);
                havrutaTimeToCompare = sdf.parse(object.getString("roomDate").split(",")[1]);
                Log.d(MainActivity.TAG, "비교할 시간" + havrutaTimeToCompare);
                long diff = havrutaTimeNow.getTime() - havrutaTimeToCompare.getTime();
                long sec = diff / 1000;
                if (sec < 7200.0) { //두시간 이내라면
                    Log.d(MainActivity.TAG, "잘들어왓네2");
                    if (object.getBoolean("isOccupied")) {
                        //그리고 점유중이라면
                        office1status.setBackgroundColor(Color.parseColor("#FF0000"));
                        office1remained.setBackgroundColor(Color.parseColor("#FF0000"));
                        office1status.setText("하브루타 중");
                        office1remained.setText("하브루타 중");
                    }
                } else if (sec > 7200.0) {
                    //불특정다수가 예약한 방이
                    //정확히 두시간이 지나면 반환되도록 하려면
                    //만약 어떤사람이 11:00 클래스를 예약햇어
                    //그런데 지금 시간이 13:00야
                    //그럼 반환이되야하잖아?
                    //지금시간기준으로,
                    //occupied된 방이
                    //13:11분이면
                    //11:00
                    //09:00은 반환되어야
                    //Log.d(MainActivity.TAG,"두시간 지낫으니 반환 로직 짜주세요");
                    if (object.getBoolean("isOccupied")) {
                        //그리고 점유중이라면
                        office1status.setBackgroundColor(Color.parseColor("#005eff"));
                        office1remained.setBackgroundColor(Color.parseColor("#005eff"));
                        office1status.setText("사용가능");
                        office1remained.setText("사용가능");
                        idOfRoomOccupier = object.getString("usedById");
                        //오늘에 해당하는 요일,
                        //요일에 해당하는 사무실 넘버는 무조건 1
                        //키값이 N요일_office1_roomInfo 인거만 고르기
                        String day = today.toString().split(" ")[0];
                        //해당 키값의 occupied -> false, memo = "", usedById =""로 바꾸기
                        //해당유저데이터 아이디값으로 수정
                        roomReturnLogic(day, 1);
                    }

                    //두시간 초과시 현황판의 이미지들은 바뀌어 있겠지만,
                    //데이터는 바뀌어있지 않기때문에 roomData, mon_roomInfo occupied 수정해줘야함

                }
                //점유중이 아니라면 바꿀필요가 없다 레이아웃이 다시 로드될떄는 기본값이기 때문
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            havrutaTimeNow = sdf.parse(MainActivity.today.toString().split(" ")[3]);
            Log.d(MainActivity.TAG, "지금 시간 파베작업" + havrutaTimeNow);
            mFirebaseDatabaseReference.child(ROOM_CHILD).child(MainActivity.today.toString().split(" ")[0]).child("1사무실").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //지금은 데이터스냅샷 키가 1사무실, 밸류는 09:00~
                    //차일드스냅샷 키가 09:00
                    Log.d("???????", dataSnapshot.getKey());
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        try {
                            Log.d("???????", childSnapshot.getKey());
                            //11:00 를 Date 자료형으로 형변환이 안되는것
                            //unparselable은 변환하려는 시간과 변환되는 시간의 형태차이때문임.
                            //11:00을 변경시켜줘야함
                            havrutaTimeToCompare = sdf.parse(childSnapshot.getKey()+":00");
                            Log.d(MainActivity.TAG, "비교할 시간 파베작업" + havrutaTimeToCompare);

                            //여기서 차일드스냅샷의 키는 09:00
                            RoomFirebase roomFirebase = childSnapshot.getValue(RoomFirebase.class);
                            long diff = havrutaTimeNow.getTime() - havrutaTimeToCompare.getTime();
                            Log.d("시간차이", "비교될시간 : " + havrutaTimeNow.getTime() + "비교할 시간 : " + havrutaTimeToCompare.getTime() + diff);
                            long sec = diff / 1000;
                            Log.d("시간차이",String.valueOf(sec));
                            if (roomFirebase.isOccupied) {
                                Log.d("???????", "잘들어옴?1");
                                if (sec < 7200.0) {
                                    Log.d("???????", "잘들어옴?2");

                                    office1status.setBackgroundColor(Color.parseColor("#FF0000"));
                                    office1remained.setBackgroundColor(Color.parseColor("#FF0000"));
                                    office1status.setText("하브루타 중");
                                    office1remained.setText("하브루타 중");
                                } else if (sec > 7200.0) {
                                    Log.d("???????", "잘들어옴?3");

                                    //그리고 점유중이라면
                                    office1status.setBackgroundColor(Color.parseColor("#005eff"));
                                    office1remained.setBackgroundColor(Color.parseColor("#005eff"));
                                    office1status.setText("사용가능");
                                    office1remained.setText("사용가능");
                                    //TODO : 유저정보 바꿔주기
                                    //idOfRoomOccupier = object.getString("usedById");
                                    //String day = today.toString().split(" ")[0];
                                    //해당 키값의 occupied -> false, memo = "", usedById =""로 바꾸기
                                    //해당유저데이터 아이디값으로 수정
                                    //roomReturnLogic(day,1);
                                }
                            }


//                            Log.d("이게 왜?", dataSnapshot.getKey());
//                            //지금 데이터스냅샷의 키는 1사무실, 4사무실, 5사무실이다
//                            //여기가틀렷네 키는 mon fri ...
//                            //다시 위에 리스너부분 차일드를 수정해서 맞을것
//                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                                Log.d("이게 왜?", childSnapshot.getKey());
//
//                                if (childSnapshot.getKey().equals("1사무실")){
//
//                                }
//                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void checkOffice4() {
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            havrutaTimeNow = sdf.parse(MainActivity.today.toString().split(" ")[3]);
            Log.d(MainActivity.TAG, "지금 시간" + havrutaTimeNow);
            String idOfRoomOccupier = null;
            for (int i = 0; i < office_roomInfo.length(); i++) {
                object = office_roomInfo.getJSONObject(i);
                havrutaTimeToCompare = sdf.parse(object.getString("roomDate").split(",")[1]);
                Log.d(MainActivity.TAG, "비교할 시간" + havrutaTimeToCompare);
                long diff = havrutaTimeNow.getTime() - havrutaTimeToCompare.getTime();
                long sec = diff / 1000;
                if (sec < 7200.0) { //두시간 이내라면
                    if (object.getBoolean("isOccupied")) {
                        //그리고 점유중이라면
                        office4status.setBackgroundColor(Color.parseColor("#FF0000"));
                        office4remained.setBackgroundColor(Color.parseColor("#FF0000"));
                        office4status.setText("하브루타 중");
                        office4remained.setText("하브루타 중");
                    }
                } else if (sec > 7200.0) {
                    if (object.getBoolean("isOccupied")) {
                        //그리고 점유중이라면
                        office4status.setBackgroundColor(Color.parseColor("#005eff"));
                        office4remained.setBackgroundColor(Color.parseColor("#005eff"));
                        office4status.setText("사용가능");
                        office4remained.setText("사용가능");
                        idOfRoomOccupier = object.getString("usedById");
                        //roomReturnLogic(idOfRoomOccupier);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            havrutaTimeNow = sdf.parse(MainActivity.today.toString().split(" ")[3]);
            Log.d(MainActivity.TAG, "지금 시간 파베작업" + havrutaTimeNow);
            mFirebaseDatabaseReference.child(ROOM_CHILD).child(MainActivity.today.toString().split(" ")[0]).child("4사무실").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //지금은 데이터스냅샷 키가 1사무실, 밸류는 09:00~
                    //차일드스냅샷 키가 09:00
                    Log.d("???????", dataSnapshot.getKey());
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        try {
                            Log.d("???????", childSnapshot.getKey());
                            //11:00 를 Date 자료형으로 형변환이 안되는것
                            //unparselable은 변환하려는 시간과 변환되는 시간의 형태차이때문임.
                            //11:00을 변경시켜줘야함
                            havrutaTimeToCompare = sdf.parse(childSnapshot.getKey()+":00");
                            Log.d(MainActivity.TAG, "비교할 시간 파베작업" + havrutaTimeToCompare);

                            //여기서 차일드스냅샷의 키는 09:00
                            RoomFirebase roomFirebase = childSnapshot.getValue(RoomFirebase.class);
                            long diff = havrutaTimeNow.getTime() - havrutaTimeToCompare.getTime();
                            Log.d("시간차이", "비교될시간 : " + havrutaTimeNow.getTime() + "비교할 시간 : " + havrutaTimeToCompare.getTime() + diff);
                            long sec = diff / 1000;
                            Log.d("시간차이",String.valueOf(sec));
                            if (roomFirebase.isOccupied) {
                                Log.d("???????", "잘들어옴?1");
                                if (sec < 7200.0) {
                                    Log.d("???????", "잘들어옴?2");

                                    office4status.setBackgroundColor(Color.parseColor("#FF0000"));
                                    office4remained.setBackgroundColor(Color.parseColor("#FF0000"));
                                    office4status.setText("하브루타 중");
                                    office4remained.setText("하브루타 중");
                                } else if (sec > 7200.0) {
                                    Log.d("???????", "잘들어옴?3");

                                    //그리고 점유중이라면
                                    office4status.setBackgroundColor(Color.parseColor("#005eff"));
                                    office4remained.setBackgroundColor(Color.parseColor("#005eff"));
                                    office4status.setText("사용가능");
                                    office4remained.setText("사용가능");
                                    //TODO : 유저정보 바꿔주기
                                    //idOfRoomOccupier = object.getString("usedById");
                                    //String day = today.toString().split(" ")[0];
                                    //해당 키값의 occupied -> false, memo = "", usedById =""로 바꾸기
                                    //해당유저데이터 아이디값으로 수정
                                    //roomReturnLogic(day,1);
                                }
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void checkOffice5() {
        Log.d("5사무실 체크", " 01");

        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            String idOfRoomOccupier = null;

            havrutaTimeNow = sdf.parse(MainActivity.today.toString().split(" ")[3]);
            Log.d(MainActivity.TAG, "지금 시간" + havrutaTimeNow);
            for (int i = 0; i < office_roomInfo.length(); i++) {
                object = office_roomInfo.getJSONObject(i);
                havrutaTimeToCompare = sdf.parse(object.getString("roomDate").split(",")[1]);
                Log.d(MainActivity.TAG, "비교할 시간" + havrutaTimeToCompare);
                long diff = havrutaTimeNow.getTime() - havrutaTimeToCompare.getTime();
                long sec = diff / 1000;
                if (sec < 7200.0) { //두시간 이내라면
                    if (object.getBoolean("isOccupied")) {
                        //그리고 점유중이라면
                        office5status.setBackgroundColor(Color.parseColor("#FF0000"));
                        office5remained.setBackgroundColor(Color.parseColor("#FF0000"));
                        office5status.setText("하브루타 중");
                        office5remained.setText("하브루타 중");
                    }
                } else if (sec > 7200.0) {
                    if (object.getBoolean("isOccupied")) {
                        //그리고 점유중이라면
                        office5status.setBackgroundColor(Color.parseColor("#005eff"));
                        office5remained.setBackgroundColor(Color.parseColor("#005eff"));
                        office5status.setText("사용가능");
                        office5remained.setText("사용가능");
                        idOfRoomOccupier = object.getString("usedById");
                        //roomReturnLogic(idOfRoomOccupier);
                    }
                    //두시간 초과시 현황판의 이미지들은 바뀌어 있겠지만,
                    //데이터는 바뀌어있지 않기때문에 roomData, mon_roomInfo occupied 수정해줘야함

                }
                //점유중이 아니라면 바꿀필요가 없다 레이아웃이 다시 로드될떄는 기본값이기 때문
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            havrutaTimeNow = sdf.parse(MainActivity.today.toString().split(" ")[3]);
            Log.d(MainActivity.TAG, "지금 시간 파베작업" + havrutaTimeNow);
            Log.d("5사무실 체크1","");

            mFirebaseDatabaseReference.child(ROOM_CHILD).child(MainActivity.today.toString().split(" ")[0]).child("5사무실").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //지금은 데이터스냅샷 키가 1사무실, 밸류는 09:00~
                    //차일드스냅샷 키가 09:00
                    Log.d("???????", dataSnapshot.getKey());
                    Log.d("5사무실 체크2", dataSnapshot.getKey());

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        try {
                            Log.d("???????", childSnapshot.getKey());
                            Log.d("5사무실 체크3", dataSnapshot.getKey());

                            //11:00 를 Date 자료형으로 형변환이 안되는것
                            //unparselable은 변환하려는 시간과 변환되는 시간의 형태차이때문임.
                            //11:00을 변경시켜줘야함
                            havrutaTimeToCompare = sdf.parse(childSnapshot.getKey()+":00");
                            Log.d(MainActivity.TAG, "비교할 시간 파베작업" + havrutaTimeToCompare);

                            //여기서 차일드스냅샷의 키는 09:00
                            RoomFirebase roomFirebase = childSnapshot.getValue(RoomFirebase.class);
                            long diff = havrutaTimeNow.getTime() - havrutaTimeToCompare.getTime();
                            Log.d("시간차이", "비교될시간 : " + havrutaTimeNow.getTime() + "비교할 시간 : " + havrutaTimeToCompare.getTime() + diff);
                            long sec = diff / 1000;
                            Log.d("시간차이",String.valueOf(sec));
                            if (roomFirebase.isOccupied) {
                                Log.d("???????", "잘들어옴?1");
                                if (sec < 7200.0) {
                                    Log.d("???????", "잘들어옴?2");

                                    office5status.setBackgroundColor(Color.parseColor("#FF0000"));
                                    office5remained.setBackgroundColor(Color.parseColor("#FF0000"));
                                    office5status.setText("하브루타 중");
                                    office5remained.setText("하브루타 중");
                                } else if (sec > 7200.0) {
                                    Log.d("???????", "잘들어옴?3");

                                    //그리고 점유중이라면
                                    office5status.setBackgroundColor(Color.parseColor("#005eff"));
                                    office5remained.setBackgroundColor(Color.parseColor("#005eff"));
                                    office5status.setText("사용가능");
                                    office5remained.setText("사용가능");
                                    //TODO : 유저정보 바꿔주기

                                }
                            }



                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void roomReturnLogic(String day, int roomNo) {
        //현재시간이 있고,
        //그 현재시간 기준으로
        //룸데이터 순회하면서
        //해당요일, 현재시간기준 두시간넘는게 있다면
        //예를 들어 11:00예약오큐파이드인데
        //룸데이터 수정
        //유저데이터 수정
        //유저 로그인 데이터 수정

        JSONArray jarrRoomDataToUpdate = null;

        if (roomNo == 1) {
            jarrRoomDataToUpdate = stringToJSONArrayInRoomData1();
        } else if (roomNo == 4) {
            jarrRoomDataToUpdate = stringToJSONArrayInRoomData4();
        } else if (roomNo == 5) {
            jarrRoomDataToUpdate = stringToJSONArrayInRoomData5();
        }

//        for (int n = 0; n < jarrRoomDataToUpdate.length(); n++) {
//            if (n == _seatNumBefore - 1) { //자리값에 해당하는 정보만 가져오기
//                try {
//                    JSONObject object = jarrRoomDataToUpdate.getJSONObject(n);
//                    object.put("isOccupied", false);
//                    Log.d(MainActivity.TAG, "파싱 후 제이슨 : " + jarrRoomDataToUpdate);
//                    office6SeatButtonList.get(_seatNumBefore -1).setImageResource(R.drawable.seatnova55);
//                    office6SeatButtonList.get(_seatNumBefore -1).setClickable(true);
//                    syncSharedPrefWithJsonArray(_office6_jsonArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


    }
}
