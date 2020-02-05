package com.example.spacenova.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;
import com.example.spacenova.RoomFirebase;
import com.example.spacenova.SeatFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class IntroActivity extends AppCompatActivity {

    private FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabaseReference = mfirebaseDatabase.getReference();

    public static final String OFFICE6_CHILD = "office6_seatInfo";
    public static final String USERLOGINED_CHILD = "userLogined";
    public static final String USERS_CHILD = "users";

    public static final String ROOM_CHILD = "roomInfo";
    public static final String MON_CHILD = "Mon";
    public static final String TUE_CHILD = "Tue";
    public static final String WED_CHILD = "Wed";
    public static final String THU_CHILD = "Thu";
    public static final String FRI_CHILD = "Fri";
    public static final String SAT_CHILD = "Sat";
    public static final String SUN_CHILD = "Sun";


    private SharedPreferences isInit;

    boolean isLoaded = false;
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //컨텍스트를 다음 로그인 액티비티로 넘김
                startActivity(intent);
                isLoaded = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.intro_layout);
        isInit = getSharedPreferences("isAppInitBefore", MODE_PRIVATE);

        //어플이 실행되는 최초에 한번만 office6 초기화
        if (!isInit.getBoolean("is_app_init_before", false)) {
            appinit();
            initOffice6SeatInfoOnFirebase();
            initRoomInfoOnFirebase();
            Log.d("isInitCalledEverytime","?????????");
        }
        //xml과 java파일 연결
    }

    private void appinit() {
        SharedPreferences.Editor editor = isInit.edit();
        editor.putBoolean("is_app_init_before", true);
        editor.apply();
    }

    public void initOffice6SeatInfoOnFirebase() {
        for (int i = 1; i <= 53; i++) {
            mFirebaseDatabaseReference.child(OFFICE6_CHILD).child(String.valueOf(i)).setValue(new SeatFirebase(false));
        }
    }

    public void initRoomInfoOnFirebase() {
        Map<String, RoomFirebase> map = new HashMap<>();
        String[] dayArr = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        String[] roomNoArr = {"1사무실", "4사무실", "5사무실"};
        String[] timeArr = {"09:00","11:00","13:00","15:00","17:00","19:00"};
        for (String day : dayArr){
            for (String roomNo : roomNoArr) {
                for (String time : timeArr){
                    map.put(time,new RoomFirebase(false,"",""));
                }
                mFirebaseDatabaseReference.child(ROOM_CHILD).child(day).child(roomNo).setValue(map);

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //화면이 다 그려지고 난 후 3초뒤에 로그인 창으로. 러너블객체 실행
        handler.postDelayed(r, 3000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
        if (!isLoaded) {
            Toast.makeText(getApplicationContext(), "어플리케이션 로딩이 취소되었습니다", Toast.LENGTH_SHORT).show();
        }
        //만약 화면에서 벗어낫다면(홈버튼 처럼). 그럼 예약을 취소하자
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(IntroActivity.this);
            builder.setMessage("앱을 종료하시겠어요?");
            builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                    //앱 종료
                }
            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Toast.makeText(getApplicationContext(),"onRestart",Toast.LENGTH_LONG).show();
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(getApplicationContext(),"onStart",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(getApplicationContext(),"onStop",Toast.LENGTH_LONG).show();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(),"onDestroy",Toast.LENGTH_LONG).show();
//    }

}
