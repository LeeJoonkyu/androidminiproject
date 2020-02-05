package com.example.spacenova.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spacenova.R;
import com.example.spacenova.adapter.ChatAdapter;
import com.example.spacenova.data.Chat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private int who;
    private boolean loginSuccess;
    private ArrayList<Chat> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public final static String TAG = "태그";
    public SharedPreferences userLogined;
    private SharedPreferences userData;
    private Intent intent;

    private Button officestatusButton;
    private Button noticeButton;
    private Button havrutaButton;
    private Button profileButton;

    private EditText chatInput;
    private TextView myChatItem;
    private TextView myChatTime;
    private Button sendButton;

    public static String loginedUid;

    public static Calendar cal = new GregorianCalendar();
    public static Date today = new Date(cal.getTimeInMillis());

    private FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabaseReference = mfirebaseDatabase.getReference();

    public static final String MESSAGES_CHILD = "messages";
    public static final String USERLOGINED_CHILD = "userLogined";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLogined = getSharedPreferences("userLogined", MODE_PRIVATE);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        intent = getIntent();
        loginedUid = intent.getStringExtra("ID");
        setLogined();
        syncUserDataWithUserLogined();
        setUI();

//        if (!intent.getBooleanExtra("isSocialLogin",false)){
//        }
        Log.d(TAG, "메인 화면에서 오늘 시간 : "+Arrays.toString(today.toString().split(" ")));
        //메모리 로드 후에는 더이상 호출되지 않음.
        //다른 액티비티에 갓다오면 stop된 상태로 남아있으니까. 딱 한번 하고싶다면 onCreate를 사용
        //매번하고싶다면 onStart나 onResume

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        syncUserDataWithUserLogined();
    }

    //이전 로그인 화면에서 가져온 정보는 아마 setLogined()에서 하는 듯 하고
    //프로필액티비티에서 넘어올때 정보 관리를 위해 작성한듯
    //TODO : 하브루타 강의실 예약할때는 진작에 싱크 맞춰서 넘어올 것
    //프로필 액티비티 수정사항을 메인에서 싱크맞춰주는 것. 로그인드 -> 유저데이터로의 정보이동
    //유저데이터를 유저로그인드에 맞춰서 편집중
    private void syncUserDataWithUserLogined(){
        userLogined = getSharedPreferences("userLogined",MODE_PRIVATE);
        userData = getSharedPreferences("userData",MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();

        //로그인된 유저의 자리정보 가져오기
        //이건 아마도, 프로필에서 좌석정보 수정되엇을때 싱크 맞추기 위해 작성한듯
        intent = getIntent();
        String loginedUid = intent.getStringExtra("ID");
        Log.d(MainActivity.TAG,userLogined.getAll().toString());
        String seatInfo = userLogined.getString(loginedUid,"").split(",")[5];
        String roomInfo = userLogined.getString(loginedUid,"").split(",")[6];

        //기존 유저데이터 정보 가져오기
        String _userData = userData.getString(loginedUid,"");
        String[] tmp = _userData.split(",");
        tmp[5] = seatInfo;
        tmp[6] = roomInfo;
        String userDataUpdated = TextUtils.join(",",tmp);

        //스트링 가져와서 싯인포 업데이트 후.
        //로그인드에 유저데이타 업데이티드를 넣는다?
        //유저 데이터를 로그인드에 맞게 편집한다음, 이를 로그인드에 다시 넣으니까 일치시키는 것
        editor.putString(loginedUid,userDataUpdated);
        editor.apply();

    }



    private void setLogined() {
        intent = getIntent();
        loginedUid = intent.getStringExtra("ID");
        //전 화면에서 넘어온 인텐트 값의 아이디 를 기반으로, 로그인유저 데이터를 생성
        //유저데이터를 가져오는 것은 해당 밸류들을 유저로그인에 집어넣어주기위함
        final SharedPreferences.Editor editor = userLogined.edit();
        //유저로그인 데이터를 편집. 인텐트에서 가져온 아이디 기반으로 유저로그인 데이터 생성
//        editor.putString(loginedUid, "");
        editor.putString(loginedUid, userData.getString(loginedUid, ""));
//        mFirebaseDatabaseReference.child(USERLOGINED_CHILD).child(loginedUid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
//                    String userLogined = childSnapshot.getValue(String.class);
//                    Log.d(TAG, childSnapshot.toString());
//                    if (userLogined.split(",")[0].equals(loginedUid)){
//                        editor.putString(loginedUid, userData.getString(loginedUid, ""));
//                        editor.apply();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        //유저 데이터에 존재하는, 가져온 아이디가 키값에 해당하는 밸류들로 채운다. 겟스트링은 밸류값 가져오는 것.
        Log.d(MainActivity.TAG, "로그인 유저정보 세이브 "+loginedUid + " : " + userData.getString(loginedUid, ""));
        editor.apply();
        //TODO : PUsh가 아니라 기존에 존재하는 값을 바꾸려면?
        mFirebaseDatabaseReference.child(USERLOGINED_CHILD).child(loginedUid).setValue(userData.getString(loginedUid,""));

    }

    private void resetLogined(boolean isBackBtn) {
        SharedPreferences.Editor editor = userLogined.edit();
        editor.clear();
        editor.apply();
        if (isBackBtn){
            Log.d(TAG, "????????????:");
            mFirebaseDatabaseReference.child(USERLOGINED_CHILD).child(loginedUid).removeValue();

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loginSuccess) {
            //첫 로그인 때만 실행하기 위함.
            intent = getIntent();
            if (intent.getBooleanExtra("IS_NOT_FIRST_LOGIN",false)){

            } else {
                if (intent.getBooleanExtra("IS_AUTO_LOGIN", false)) {
                    Toast.makeText(getApplicationContext(), intent.getStringExtra("ID") + "님 자동로그인 되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (intent.getStringExtra("ID") != null){
                        Toast.makeText(getApplicationContext(), intent.getStringExtra("ID") + "님 로그인 성공", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            loginSuccess = true;
            //디스트로이 되기전에는 초기화되지 않음. 고로 한번만 실행될것
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("로그아웃 하시겠어요?");
            builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean isBackBtn=true;
                    dialogInterface.dismiss();
                    intent1.putExtra("LOGOUT", true);
                    resetLogined(isBackBtn);
                    //로그인 유저정보 리셋
                    startActivity(intent1);
                    finish();
                    //액티비티 삭제
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

    private void setUI(){
        officestatusButton = (Button) findViewById(R.id.officeStatus);
        noticeButton = (Button) findViewById(R.id.notice);
        havrutaButton = (Button) findViewById(R.id.havruta);
        profileButton = (Button) findViewById(R.id.profile);


        recyclerView = findViewById(R.id.chattingView);
        recyclerView.setHasFixedSize(true);
        //리사이클러뷰를 어떤 레이아웃매니저를 설정해서 표시할 것인지. 수직 수평에 대한 설정
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(getApplicationContext(), chatList);
        //채팅 내용이 들어가는 my_chat_item.xml에 어댑터 적용
        recyclerView.setAdapter(adapter);

        chatInput = (EditText) findViewById(R.id.chat);
        myChatItem = (TextView) findViewById(R.id.my_chat_view);
        myChatTime = (TextView) findViewById(R.id.my_chat_time);
        sendButton = (Button) findViewById(R.id.send);


        //그 어댑터가 적용된 아이템을 해당 채팅 뷰에 띄우겟다는 것.
        officestatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OfficeStatusActivity.class);
                Intent intent1 = getIntent();
                loginedUid = intent1.getStringExtra("ID");
//                intent.putExtra("loginedUid",loginedUid);
                //전 화면에서 넘어온 인텐트 값의 아이디 를 기반으로, 로그인유저 데이터를 생성
                startActivity(intent);
            }
        });

        havrutaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                startActivity(intent);

            }
        });

        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);

            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                Intent intent1 = getIntent();
                loginedUid = intent1.getStringExtra("ID");
                intent.putExtra("loginedUid",loginedUid);
                //전 화면에서 넘어온 인텐트 값의 아이디 를 기반으로, 로그인유저 데이터를 생성
                startActivity(intent);

            }
        });


        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { //키보드가 올라오는 이벤트 발생시, 마지막 위치로 가기위
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });


        chatInput.setOnKeyListener(new View.OnKeyListener() { //채팅창에서 엔터키를 눌렀을때 전송버튼 누를것과 똑같이하기위함
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    if (chatInput.getText().length()==0){
                        Toast.makeText(getApplicationContext(),"메세지를 입력해 주세요",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    Date today = new Date();
                    SimpleDateFormat timeNow = new SimpleDateFormat("a K:mm");
                    String imageUri = "drawable://" + R.drawable.ic_launcher_foreground;

                    Chat chat = new Chat(userLogined.getString(loginedUid,"").split(",")[2], chatInput.getText().toString(), timeNow.format(today), imageUri,0);
                    adapter.addItem(chat);
                    //TODO : 애초에 올릴때 파이어베이스 유저기반으로 채팅 객체 생성해야함
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(chat);
                    //어댑터 새로고침
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    chatInput.setText(""); //채팅 내용 초기화? -> 채팅입력창에 찌꺼기 안남아있도록 초기화
                    return true;
                }
                return false;
            }
        });
        //send Button 클릭시 chatList 에 추가해서, rv에 올라가게 한다
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (chatInput.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"메세지를 입력해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                Date today = new Date();
                SimpleDateFormat timeNow = new SimpleDateFormat("a K:mm");
                //click시에 해당 시간도 전송하기
                String imageUri = "drawable://" + R.drawable.ic_launcher_foreground;

                Chat chat = new Chat(userLogined.getString(loginedUid,"").split(",")[2], chatInput.getText().toString(), timeNow.format(today), imageUri,0);
                adapter.addItem(chat);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(chat);
                //어댑터 새로고침
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                chatInput.setText(""); //채팅 내용 초기화? -> 채팅입력창에 찌꺼기 안남아있도록 초기화
            }
        });

        mFirebaseDatabaseReference.child(MESSAGES_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Chat chat = childSnapshot.getValue(Chat.class);
                    Log.d(TAG, childSnapshot.toString());
                    if (chat.getNickname().equals(userLogined.getString(loginedUid,"").split(",")[2])){
                        Log.d(TAG,"내가 친 채팅 불러오기");
                        chat.setWho(0);
                    } else {
                        Log.d(TAG, "남이 친 채팅 불러오기");
                        chat.setWho(1);
                    }
                    adapter.addItem(chat);
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() -1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
