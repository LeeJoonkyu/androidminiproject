package com.example.spacenova.reservation_activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;
import com.example.spacenova.Room;
import com.example.spacenova.RoomFirebase;
import com.example.spacenova.SeatFirebase;
import com.example.spacenova.activity.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Office1_ReservationActivity extends AppCompatActivity implements View.OnClickListener {

    private String answerNo = "예약이 취소되었습니다";
    private String answerYes = "예약이 완료되었습니다";


    private ArrayList<Room> roomTimeArrayList = new ArrayList<>();
    private ArrayList<TextView> tvArrayList = new ArrayList<>();
    private ArrayList<Button> buttonArrayList = new ArrayList<>();


    private JSONArray office1_jsonArray;
    private JSONArray _office1_jsonArray;

    private SharedPreferences roomData;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private TextView officeTitle;
    private TextView tvMonOffice10900;
    private Button btnMonOffice10900;
    private TextView tvMonOffice11100;
    private Button btnMonOffice11100;
    private TextView tvMonOffice11300;
    private Button btnMonOffice11300;
    private TextView tvMonOffice11500;
    private Button btnMonOffice11500;
    private TextView tvMonOffice11700;
    private Button btnMonOffice11700;
    private TextView tvMonOffice11900;
    private Button btnMonOffice11900;

    private EditText inputName;

    private SharedPreferences userData;
    private SharedPreferences userLogined;
    private String whichDaywhichRoom;
    private String whichToPutToRoomData;
    private String whichDayToRoomDate;

    private String dayInKorean;

    JSONArray _office_jsonArray;

    private FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabaseReference = mfirebaseDatabase.getReference();

    public static final String ROOM_CHILD = "roomInfo";
    public static final String OFFICE6_CHILD = "office6_seatInfo";
    public static final String USERLOGINED_CHILD = "userLogined";
    public static final String USERS_CHILD = "users";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.office_reservation_layout);
        findViews();
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        userLogined = getSharedPreferences("userLogined", MODE_PRIVATE);
        roomData = getSharedPreferences("roomData", MODE_PRIVATE);
        setUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        syncRoomStatusImgOnFirebase();
    }

    private void setUI() {
        Intent intent = getIntent();
        whichDaywhichRoom = intent.getStringExtra("reserveWhich");
        Log.d(MainActivity.TAG, whichDaywhichRoom);
        //이걸 쓰면 이제 wed_office1_roomInfo 식으로 정보저장이 가능
        whichToPutToRoomData = whichDaywhichRoom + "_roomInfo";
        //wed -> Wed로 바꾸기
        //firebase에 저장할때도 아주 중요한 키값으로 사용됨
        whichDayToRoomDate = whichDaywhichRoom.split("_")[0].substring(0, 1).toUpperCase() + whichDaywhichRoom.split("_")[0].substring(1);
        Log.d(MainActivity.TAG, whichDayToRoomDate);
        int roomNo = Integer.parseInt(whichDaywhichRoom.split("_")[1].substring(6));
        Log.d(MainActivity.TAG, "" + roomNo);

        //dayInKorean은 유저의 정보 변경시에 쓰일 것
        if (whichDayToRoomDate.equals("Mon")) {
            dayInKorean = "월요일";
        } else if (whichDayToRoomDate.equals("Tue")) {
            dayInKorean = "화요일";
        } else if (whichDayToRoomDate.equals("Wed")) {
            dayInKorean = "수요일";
        } else if (whichDayToRoomDate.equals("Thu")) {
            dayInKorean = "목요일";
        } else if (whichDayToRoomDate.equals("Fri")) {
            dayInKorean = "금요일";
        } else if (whichDayToRoomDate.equals("Sat")) {
            dayInKorean = "토요일";
        } else if (whichDayToRoomDate.equals("Sun")) {
            dayInKorean = "일요일";
        }

        //TODO : 정보를 파이어베이스에 다룰수 있도록 올리고 내리도록

        //TODO : whichDayToRoomDate와 현재시간 비교해서 현재시간에 해당하는 액티비티만 보여주기


        //액티비티 생성시 사무실의 모든 사용 시간을 관리하는 arraylist초기화
        roomTimeArrayList.add(new Room(roomNo, whichDayToRoomDate + ",09:00:00", false, "", ""));
        roomTimeArrayList.add(new Room(roomNo, whichDayToRoomDate + ",11:00:00", false, "", ""));
        roomTimeArrayList.add(new Room(roomNo, whichDayToRoomDate + ",13:00:00", false, "", ""));
        roomTimeArrayList.add(new Room(roomNo, whichDayToRoomDate + ",15:00:00", false, "", ""));
        roomTimeArrayList.add(new Room(roomNo, whichDayToRoomDate + ",17:00:00", false, "", ""));
        roomTimeArrayList.add(new Room(roomNo, whichDayToRoomDate + ",19:00:00", false, "", ""));

        createTextViewList();
        createButtonList();


        //이를 바탕으로 office6_jsonArray 생성
        office1_jsonArray = new JSONArray();
        for (int i = 0; i < roomTimeArrayList.size(); i++) {
            office1_jsonArray.put(roomTimeArrayList.get(i).getJSONObject());
        }

        syncRoomStatusImgOnFirebase();
        Log.d(MainActivity.TAG, "온 크리에이트 당시 쉐어드에 저장된 강의실 정보 값 : " + roomData.getAll().toString());
        Log.d(MainActivity.TAG, "초기화된 제이슨 배열 값 : " + office1_jsonArray.toString());
        //처음에 초기화된 제이슨을 넣어준 적이 없다면, 초기화하고 그렇지 않다면 만들어진 제이슨을 쉐어드에 저장. 이 저장 값을 기반으로 진행
        if (!roomData.getBoolean("IS_" + whichDayToRoomDate.toUpperCase() + "_OFFICE1_INIT_BEFORE", false)) {
            initRoomDataInfo();
            Log.d(MainActivity.TAG, "쉐어드 이닛 당시, 쉐어드에 저장된 강의실 정보 값 : " + roomData.getAll().toString());

        } else {
            syncRoomStatusImgWithPref();
            Log.d(MainActivity.TAG, "쉐어드 이닛 말고 싱크 이미지 당시, 쉐어드에 저장된 강의실 정보 값 : " + roomData.getAll().toString());

        }
    }

    public void syncRoomStatusImgOnFirebase() {


        mFirebaseDatabaseReference.child(ROOM_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isBreak = false;
                int donotchange = -1;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    //여기서는 룸인포의 자식인 fri, mon, sat...
//                    Log.d("싱크룸 키, 차일드 ", childSnapshot.getKey() + childSnapshot);
                    for (DataSnapshot grandchildSnapshot : childSnapshot.getChildren()) {
                        //여기서는 fri의 자식인 1사, 4사, 5사
                        //Log.d("싱크룸 키, 그랜드차일드 ", grandchildSnapshot.getKey() + grandchildSnapshot);
                        if (grandchildSnapshot.getKey().equals("1사무실")){
                            //월요일 1사무실, 화요일 1사무실... 다돌것
                            for (DataSnapshot grandgrandchildSnapshot : grandchildSnapshot.getChildren()) {
                                //여기서는 비로소 사무실의 자식인 09:00 ...
                                String key = grandgrandchildSnapshot.getKey();
                                Log.d("어디? ", childSnapshot.getKey() + " " +grandchildSnapshot.getKey() + " " + grandgrandchildSnapshot.getKey());
                                //tvarraylist get(n)을 위함
                                int n = -1;
                                if (key.equals("09:00")) {
                                    n = 0;
                                } else if (key.equals("11:00")) {
                                    n = 1;
                                } else if (key.equals("13:00")) {
                                    n = 2;
                                } else if (key.equals("15:00")) {
                                    n = 3;
                                } else if (key.equals("17:00")) {
                                    n = 4;
                                } else if (key.equals("19:00")) {
                                    n = 5;
                                }
                                RoomFirebase roomFirebase = grandgrandchildSnapshot.getValue(RoomFirebase.class);
                                if (roomFirebase.isOccupied) {
                                    Log.d("dd?",n + "ocococo");

                                    tvArrayList.get(n).setBackgroundColor(Color.parseColor("#E91E1E"));
                                    tvArrayList.get(n).setTextColor(Color.parseColor("#FFFFFF"));
                                    tvArrayList.get(n).setText(roomFirebase.memo + " 예약 중");
                                    buttonArrayList.get(n).setText("일찍 마치셨으면 반환해주세요!");
                                    buttonArrayList.get(n).setClickable(false);
                                    donotchange = n;
                                    Log.d("???????????",""+donotchange);
                                }
                                //TODO: 여기가 완성되어야 반환이 뷰에 반영되는 부분이나, 예약이동 시 중복 방지가 완성됨.
//                                else {
//                                    Log.d("dd?",n + "UNOCCUPIED");
//                                    if (n!=donotchange){
//                                        tvArrayList.get(n).setText("예약가능");
//                                        tvArrayList.get(n).setBackgroundColor(Color.parseColor("#005eff"));
//                                        buttonArrayList.get(n).setClickable(true);
//                                        buttonArrayList.get(n).setText("하브루타 예약 하시겠어요?");
//                                    }
//
//                                }
                            }
                        }
//                        else {
//                            isBreak = true;
//                            break;
//                        }
                    }
//                    if (isBreak){
//                        break;
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void syncSharedPrefWithJsonArray(JSONArray _office1_jsonArray) {
        SharedPreferences.Editor editor = roomData.edit();
        editor.putString(whichToPutToRoomData, _office1_jsonArray.toString());
        editor.apply();
    }

    private void editRoomData(final Button reserveButton, final TextView tv, final int index) {

        //뷰의 변경
        String value = inputName.getText().toString();
        tv.setBackgroundColor(Color.parseColor("#E91E1E"));
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setText(value + " 예약 중");
        reserveButton.setText("일찍 마치셨으면 반환해주세요!");
        reserveButton.setClickable(false);
        //쉐어드에 저장된 스트링으로 들어가있는 제이슨어레이를
        //스트링투제이슨 어레이를 통해 바꿔준다음
        //해당값변경후
        //풋
        _office1_jsonArray = stringToJSONArray();
        for (int n = 0; n < _office1_jsonArray.length(); n++) {
            if (n == index) { //몇번째 오픈되는 강의실인지가 인덱스넘버
                try {
                    //아이디, 메모 수정
                    JSONObject object = _office1_jsonArray.getJSONObject(n);
                    object.put("isOccupied", true);
                    object.put("usedById", MainActivity.loginedUid);
                    object.put("memo", value);
                    Log.d(MainActivity.TAG, "파싱 후 제이슨 : " + _office1_jsonArray);
                    //해당 제이슨 배열과 같이 쉐어드 역시 싱크를 맞춰준다.
                    syncSharedPrefWithJsonArray(_office1_jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        String time = "";
        if (index == 0) {
            time = "09:00";
        } else if (index == 1) {
            time = "11:00";
        } else if (index == 2) {
            time = "13:00";
        } else if (index == 3) {
            time = "15:00";
        } else if (index == 4) {
            time = "17:00";
        } else if (index == 5) {
            time = "19:00";
        }

        mFirebaseDatabaseReference.child(ROOM_CHILD).child(whichDayToRoomDate).child("1사무실").child(time).setValue(new RoomFirebase(true, MainActivity.loginedUid, value));


    }

    private void editUserData(int index) {
        String time = "";
        if (index == 0) {
            time = "09:00";
        } else if (index == 1) {
            time = "11:00";
        } else if (index == 2) {
            time = "13:00";
        } else if (index == 3) {
            time = "15:00";
        } else if (index == 4) {
            time = "17:00";
        } else if (index == 5) {
            time = "19:00";
        }
        //이걸로 진행하려했지만 애초에 로직이 좀 꼬여서, 돌아가는 방식
//        SharedPreferences.Editor editor = userData.edit();

        SharedPreferences.Editor editor = userLogined.edit();
        String userInfoBefore = userLogined.getString(MainActivity.loginedUid, "");
        //가져와서, 강의실값을 업데이트해준다
        String infoarr[] = userInfoBefore.split(",");
        Log.d(MainActivity.TAG, userInfoBefore);
        Log.d(MainActivity.TAG, Arrays.toString(infoarr));
        //TODO : 이 모든 반영 사항은 userData에는 들어가지 않고잇다. 메인에 가야 비로소 싱크가 맞춰진다
        //이로 인해 유저데이터를 먼저 편집하고, 로그인드 유저를 나중에 편집하는 방안을 쓰려햇지만 좀 꼬엿다
        //다른 작업을 할때 메인에 가야 싱크가 맞춰진다는 사실을 명심할것
        infoarr[6] = dayInKorean + " 1사무실 " + time;
        editor.putString(MainActivity.loginedUid, TextUtils.join(",", infoarr));
        editor.apply();
        mFirebaseDatabaseReference.child(USERLOGINED_CHILD).child(MainActivity.loginedUid).setValue(TextUtils.join(",", infoarr));
        mFirebaseDatabaseReference.child(USERS_CHILD).child(MainActivity.loginedUid).setValue(TextUtils.join(",", infoarr));

    }

    private void buildDialog(final Button reserveButton, final TextView tv, final int index) {
        inputName = new EditText(this);
        inputName.setText(userLogined.getString(MainActivity.loginedUid, "").split(",")[7]);
        builder = new AlertDialog.Builder(Office1_ReservationActivity.this);
        builder.setMessage("예약자 이름을 입력하세요");
        builder.setCancelable(false).setView(inputName).setPositiveButton("예약", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!inputName.getText().toString().equals(userLogined.getString(MainActivity.loginedUid, "").split(",")[7])) {
                    Toast.makeText(getApplicationContext(), "예약자 본인의 실명을 입력하셔야 합니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), answerYes, Toast.LENGTH_SHORT).show();
                //강의실 occupied여부 업데이트 + 누가 예약햇는지 업데이트(id값) + 실명텍스트를 메모에 적기
                editRoomData(reserveButton, tv, index);

                if (!getSharedPreferences("userLogined", MODE_PRIVATE).getString(MainActivity.loginedUid, "").split(",")[6].equals("null")) {
                    //강의실 예약정보가 존재한다면,
                    //해당 시간을 제이슨에서 unoccupied처리, 그리고 쉐어드에 반영, 그리고 이미지 처리
                    //09:00의 룸인덱스를 어떻게 처리할것인지?
                    changeRoomStatusWhenReservationChanged(index);
                    editUserData(index);
                    Log.d(MainActivity.TAG, "자리 이동 후 제이슨이 반영된 쉐어드 값 : " + roomData.getAll().toString());

                } else { //유저에게 강의실 예약정보가 없는 경우
                    editUserData(index);
                    Log.d(MainActivity.TAG, "자리 정보 없는 경우 제이슨이 반영된 쉐어드 값 : " + roomData.getAll().toString());
                }
                //유저데이터 업데이트 -> 메인가면 싱크맞춰지니까 userLogined는 안맞춰도되는 걸로
//                editUserData(index);

            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), answerNo, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog = builder.create();
    }

    private void changeRoomStatusWhenReservationChanged(int indextochange) {
        String _roomTimeBefore = getSharedPreferences("userLogined", MODE_PRIVATE).getString(MainActivity.loginedUid, "").split(",")[6].split(" ")[2];
        //파이어베이스 처리를 위함
        String _roomDayBefore = getSharedPreferences("userLogined", MODE_PRIVATE).getString(MainActivity.loginedUid, "").split(",")[6].split(" ")[0];
        String _roomNoBefore = getSharedPreferences("userLogined", MODE_PRIVATE).getString(MainActivity.loginedUid, "").split(",")[6].split(" ")[1];

        int index = -1;
        String roomdayBeforeForFirebase = "";
        //월요일 1사무실의 처리
        if (_roomTimeBefore.equals("09:00")) {
            index = 0;
        } else if (_roomTimeBefore.equals("11:00")) {
            index = 1;
        } else if (_roomTimeBefore.equals("13:00")) {
            index = 2;
        } else if (_roomTimeBefore.equals("15:00")) {
            index = 3;
        } else if (_roomTimeBefore.equals("17:00")) {
            index = 4;
        } else if (_roomTimeBefore.equals("19:00")) {
            index = 5;
        }

        if (_roomDayBefore.equals("월요일")) {
            roomdayBeforeForFirebase = "Mon";
        } else if (_roomDayBefore.equals("화요일")) {
            roomdayBeforeForFirebase = "Tue";
        } else if (_roomDayBefore.equals("수요일")) {
            roomdayBeforeForFirebase = "Wed";
        } else if (_roomDayBefore.equals("목요일")) {
            roomdayBeforeForFirebase = "Thu";
        } else if (_roomDayBefore.equals("금요일")) {
            roomdayBeforeForFirebase = "Fri";
        } else if (_roomDayBefore.equals("토요일")) {
            roomdayBeforeForFirebase = "Sat";
        } else if (_roomDayBefore.equals("일요일")) {
            roomdayBeforeForFirebase = "Sun";
        }


        //반환할 자리 번호 파싱
        //쉐어드 값 수정하기
        _office_jsonArray = stringToJSONArrayInRoomData();
        //자리정보 없어서 null일때 반환누르면 터짐 if문 추가
        if (_office_jsonArray != null) {
            Log.d(MainActivity.TAG, "!@#!$1");
            for (int n = 0; n < _office_jsonArray.length(); n++) {
                Log.d(MainActivity.TAG, "!@#!$2");
                if (n == index) { //자리값에 해당하는 정보만 가져오기
                    try {
                        Log.d(MainActivity.TAG, "!@#!$3");
                        JSONObject object = _office_jsonArray.getJSONObject(n);
                        object.put("isOccupied", false);
                        object.put("usedById", "");
                        object.put("memo", "");
                        Log.d(MainActivity.TAG, "파싱 후 제이슨 : " + _office_jsonArray);
                        //이건 액티비티 1,4,5사무실 전환시에 같은 인덱스 예컨대
                        //화19:00 -> 수19:00 로 변경시 기존의 UI 세팅이 날아가버리는
                        //문제를 해결하기 위함
                        if (index != indextochange) {
                            tvArrayList.get(index).setText("예약가능");
                            tvArrayList.get(index).setBackgroundColor(Color.parseColor("#005eff"));
                            buttonArrayList.get(index).setClickable(true);
                            buttonArrayList.get(index).setText("하브루타 예약 하시겠어요?");

                        }

                        syncJsonWithSharedPrefHavruta(_office_jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        mFirebaseDatabaseReference.child(ROOM_CHILD).child(roomdayBeforeForFirebase).child(_roomNoBefore).child(_roomTimeBefore).setValue(new RoomFirebase(false, "", ""));

    }


    public JSONArray stringToJSONArrayInRoomData() {

        //월요일 1사무실 09:00
        String[] reserveWhen = userLogined.getString(MainActivity.loginedUid, "").split(",")[6].split(" ");
        String strJsonArray = null;
        Log.d(MainActivity.TAG, Arrays.toString(reserveWhen));

        if (reserveWhen[0].equals("월요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                strJsonArray = roomData.getString("mon_office1_roomInfo", "");
            } else if (reserveWhen[1].equals("4사무실")) {
                strJsonArray = roomData.getString("mon_office4_roomInfo", "");
            } else if (reserveWhen[1].equals("5사무실")) {
                strJsonArray = roomData.getString("mon_office5_roomInfo", "");
            }
        } else if (reserveWhen[0].equals("화요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                strJsonArray = roomData.getString("tue_office1_roomInfo", "");
            } else if (reserveWhen[1].equals("4사무실")) {
                strJsonArray = roomData.getString("tue_office4_roomInfo", "");
            } else if (reserveWhen[1].equals("5사무실")) {
                strJsonArray = roomData.getString("tue_office5_roomInfo", "");
            }
        } else if (reserveWhen[0].equals("수요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                strJsonArray = roomData.getString("wed_office1_roomInfo", "");
            } else if (reserveWhen[1].equals("4사무실")) {
                strJsonArray = roomData.getString("wed_office4_roomInfo", "");
            } else if (reserveWhen[1].equals("5사무실")) {
                strJsonArray = roomData.getString("wed_office5_roomInfo", "");
            }
        } else if (reserveWhen[0].equals("목요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                strJsonArray = roomData.getString("thu_office1_roomInfo", "");
            } else if (reserveWhen[1].equals("4사무실")) {
                strJsonArray = roomData.getString("thu_office4_roomInfo", "");
            } else if (reserveWhen[1].equals("5사무실")) {
                strJsonArray = roomData.getString("thu_office5_roomInfo", "");
            }
        } else if (reserveWhen[0].equals("금요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                strJsonArray = roomData.getString("fri_office1_roomInfo", "");
            } else if (reserveWhen[1].equals("4사무실")) {
                strJsonArray = roomData.getString("fri_office4_roomInfo", "");
                Log.d(MainActivity.TAG, "!!!");
            } else if (reserveWhen[1].equals("5사무실")) {
                strJsonArray = roomData.getString("fri_office5_roomInfo", "");
            }
        } else if (reserveWhen[0].equals("토요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                strJsonArray = roomData.getString("sat_office1_roomInfo", "");
            } else if (reserveWhen[1].equals("4사무실")) {
                strJsonArray = roomData.getString("sat_office4_roomInfo", "");
            } else if (reserveWhen[1].equals("5사무실")) {
                strJsonArray = roomData.getString("sat_office5_roomInfo", "");
            }
        } else if (reserveWhen[0].equals("일요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                strJsonArray = roomData.getString("sun_office1_roomInfo", "");
            } else if (reserveWhen[1].equals("4사무실")) {
                strJsonArray = roomData.getString("sun_office4_roomInfo", "");
            } else if (reserveWhen[1].equals("5사무실")) {
                strJsonArray = roomData.getString("sun_office5_roomInfo", "");
            }
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

    private void syncJsonWithSharedPrefHavruta(JSONArray _office_jsonArray) {
        SharedPreferences.Editor editor = roomData.edit();

        String[] reserveWhen = userLogined.getString(MainActivity.loginedUid, "").split(",")[6].split(" ");
        Log.d(MainActivity.TAG, Arrays.toString(reserveWhen));
        if (reserveWhen[0].equals("월요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                editor.putString("mon_office1_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("4사무실")) {
                editor.putString("mon_office4_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("5사무실")) {
                editor.putString("mon_office5_roomInfo", _office_jsonArray.toString());
            }
        } else if (reserveWhen[0].equals("화요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                editor.putString("tue_office1_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("4사무실")) {
                editor.putString("tue_office4_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("5사무실")) {
                editor.putString("tue_office5_roomInfo", _office_jsonArray.toString());
            }
        } else if (reserveWhen[0].equals("수요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                editor.putString("wed_office1_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("4사무실")) {
                editor.putString("wed_office4_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("5사무실")) {
                editor.putString("wed_office5_roomInfo", _office_jsonArray.toString());
            }
        } else if (reserveWhen[0].equals("목요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                editor.putString("thu_office1_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("4사무실")) {
                editor.putString("thu_office4_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("5사무실")) {
                editor.putString("thu_office5_roomInfo", _office_jsonArray.toString());
            }
        } else if (reserveWhen[0].equals("금요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                editor.putString("fri_office1_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("4사무실")) {
                editor.putString("fri_office4_roomInfo", _office_jsonArray.toString());
                Log.d(MainActivity.TAG, "?????");
            } else if (reserveWhen[1].equals("5사무실")) {
                editor.putString("fri_office5_roomInfo", _office_jsonArray.toString());
            }
        } else if (reserveWhen[0].equals("토요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                editor.putString("sat_office1_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("4사무실")) {
                editor.putString("sat_office4_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("5사무실")) {
                editor.putString("sat_office5_roomInfo", _office_jsonArray.toString());
            }
        } else if (reserveWhen[0].equals("일요일")) {
            if (reserveWhen[1].equals("1사무실")) {
                editor.putString("sun_office1_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("4사무실")) {
                editor.putString("sun_office4_roomInfo", _office_jsonArray.toString());
            } else if (reserveWhen[1].equals("5사무실")) {
                editor.putString("sun_office5_roomInfo", _office_jsonArray.toString());
            }
        }
        Log.d(MainActivity.TAG, "?????");
        editor.apply();
        Log.d(MainActivity.TAG, "반환 후 쉐어드" + roomData.getString("fri_office4_roomInfo", ""));


    }


    public void initRoomDataInfo() {
        SharedPreferences.Editor editor = roomData.edit();
        //jsonArray를 String으로 바꾸어서 넣어준다

        editor.putString(whichToPutToRoomData, office1_jsonArray.toString());
        editor.putBoolean("IS_" + whichDayToRoomDate.toUpperCase() + "_OFFICE1_INIT_BEFORE", true);
        editor.apply();
    }
    //TODO : 1사무실, 4사무실, 5사무실의 텍스트는 visible -> gone처리를 한다면 액티비티 하나에서 할 수 있지 않을까

    private void syncRoomStatusImgWithPref() {
        JSONArray jarr = stringToJSONArray();

        int i;
        for (int n = 0; n < jarr.length(); n++) {
            try {
                JSONObject object = jarr.getJSONObject(n);
                if (object.getBoolean("isOccupied")) {
                    //jsonArray중 n번째가 점유중이라면,
                    //n번째 tv를 바꿔줘야한다.
                    //n번째 버튼도.
                    String value = object.getString("memo");
                    tvArrayList.get(n).setBackgroundColor(Color.parseColor("#E91E1E"));
                    tvArrayList.get(n).setTextColor(Color.parseColor("#FFFFFF"));
                    tvArrayList.get(n).setText(value + " 예약 중");
                    buttonArrayList.get(n).setText("일찍 마치셨으면 반환해주세요!");
                    buttonArrayList.get(n).setClickable(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public JSONArray stringToJSONArray() {

        String strJsonArray = roomData.getString(whichToPutToRoomData, "");
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

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2019-09-26 17:48:28 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        officeTitle = (TextView) findViewById(R.id.office_title);
        officeTitle.setText("1사무실");
        tvMonOffice10900 = (TextView) findViewById(R.id.tv_mon_office1_0900);
        btnMonOffice10900 = (Button) findViewById(R.id.btn_mon_office1_0900);
        tvMonOffice11100 = (TextView) findViewById(R.id.tv_mon_office1_1100);
        btnMonOffice11100 = (Button) findViewById(R.id.btn_mon_office1_1100);
        tvMonOffice11300 = (TextView) findViewById(R.id.tv_mon_office1_1300);
        btnMonOffice11300 = (Button) findViewById(R.id.btn_mon_office1_1300);
        tvMonOffice11500 = (TextView) findViewById(R.id.tv_mon_office1_1500);
        btnMonOffice11500 = (Button) findViewById(R.id.btn_mon_office1_1500);
        tvMonOffice11700 = (TextView) findViewById(R.id.tv_mon_office1_1700);
        btnMonOffice11700 = (Button) findViewById(R.id.btn_mon_office1_1700);
        tvMonOffice11900 = (TextView) findViewById(R.id.tv_mon_office1_1900);
        btnMonOffice11900 = (Button) findViewById(R.id.btn_mon_office1_1900);

        btnMonOffice10900.setOnClickListener(this);
        btnMonOffice11100.setOnClickListener(this);
        btnMonOffice11300.setOnClickListener(this);
        btnMonOffice11500.setOnClickListener(this);
        btnMonOffice11700.setOnClickListener(this);
        btnMonOffice11900.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2019-09-26 17:48:28 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnMonOffice10900) {
            buildDialog((Button) v, tvMonOffice10900, 0);
            builder.show();
        } else if (v == btnMonOffice11100) {
            buildDialog((Button) v, tvMonOffice11100, 1);
            builder.show();
        } else if (v == btnMonOffice11300) {
            buildDialog((Button) v, tvMonOffice11300, 2);
            builder.show();
        } else if (v == btnMonOffice11500) {
            buildDialog((Button) v, tvMonOffice11500, 3);
            builder.show();
        } else if (v == btnMonOffice11700) {
            buildDialog((Button) v, tvMonOffice11700, 4);
            builder.show();
        } else if (v == btnMonOffice11900) {
            buildDialog((Button) v, tvMonOffice11900, 5);
            builder.show();
        }
    }

    private void createTextViewList() {
        tvArrayList.add(tvMonOffice10900);
        tvArrayList.add(tvMonOffice11100);
        tvArrayList.add(tvMonOffice11300);
        tvArrayList.add(tvMonOffice11500);
        tvArrayList.add(tvMonOffice11700);
        tvArrayList.add(tvMonOffice11900);


    }

    private void createButtonList() {

        buttonArrayList.add(btnMonOffice10900);
        buttonArrayList.add(btnMonOffice11100);
        buttonArrayList.add(btnMonOffice11300);
        buttonArrayList.add(btnMonOffice11500);
        buttonArrayList.add(btnMonOffice11700);
        buttonArrayList.add(btnMonOffice11900);

    }


}
