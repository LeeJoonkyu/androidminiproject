package com.example.spacenova.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.spacenova.R;
import com.example.spacenova.Seat;
import com.example.spacenova.SeatFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Office6Activity extends AppCompatActivity implements View.OnClickListener {
    private String answerNo = "발권이 취소되었습니다";
    private String answerYes = "발권이 완료되었습니다";
    private ConstraintLayout rootView;
    private ImageButton office6Seatnum1;
    private ImageButton office6Seatnum2;
    private ImageButton office6Seatnum3;
    private ImageButton office6Seatnum4;
    private ImageButton office6Seatnum5;
    private ImageButton office6Seatnum6;
    private ImageButton office6Seatnum7;
    private ImageButton office6Seatnum8;
    private ImageButton office6Seatnum9;
    private ImageButton office6Seatnum10;
    private ImageButton office6Seatnum11;
    private ImageButton office6Seatnum12;
    private ImageButton office6Seatnum13;
    private ImageButton office6Seatnum14;
    private ImageButton office6Seatnum15;
    private ImageButton office6Seatnum16;
    private ImageButton office6Seatnum17;
    private ImageButton office6Seatnum18;
    private ImageButton office6Seatnum19;
    private ImageButton office6Seatnum20;
    private ImageButton office6Seatnum21;
    private ImageButton office6Seatnum22;
    private ImageButton office6Seatnum23;
    private ImageButton office6Seatnum24;
    private ImageButton office6Seatnum25;
    private ImageButton office6Seatnum26;
    private ImageButton office6Seatnum27;
    private ImageButton office6Seatnum28;
    private ImageButton office6Seatnum29;
    private ImageButton office6Seatnum30;
    private ImageButton office6Seatnum31;
    private ImageButton office6Seatnum32;
    private ImageButton office6Seatnum33;
    private ImageButton office6Seatnum34;
    private ImageButton office6Seatnum35;
    private ImageButton office6Seatnum36;
    private ImageButton office6Seatnum37;
    private ImageButton office6Seatnum38;
    private ImageButton office6Seatnum39;
    private ImageButton office6Seatnum40;
    private ImageButton office6Seatnum41;
    private ImageButton office6Seatnum42;
    private ImageButton office6Seatnum43;
    private ImageButton office6Seatnum44;
    private ImageButton office6Seatnum45;
    private ImageButton office6Seatnum46;
    private ImageButton office6Seatnum47;
    private ImageButton office6Seatnum48;
    private ImageButton office6Seatnum49;
    private ImageButton office6Seatnum50;
    private ImageButton office6Seatnum51;
    private ImageButton office6Seatnum52;
    private ImageButton office6Seatnum53;

    //static 선언 시에, 새로 만들어진 office6SeatNum1 == office6SeatButtonList.get(n)이어야하는데,
    //static 이면 전자의 변수는 새로 만들어 지나, 후자의 경우엔 처음에 만들어진 변수를 유지하고 디스트로이 되어도 유지되기 떄문에.
    //문제가 발생!
    public ArrayList<ImageButton> office6SeatButtonList = new ArrayList<>();


    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    private SharedPreferences userLogined;
    public SharedPreferences office6_seatInfo;
    private SharedPreferences userData;

    public ArrayList<Seat> seatArrayList = new ArrayList<>();
    private JSONArray office6_jsonArray;
    private JSONArray _office6_jsonArray;

    private Button btn_seatreturn_at_activity;

    private FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabaseReference = mfirebaseDatabase.getReference();

    public static final String OFFICE6_CHILD = "office6_seatInfo";
    public static final String USERLOGINED_CHILD = "userLogined";
    public static final String USERS_CHILD = "users";


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //레이아웃이 화면 전체 차지하도록
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userLogined = getSharedPreferences("userLogined", MODE_PRIVATE);
        office6_seatInfo = getSharedPreferences("office6_seatInfo", MODE_PRIVATE);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        setUI();
        DatabaseReference usersRef = mFirebaseDatabaseReference.child("userstest");

//        Map<String, Usertest> users = new HashMap<>();
//        users.put("alanisawesome", new Usertest("June 23, 1912", "Alan Turing"));
//        users.put("gracehop", new Usertest("December 9, 1906", "Grace Hopper"));
//        usersRef.setValue(users);


    }

    private void setUI() {
        setContentView(R.layout.office6_layout);
        findViews();
        //imgButtonlist에 담겨있는 전역으로 선언된, image버튼들의 setimageres 하기 위함
        createImgButtonList();
        createSeatReturnBtn();


        //액티비티 생성시 일단 arraylist 초기화
        for (int i = 1; i <= 53; i++) {
            seatArrayList.add(new Seat("6사무실," + i + "번", false));
//            Map<String, SeatFirebase> map = new HashMap<>();
//            map.put(String.valueOf(i),new SeatFirebase(false));
            //맵으로 키 : 밸류를 넣으나, 키를 차일드로 해서 밸류를 오브젝트로 넣으나 결과값은 같다.
            //다만 위에것이 안되는건 계속 덮어씌워져서그럼
        }
        //이를 바탕으로 office6_jsonArray 생성
        office6_jsonArray = new JSONArray();
        for (int i = 0; i < seatArrayList.size(); i++) {
            office6_jsonArray.put(seatArrayList.get(i).getJSONObject());
        }
        syncOffice6SeatImgWithFirebase();
        Log.d(MainActivity.TAG, "온 크리에이트 당시 쉐어드에 저장된 자리 정보 값 : " + office6_seatInfo.getAll().toString());
        Log.d(MainActivity.TAG, "초기화된 제이슨 배열 값 : " + office6_jsonArray.toString());
        //처음에 초기화된 제이슨을 넣어준 적이 없다면, 초기화하고 그렇지 않다면 만들어진 제이슨을 쉐어드에 저장. 이 저장 값을 기반으로 진행
        if (!office6_seatInfo.getBoolean("IS_INIT_BEFORE", false)) {
            initOffice6SeatInfoPref();
            Log.d(MainActivity.TAG, "쉐어드 이닛 당시 쉐어드에 저장된 자리 정보 값 : " + office6_seatInfo.getAll().toString());

        } else {
            syncOffice6SeatImgWithPref();
            Log.d(MainActivity.TAG, "쉐어드 이닛 말고 싱크 이미지 당시 쉐어드에 저장된 자리 정보 값 : " + office6_seatInfo.getAll().toString());
        }

    }

    private void initUserSeatInfo() {
        SharedPreferences.Editor editor = userLogined.edit();
        String userInfoBefore = userLogined.getString(MainActivity.loginedUid, "");
        //가져와서, 자리정보값을 업데이트해준다
        String infoarr[] = userInfoBefore.split(",");
        infoarr[5] = "null";
        editor.putString(MainActivity.loginedUid, TextUtils.join(",", infoarr));
        editor.apply();
        mFirebaseDatabaseReference.child(USERLOGINED_CHILD).child(MainActivity.loginedUid).setValue(TextUtils.join(",", infoarr));
        mFirebaseDatabaseReference.child(USERS_CHILD).child(MainActivity.loginedUid).setValue(TextUtils.join(",", infoarr));

        initUserSeatInfoInUserData();

    }

    private void initUserSeatInfoInUserData() {
        SharedPreferences.Editor editor = userData.edit();
        String userInfoBefore = userData.getString(MainActivity.loginedUid, "");
        String infoarr[] = userInfoBefore.split(",");
        infoarr[5] = "null";
        editor.putString(MainActivity.loginedUid, TextUtils.join(",", infoarr));
        editor.apply();


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        syncOffice6SeatImgWithPref();
//    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2019-09-21 12:32:16 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        rootView = (ConstraintLayout) findViewById(0);
        office6Seatnum1 = (ImageButton) findViewById(R.id.office6_seatnum1);
        office6Seatnum2 = (ImageButton) findViewById(R.id.office6_seatnum2);
        office6Seatnum3 = (ImageButton) findViewById(R.id.office6_seatnum3);
        office6Seatnum4 = (ImageButton) findViewById(R.id.office6_seatnum4);
        office6Seatnum5 = (ImageButton) findViewById(R.id.office6_seatnum5);
        office6Seatnum6 = (ImageButton) findViewById(R.id.office6_seatnum6);
        office6Seatnum7 = (ImageButton) findViewById(R.id.office6_seatnum7);
        office6Seatnum8 = (ImageButton) findViewById(R.id.office6_seatnum8);
        office6Seatnum9 = (ImageButton) findViewById(R.id.office6_seatnum9);
        office6Seatnum10 = (ImageButton) findViewById(R.id.office6_seatnum10);
        office6Seatnum11 = (ImageButton) findViewById(R.id.office6_seatnum11);
        office6Seatnum12 = (ImageButton) findViewById(R.id.office6_seatnum12);
        office6Seatnum13 = (ImageButton) findViewById(R.id.office6_seatnum13);
        office6Seatnum14 = (ImageButton) findViewById(R.id.office6_seatnum14);
        office6Seatnum15 = (ImageButton) findViewById(R.id.office6_seatnum15);
        office6Seatnum16 = (ImageButton) findViewById(R.id.office6_seatnum16);
        office6Seatnum17 = (ImageButton) findViewById(R.id.office6_seatnum17);
        office6Seatnum18 = (ImageButton) findViewById(R.id.office6_seatnum18);
        office6Seatnum19 = (ImageButton) findViewById(R.id.office6_seatnum19);
        office6Seatnum20 = (ImageButton) findViewById(R.id.office6_seatnum20);
        office6Seatnum21 = (ImageButton) findViewById(R.id.office6_seatnum21);
        office6Seatnum22 = (ImageButton) findViewById(R.id.office6_seatnum22);
        office6Seatnum23 = (ImageButton) findViewById(R.id.office6_seatnum23);
        office6Seatnum24 = (ImageButton) findViewById(R.id.office6_seatnum24);
        office6Seatnum25 = (ImageButton) findViewById(R.id.office6_seatnum25);
        office6Seatnum26 = (ImageButton) findViewById(R.id.office6_seatnum26);
        office6Seatnum27 = (ImageButton) findViewById(R.id.office6_seatnum27);
        office6Seatnum28 = (ImageButton) findViewById(R.id.office6_seatnum28);
        office6Seatnum29 = (ImageButton) findViewById(R.id.office6_seatnum29);
        office6Seatnum30 = (ImageButton) findViewById(R.id.office6_seatnum30);
        office6Seatnum31 = (ImageButton) findViewById(R.id.office6_seatnum31);
        office6Seatnum32 = (ImageButton) findViewById(R.id.office6_seatnum32);
        office6Seatnum33 = (ImageButton) findViewById(R.id.office6_seatnum33);
        office6Seatnum34 = (ImageButton) findViewById(R.id.office6_seatnum34);
        office6Seatnum35 = (ImageButton) findViewById(R.id.office6_seatnum35);
        office6Seatnum36 = (ImageButton) findViewById(R.id.office6_seatnum36);
        office6Seatnum37 = (ImageButton) findViewById(R.id.office6_seatnum37);
        office6Seatnum38 = (ImageButton) findViewById(R.id.office6_seatnum38);
        office6Seatnum39 = (ImageButton) findViewById(R.id.office6_seatnum39);
        office6Seatnum40 = (ImageButton) findViewById(R.id.office6_seatnum40);
        office6Seatnum41 = (ImageButton) findViewById(R.id.office6_seatnum41);
        office6Seatnum42 = (ImageButton) findViewById(R.id.office6_seatnum42);
        office6Seatnum43 = (ImageButton) findViewById(R.id.office6_seatnum43);
        office6Seatnum44 = (ImageButton) findViewById(R.id.office6_seatnum44);
        office6Seatnum45 = (ImageButton) findViewById(R.id.office6_seatnum45);
        office6Seatnum46 = (ImageButton) findViewById(R.id.office6_seatnum46);
        office6Seatnum47 = (ImageButton) findViewById(R.id.office6_seatnum47);
        office6Seatnum48 = (ImageButton) findViewById(R.id.office6_seatnum48);
        office6Seatnum49 = (ImageButton) findViewById(R.id.office6_seatnum49);
        office6Seatnum50 = (ImageButton) findViewById(R.id.office6_seatnum50);
        office6Seatnum51 = (ImageButton) findViewById(R.id.office6_seatnum51);
        office6Seatnum52 = (ImageButton) findViewById(R.id.office6_seatnum52);
        office6Seatnum53 = (ImageButton) findViewById(R.id.office6_seatnum53);
        btn_seatreturn_at_activity = (Button) findViewById(R.id.seatreturn_at_office_activity);

        office6Seatnum1.setOnClickListener(this);
        office6Seatnum2.setOnClickListener(this);
        office6Seatnum3.setOnClickListener(this);
        office6Seatnum4.setOnClickListener(this);
        office6Seatnum5.setOnClickListener(this);
        office6Seatnum6.setOnClickListener(this);
        office6Seatnum7.setOnClickListener(this);
        office6Seatnum8.setOnClickListener(this);
        office6Seatnum9.setOnClickListener(this);
        office6Seatnum10.setOnClickListener(this);
        office6Seatnum11.setOnClickListener(this);
        office6Seatnum12.setOnClickListener(this);
        office6Seatnum13.setOnClickListener(this);
        office6Seatnum14.setOnClickListener(this);
        office6Seatnum15.setOnClickListener(this);
        office6Seatnum16.setOnClickListener(this);
        office6Seatnum17.setOnClickListener(this);
        office6Seatnum18.setOnClickListener(this);
        office6Seatnum19.setOnClickListener(this);
        office6Seatnum20.setOnClickListener(this);
        office6Seatnum21.setOnClickListener(this);
        office6Seatnum22.setOnClickListener(this);
        office6Seatnum23.setOnClickListener(this);
        office6Seatnum24.setOnClickListener(this);
        office6Seatnum25.setOnClickListener(this);
        office6Seatnum26.setOnClickListener(this);
        office6Seatnum27.setOnClickListener(this);
        office6Seatnum28.setOnClickListener(this);
        office6Seatnum29.setOnClickListener(this);
        office6Seatnum30.setOnClickListener(this);
        office6Seatnum31.setOnClickListener(this);
        office6Seatnum32.setOnClickListener(this);
        office6Seatnum33.setOnClickListener(this);
        office6Seatnum34.setOnClickListener(this);
        office6Seatnum35.setOnClickListener(this);
        office6Seatnum36.setOnClickListener(this);
        office6Seatnum37.setOnClickListener(this);
        office6Seatnum38.setOnClickListener(this);
        office6Seatnum39.setOnClickListener(this);
        office6Seatnum40.setOnClickListener(this);
        office6Seatnum41.setOnClickListener(this);
        office6Seatnum42.setOnClickListener(this);
        office6Seatnum43.setOnClickListener(this);
        office6Seatnum44.setOnClickListener(this);
        office6Seatnum45.setOnClickListener(this);
        office6Seatnum46.setOnClickListener(this);
        office6Seatnum47.setOnClickListener(this);
        office6Seatnum48.setOnClickListener(this);
        office6Seatnum49.setOnClickListener(this);
        office6Seatnum50.setOnClickListener(this);
        office6Seatnum51.setOnClickListener(this);
        office6Seatnum52.setOnClickListener(this);
        office6Seatnum53.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2019-09-21 12:32:16 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == office6Seatnum1) {
            buildDialog((ImageButton) v, "1번");
            alertDialog.show();
        } else if (v == office6Seatnum2) {
            // Handle clicks for office6Seatnum2
            buildDialog((ImageButton) v, "2번");
            alertDialog.show();
        } else if (v == office6Seatnum3) {
            // Handle clicks for office6Seatnum3
            buildDialog((ImageButton) v, "3번");
            alertDialog.show();
        } else if (v == office6Seatnum4) {
            // Handle clicks for office6Seatnum4
            buildDialog((ImageButton) v, "4번");
            alertDialog.show();
        } else if (v == office6Seatnum5) {
            // Handle clicks for office6Seatnum5
            buildDialog((ImageButton) v, "5번");
            alertDialog.show();
        } else if (v == office6Seatnum6) {
            // Handle clicks for office6Seatnum6
            buildDialog((ImageButton) v, "6번");
            alertDialog.show();
        } else if (v == office6Seatnum7) {
            buildDialog((ImageButton) v, "7번");
            alertDialog.show();
        } else if (v == office6Seatnum8) {
            buildDialog((ImageButton) v, "8번");
            alertDialog.show();
        } else if (v == office6Seatnum9) {
            buildDialog((ImageButton) v, "9번");
            alertDialog.show();
        } else if (v == office6Seatnum10) {
            buildDialog((ImageButton) v, "10번");
            alertDialog.show();
        } else if (v == office6Seatnum11) {
            buildDialog((ImageButton) v, "11번");
            alertDialog.show();
        } else if (v == office6Seatnum12) {
            buildDialog((ImageButton) v, "12번");
            alertDialog.show();
        } else if (v == office6Seatnum13) {
            buildDialog((ImageButton) v, "13번");
            alertDialog.show();
        } else if (v == office6Seatnum14) {
            buildDialog((ImageButton) v, "14번");
            alertDialog.show();
        } else if (v == office6Seatnum15) {
            buildDialog((ImageButton) v, "15번");
            alertDialog.show();
        } else if (v == office6Seatnum16) {
            buildDialog((ImageButton) v, "16번");
            alertDialog.show();
        } else if (v == office6Seatnum17) {
            buildDialog((ImageButton) v, "17번");
            alertDialog.show();
        } else if (v == office6Seatnum18) {
            buildDialog((ImageButton) v, "18번");
            alertDialog.show();
        } else if (v == office6Seatnum19) {
            buildDialog((ImageButton) v, "19번");
            alertDialog.show();
        } else if (v == office6Seatnum20) {
            buildDialog((ImageButton) v, "20번");
            alertDialog.show();
        } else if (v == office6Seatnum21) {
            buildDialog((ImageButton) v, "21번");
            alertDialog.show();
        } else if (v == office6Seatnum22) {
            buildDialog((ImageButton) v, "22번");
            alertDialog.show();
        } else if (v == office6Seatnum23) {
            buildDialog((ImageButton) v, "23번");
            alertDialog.show();
        } else if (v == office6Seatnum24) {
            buildDialog((ImageButton) v, "24번");
            alertDialog.show();
        } else if (v == office6Seatnum25) {
            buildDialog((ImageButton) v, "25번");
            alertDialog.show();
        } else if (v == office6Seatnum26) {
            buildDialog((ImageButton) v, "26번");
            alertDialog.show();
        } else if (v == office6Seatnum27) {
            buildDialog((ImageButton) v, "27번");
            alertDialog.show();
        } else if (v == office6Seatnum28) {
            buildDialog((ImageButton) v, "28번");
            alertDialog.show();
        } else if (v == office6Seatnum29) {
            buildDialog((ImageButton) v, "29번");
            alertDialog.show();
        } else if (v == office6Seatnum30) {
            buildDialog((ImageButton) v, "30번");
            alertDialog.show();
        } else if (v == office6Seatnum31) {
            buildDialog((ImageButton) v, "31번");
            alertDialog.show();
        } else if (v == office6Seatnum32) {
            buildDialog((ImageButton) v, "32번");
            alertDialog.show();
        } else if (v == office6Seatnum33) {
            buildDialog((ImageButton) v, "33번");
            alertDialog.show();
        } else if (v == office6Seatnum34) {
            buildDialog((ImageButton) v, "34번");
            alertDialog.show();
        } else if (v == office6Seatnum35) {
            buildDialog((ImageButton) v, "35번");
            alertDialog.show();
        } else if (v == office6Seatnum36) {
            buildDialog((ImageButton) v, "36번");
            alertDialog.show();
        } else if (v == office6Seatnum37) {
            buildDialog((ImageButton) v, "37번");
            alertDialog.show();
        } else if (v == office6Seatnum38) {
            buildDialog((ImageButton) v, "38번");
            alertDialog.show();
        } else if (v == office6Seatnum39) {
            buildDialog((ImageButton) v, "39번");
            alertDialog.show();
        } else if (v == office6Seatnum40) {
            buildDialog((ImageButton) v, "40번");
            alertDialog.show();
        } else if (v == office6Seatnum41) {
            buildDialog((ImageButton) v, "41번");
            alertDialog.show();
        } else if (v == office6Seatnum42) {
            buildDialog((ImageButton) v, "42번");
            alertDialog.show();
        } else if (v == office6Seatnum43) {
            buildDialog((ImageButton) v, "43번");
            alertDialog.show();
        } else if (v == office6Seatnum44) {
            buildDialog((ImageButton) v, "44번");
            alertDialog.show();
        } else if (v == office6Seatnum45) {
            buildDialog((ImageButton) v, "45번");
            alertDialog.show();
        } else if (v == office6Seatnum46) {
            buildDialog((ImageButton) v, "46번");
            alertDialog.show();
        } else if (v == office6Seatnum47) {
            buildDialog((ImageButton) v, "47번");
            alertDialog.show();
        } else if (v == office6Seatnum48) {
            buildDialog((ImageButton) v, "48번");
            alertDialog.show();
        } else if (v == office6Seatnum49) {
            buildDialog((ImageButton) v, "49번");
            alertDialog.show();
        } else if (v == office6Seatnum50) {
            buildDialog((ImageButton) v, "50번");
            alertDialog.show();
        } else if (v == office6Seatnum51) {
            buildDialog((ImageButton) v, "51번");
            alertDialog.show();
        } else if (v == office6Seatnum52) {
            buildDialog((ImageButton) v, "52번");
            alertDialog.show();
        } else if (v == office6Seatnum53) {
            buildDialog((ImageButton) v, "53번");
            alertDialog.show();
        }
    }

    private void buildDialog(final ImageButton seatButton, final String seatNum) {
        builder = new AlertDialog.Builder(Office6Activity.this);
        builder.setMessage("해당 좌석을 발권하시겠습니까?");
        builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
            int _seatNum;

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), answerYes, Toast.LENGTH_SHORT).show();
                seatButton.setImageResource(R.drawable.seatnova55_occupied);
                seatButton.setClickable(false);
                //넘어온 파라미터 값이 상수화 되는 건 이전 에러의 문제가 아니었다.
                //생성과 온클릭이 따로 떨어져있어서, 넘어가는 파라미터 값이 달랐던 게 문제엿나?
                //빌드 다이얼로그가 온클릭 밖에서 buildDialog(office6SeatNum1); 이후 온클릭에서 alertDialog.show()
                //했을 때 문제가 생겻는데, 이게 왜문제지?! --> 파트장님한테 질문
                //TODO : 유저 정보 업데이트 및 좌석 정보 업데이트
                //TODO : 자리 번호를 파싱하려면? SEAT json 생성이 필수적인가?
                //office6_seatInfo를 키값으로 정보를 가져온다음,
                //해당 키값으로 받아온 정보는 배열이니까 [0]은 1번자리와 맵핑되어잇다.
                //파라미터로 받아온 싯넘버의 스트링이 1번이라면,
                //이를 substring(0,seatNum.length-1)로 처리해준다음, integer로 파싱한다
                //그럼 이제 싯넘버가 1로 파싱되었고, 싯넘버가 1이라면, 배열정보는 0에 해당하는 (차이1)을 업데이트해준다
                //TODO : 온클릭으로 자리 선택 "네"를 눌렀을 때 해당 자리가 몇번인지 일단 레이아웃에서 얻어와야함
                _seatNum = Integer.parseInt(seatNum.substring(0, seatNum.length() - 1));
                _office6_jsonArray = stringToJSONArray();

                for (int n = 0; n < _office6_jsonArray.length(); n++) {
                    if (n == _seatNum - 1) { //자리값에 해당하는 정보만 가져오기
                        try {
//                            Log.d(MainActivity.TAG, "바꾸기전 제이슨 : " + _office6_jsonArray);
                            JSONObject object = _office6_jsonArray.getJSONObject(n);
//                            Log.d(MainActivity.TAG, "바꾸기후 제이슨 : " + _office6_jsonArray);
                            object.put("isOccupied", true);
//                            _office6_jsonArray.put(n,object);
                            //여기서 object는 그냥 n번째 jsonobject를 꺼낸건데, 어째서 _office6_jsonArray값이 바뀌어잇지?
                            Log.d(MainActivity.TAG, "파싱 후 제이슨 : " + _office6_jsonArray);
                            //해당 제이슨 배열과 같이 쉐어드 역시 싱크를 맞춰준다.
                            syncSharedPrefWithJsonArray(_office6_jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //seatNum이 1번 이런식으로 돼있어서 파싱이 필요함
                mFirebaseDatabaseReference.child(OFFICE6_CHILD).child(seatNum.substring(0,seatNum.length()-1)).setValue(new SeatFirebase(true));

                //TODO : 유저정보 변경하기
                //TODO : 이미 유저에게 자리정보가 존재한다면, 이전자리의 처리
                //TODO : 쉐어드 정보 기준으로, occupied true 이면, 색깔변경처리.
                if (!userLogined.getString(MainActivity.loginedUid, "").split(",")[5].equals("null")) {
                    //자리정보가 존재한다면
                    //자리 이동시의 처리를 해줘야함
                    //해당 자리를 제이슨에서 unoccupied처리, 그리고 쉐어드에 반영, 그리고 이미지 처리
                    changeSeatImageWhenSeatChanged();
                    editUserSeatInfo(seatNum);
                    Log.d(MainActivity.TAG, "자리 이동 후 제이슨이 반영된 쉐어드 값 : " + office6_seatInfo.getAll().toString());

                } else { //유저에게 자리 정보 없는 경우
                    editUserSeatInfo(seatNum);
                    Log.d(MainActivity.TAG, "자리 정보 없는 경우 제이슨이 반영된 쉐어드 값 : " + office6_seatInfo.getAll().toString());
                }


            }
        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), answerNo, Toast.LENGTH_SHORT).show();
                seatButton.setImageResource(R.drawable.seatnova55);
            }
        });
        alertDialog = builder.create();
    }

    private void changeSeatImageWhenSeatChanged() {
        String tmp = userLogined.getString(MainActivity.loginedUid, "").split(",")[5].split(" ")[1];
        int _seatNumBefore = Integer.parseInt(tmp.substring(0, tmp.length() - 1));
        //1번 가져옴
        //json에서 변경처리
        //shared에 반영
        _office6_jsonArray = stringToJSONArray();
        //if not null 필요가없는게 null 인 경우가 존재하지않음
        for (int n = 0; n < _office6_jsonArray.length(); n++) {
            if (n == _seatNumBefore - 1) { //자리값에 해당하는 정보만 가져오기
                try {
                    if (_seatNumBefore >= 9) {
                        Log.d(MainActivity.TAG, "!@#!@$!@#");
                    }
                    JSONObject object = _office6_jsonArray.getJSONObject(n);
                    object.put("isOccupied", false);
                    Log.d(MainActivity.TAG, "파싱 후 제이슨 : " + _office6_jsonArray);
                    office6SeatButtonList.get(_seatNumBefore - 1).setImageResource(R.drawable.seatnova55);
                    office6SeatButtonList.get(_seatNumBefore - 1).setClickable(true);
                    syncSharedPrefWithJsonArray(_office6_jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //이전 자리의 처리만 해주면 새 예약은 어차피 또 다른 메소드에서 처리해주니까
        //UI처리는 syncimgonFirebase에서 해주니까
        mFirebaseDatabaseReference.child(OFFICE6_CHILD).child(tmp.substring(0, tmp.length() - 1)).setValue(new SeatFirebase(false));
    }


    private void syncOffice6SeatImgWithPref() {
        //office6_seantInfo 순회하면서, true인 자리는, office6seatnum1.setimageresource
        //싱크 맞춰서 이미지 변경은 쉐어드를 토대로 진행됨


        JSONArray jarr = stringToJSONArray();
//        JSONArray jarrfb = stringOnFbToJSONArray();
        for (int n = 0; n < jarr.length(); n++) {
            try {
                JSONObject object = jarr.getJSONObject(n);
                if (object.getBoolean("isOccupied")) {
                    office6SeatButtonList.get(n).setImageResource(R.drawable.seatnova55_occupied);
                    office6SeatButtonList.get(n).setClickable(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    private void syncSharedPrefWithJsonArray(JSONArray _office6_seatInfo) {
        SharedPreferences.Editor editor = office6_seatInfo.edit();
        editor.putString("office6_seatInfo", _office6_seatInfo.toString());
        editor.apply();
    }

    public void initOffice6SeatInfoPref() {
        SharedPreferences.Editor editor = office6_seatInfo.edit();
        //jsonArray를 String으로 바꾸어서 넣어준다

        editor.putString("office6_seatInfo", office6_jsonArray.toString());
        editor.putBoolean("IS_INIT_BEFORE", true);
        editor.apply();

    }



    public void syncOffice6SeatImgWithFirebase(){
        mFirebaseDatabaseReference.child(OFFICE6_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int n=0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    SeatFirebase seatFirebase = childSnapshot.getValue(SeatFirebase.class);
                    if (seatFirebase.isOccupied) {
                        office6SeatButtonList.get(n).setImageResource(R.drawable.seatnova55_occupied);
                        office6SeatButtonList.get(n).setClickable(false);
                    } else {
                        office6SeatButtonList.get(n).setImageResource(R.drawable.seatnova55);
                        office6SeatButtonList.get(n).setClickable(true);
                    }
                    n++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    //여기서는 userLogined만 수정하고 메인 가서 userData와 싱크를 맞춰준다.
    //가급적이면 여기서 한번에 하는 게 낫겟지?
    private void editUserSeatInfo(String seatNum) {
        SharedPreferences.Editor editor = userLogined.edit();
        String userInfoBefore = userLogined.getString(MainActivity.loginedUid, "");
        //가져와서, 자리정보값을 업데이트해준다
        String infoarr[] = userInfoBefore.split(",");
        infoarr[5] = "6사무실 " + seatNum;
        editor.putString(MainActivity.loginedUid, TextUtils.join(",", infoarr));
        editor.apply();
        mFirebaseDatabaseReference.child(USERS_CHILD).child(MainActivity.loginedUid).setValue(TextUtils.join(",", infoarr));
        mFirebaseDatabaseReference.child(USERLOGINED_CHILD).child(MainActivity.loginedUid).setValue(TextUtils.join(",", infoarr));
        editUserSeatInfoInUserData(seatNum);
    }
    private void editUserSeatInfoInUserData(String seatNum){
        SharedPreferences.Editor editor = userData.edit();
        String userInfoBefore = userData.getString(MainActivity.loginedUid, "");
        String infoarr[] = userInfoBefore.split(",");
        infoarr[5] = "6사무실 " + seatNum;
        editor.putString(MainActivity.loginedUid, TextUtils.join(",", infoarr));
        editor.apply();
    }
    //TODO : userLogined 자동로그인될때 자리정보값이 null로 들어오는데, 이거 싱크 맞춰줘야한다.

    private void createImgButtonList() {
        office6SeatButtonList.add(office6Seatnum1);
        office6SeatButtonList.add(office6Seatnum2);
        office6SeatButtonList.add(office6Seatnum3);
        office6SeatButtonList.add(office6Seatnum4);
        office6SeatButtonList.add(office6Seatnum5);
        office6SeatButtonList.add(office6Seatnum6);
        office6SeatButtonList.add(office6Seatnum7);
        office6SeatButtonList.add(office6Seatnum8);
        office6SeatButtonList.add(office6Seatnum9);
        office6SeatButtonList.add(office6Seatnum10);
        office6SeatButtonList.add(office6Seatnum11);
        office6SeatButtonList.add(office6Seatnum12);
        office6SeatButtonList.add(office6Seatnum13);
        office6SeatButtonList.add(office6Seatnum14);
        office6SeatButtonList.add(office6Seatnum15);
        office6SeatButtonList.add(office6Seatnum16);
        office6SeatButtonList.add(office6Seatnum17);
        office6SeatButtonList.add(office6Seatnum18);
        office6SeatButtonList.add(office6Seatnum19);
        office6SeatButtonList.add(office6Seatnum20);
        office6SeatButtonList.add(office6Seatnum21);
        office6SeatButtonList.add(office6Seatnum22);
        office6SeatButtonList.add(office6Seatnum23);
        office6SeatButtonList.add(office6Seatnum24);
        office6SeatButtonList.add(office6Seatnum25);
        office6SeatButtonList.add(office6Seatnum26);
        office6SeatButtonList.add(office6Seatnum27);
        office6SeatButtonList.add(office6Seatnum28);
        office6SeatButtonList.add(office6Seatnum29);
        office6SeatButtonList.add(office6Seatnum30);
        office6SeatButtonList.add(office6Seatnum31);
        office6SeatButtonList.add(office6Seatnum32);
        office6SeatButtonList.add(office6Seatnum33);
        office6SeatButtonList.add(office6Seatnum34);
        office6SeatButtonList.add(office6Seatnum35);
        office6SeatButtonList.add(office6Seatnum36);
        office6SeatButtonList.add(office6Seatnum37);
        office6SeatButtonList.add(office6Seatnum38);
        office6SeatButtonList.add(office6Seatnum39);
        office6SeatButtonList.add(office6Seatnum40);
        office6SeatButtonList.add(office6Seatnum41);
        office6SeatButtonList.add(office6Seatnum42);
        office6SeatButtonList.add(office6Seatnum43);
        office6SeatButtonList.add(office6Seatnum44);
        office6SeatButtonList.add(office6Seatnum45);
        office6SeatButtonList.add(office6Seatnum46);
        office6SeatButtonList.add(office6Seatnum47);
        office6SeatButtonList.add(office6Seatnum48);
        office6SeatButtonList.add(office6Seatnum49);
        office6SeatButtonList.add(office6Seatnum50);
        office6SeatButtonList.add(office6Seatnum51);
        office6SeatButtonList.add(office6Seatnum52);
        office6SeatButtonList.add(office6Seatnum53);

    }

    private void createSeatReturnBtn() {
        btn_seatreturn_at_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(MainActivity.TAG, "자리반환 온클릭 만들어젼시?");
                builder = new AlertDialog.Builder(Office6Activity.this);
                builder.setMessage("자리를 반환하시겠어요?");
                builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "자리를 반환했습니다", Toast.LENGTH_SHORT).show();
                        //6사무실 1번
                        String tmp = userLogined.getString(MainActivity.loginedUid, "").split(",")[5].split(" ")[1];
                        int _seatNumBefore = Integer.parseInt(tmp.substring(0, tmp.length() - 1));
                        //1번 가져옴
                        //json에서 변경처리
                        //shared에 반영
                        _office6_jsonArray = stringToJSONArray();
                        if (_office6_jsonArray != null) {
                            for (int n = 0; n < _office6_jsonArray.length(); n++) {
                                if (n == _seatNumBefore - 1) { //자리값에 해당하는 정보만 가져오기
                                    try {
                                        JSONObject object = _office6_jsonArray.getJSONObject(n);
                                        object.put("isOccupied", false);
                                        Log.d(MainActivity.TAG, "파싱 후 제이슨 : " + _office6_jsonArray);
                                        office6SeatButtonList.get(_seatNumBefore - 1).setImageResource(R.drawable.seatnova55);
                                        office6SeatButtonList.get(_seatNumBefore - 1).setClickable(true);
                                        syncSharedPrefWithJsonArray(_office6_jsonArray);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            initUserSeatInfo();
                            mFirebaseDatabaseReference.child(OFFICE6_CHILD).child(tmp.substring(0,tmp.length()-1)).setValue(new SeatFirebase(false));

                        }


                        //TODO : 유저정보 변경하기
                        //TODO : 이미 유저에게 자리정보가 존재한다면, 이전자리의 처리
                        //TODO : 쉐어드 정보 기준으로, occupied true 이면, 색깔변경처리.
                    }
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), answerNo, Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog = builder.create();
                builder.show();
            }
        });
    }


}
