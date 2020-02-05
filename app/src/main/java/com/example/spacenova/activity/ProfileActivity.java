package com.example.spacenova.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;
import com.example.spacenova.RoomFirebase;
import com.example.spacenova.SeatFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    private TextView tv_page;

    private TextView tv_id;
    private EditText et_id;
    private TextView tv_pw;
    private EditText et_pw;
    private Button changePwBtn;

    private TextView tv_nick;
    private EditText et_nick;
    private TextView tv_nickChange;
    private TextView tv_email;
    private EditText et_email;
    private TextView tv_phone;
    private EditText et_phone;
    private TextView tv_name;
    private EditText et_name;

    private TextView tv_seat;
    private TextView tv_seat_info;
    private Button seat_return;
    private TextView tv_havruta;
    private TextView tv_havruta_info;
    private Button havruta_return;

    private Button changeUserInfoBtn;


    private String uid;
    private String pw;
    private String nick;
    private String email;
    private String phoneNo;
    private String seatInfo;
    private String havrutaInfo;
    private String name;

    //처음 여기 진입할땐 이미 유효하니까.
    private boolean isNicknameUnique = true;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private String answerYes = "자리를 반환했습니다";
    private String answerNo = "자리 반환을 취소했습니다";


    private JSONArray _office6_jsonArray;
    private JSONArray _office_jsonArray;

    private SharedPreferences userLogined;
    private SharedPreferences office6_seatInfo;
    private SharedPreferences userData;
    private SharedPreferences roomData;

    private FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabaseReference = mfirebaseDatabase.getReference();

    public static final String ROOM_CHILD = "roomInfo";
    public static final String OFFICE6_CHILD = "office6_seatInfo";
    public static final String USERLOGINED_CHILD = "userLogined";
    public static final String USERS_CHILD = "users";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        userLogined = getSharedPreferences("userLogined", MODE_PRIVATE);
        office6_seatInfo = getSharedPreferences("office6_seatInfo", MODE_PRIVATE);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        roomData = getSharedPreferences("roomData", MODE_PRIVATE);
        //        String tmp = userLogined.getAll().toString();
        //그냥 쉐어드 첫번째에 아이디값을넣어라.
//        String[] tmp1 = tmp.split("=");
//        String tmp = userLogined.getAll().toString();

        String tmp = userLogined.getString(MainActivity.loginedUid, "");

        uid = tmp.split(",")[0];
        pw = tmp.split(",")[1];
        nick = tmp.split(",")[2];
        email = tmp.split(",")[3];
        phoneNo = tmp.split(",")[4];
        Log.d(MainActivity.TAG, Arrays.toString(tmp.split(",")));
        seatInfo = tmp.split(",")[5];
        havrutaInfo = tmp.split(",")[6].substring(0, tmp.split(",")[6].length());
        //뒤의 } 떼기 //getAll을 한게 아니고 겟스트링햇기떄문에 불필요한작업. { } 떼는 것은 불필요한 작업
        name = tmp.split(",")[7];


        tv_page = (TextView) findViewById(R.id.profile_page);

        tv_id = findViewById(R.id.profile_id);
        et_id = findViewById(R.id.profile_id_et);
        tv_pw = findViewById(R.id.profile_pw);
        et_pw = findViewById(R.id.profile_pw_et);
        changePwBtn = findViewById(R.id.changePwBtn);
        tv_nick = findViewById(R.id.profile_nickname);
        et_nick = findViewById(R.id.profile_nickname_et);
        tv_nickChange = findViewById(R.id.checkChangedNickAvailable);
        tv_email = findViewById(R.id.profile_email);
        et_email = findViewById(R.id.profile_email_et);
        tv_phone = findViewById(R.id.profile_phoneNo);
        et_phone = findViewById(R.id.profile_phoneNo_et);

        tv_name = findViewById(R.id.profile_name);
        et_name = findViewById(R.id.profile_name_et);


        tv_seat = findViewById(R.id.profile_seat);
        tv_seat_info = findViewById(R.id.profile_seat_info);
        seat_return = findViewById(R.id.seat_return);

        tv_havruta = findViewById(R.id.profile_havruta);
        tv_havruta_info = findViewById(R.id.profile_havruta_info);
        havruta_return = findViewById(R.id.havruta_return);
        changeUserInfoBtn = findViewById(R.id.changeUserInfoBtn);


        et_id.setText(uid);
        et_id.setEnabled(false);
        et_id.setTextColor(Color.parseColor("#000000"));
        et_pw.setText(pw);
        et_pw.setEnabled(false);
        et_pw.setTextColor(Color.parseColor("#000000"));

        et_nick.setText(nick);
        et_email.setText(email);
        et_email.setEnabled(false);
        et_email.setTextColor(Color.parseColor("#000000"));

        et_phone.setText(phoneNo);
        et_phone.setEnabled(false);
        et_phone.setTextColor(Color.parseColor("#000000"));

        et_name.setText(name);
        et_name.setEnabled(false);
        et_name.setTextColor(Color.parseColor("#000000"));


        tv_seat_info.setText(seatInfo);
        tv_havruta_info.setText(havrutaInfo);

        changePwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("비밀번호를 변경 하시겠어요?");
                builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "수정할 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                        et_pw.setEnabled(true);

                    }
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "비밀번호 변경이 취소되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog = builder.create();
                builder.show();
            }
        });

        seat_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(MainActivity.TAG, "자리반환 온클릭 만들어젼시?");
                builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("자리를 반환하시겠어요?");
                builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                    int _seatNum;

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), answerYes, Toast.LENGTH_SHORT).show();
                        //6사무실 1번
                        if (tv_seat_info.getText().toString().split(" ")[0].equals("6사무실")) {
                            //반환할 자리 번호 파싱
                            _seatNum = Integer.parseInt(tv_seat_info.getText().toString().split(" ")[1].substring(0, 1));
                            //쉐어드 값 수정하기

                            _office6_jsonArray = stringToJSONArray();
                            if (_office6_jsonArray != null) {
                                for (int n = 0; n < _office6_jsonArray.length(); n++) {
                                    if (n == _seatNum - 1) { //자리값에 해당하는 정보만 가져오기
                                        try {
                                            JSONObject object = _office6_jsonArray.getJSONObject(n);
                                            object.put("isOccupied", false);
                                            Log.d(MainActivity.TAG, "파싱 후 제이슨 : " + _office6_jsonArray);
                                            syncJsonWithSharedPref(_office6_jsonArray);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                tv_seat_info.setText("null");
                                mFirebaseDatabaseReference.child(OFFICE6_CHILD).child(String.valueOf(_seatNum)).setValue(new SeatFirebase(false));
                                initUserSeatInfo();
                            }

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


        //애초에 처음 화면이 생성될떄는 기본 레이아웃의 상태가 오는데
        //그걸 유저데이터와 싱크맞춰서
        //예약된것만 바꿔주는 거기때문에
        //isoccupied false인 경우에 이미지 상태 변환을 따로 해줄 필요가없ㅇ는 것.
        havruta_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(MainActivity.TAG, "하브루타반환 온클릭 만들어졋나?");
                builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("하브루타 강의실을 반환하시겠어요?");
                builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "강의실을 반환했습니다", Toast.LENGTH_SHORT).show();
                        //6사무실 1번
                        int index = -1;
                        //firebase 수정을 위함
                        String time = "";
                        String whichRoom = "";
                        //월요일 1사무실의 처리
                        if (tv_havruta_info.getText().toString().split(" ")[2].equals("09:00")) {
                            index = 0;
                            time = "09:00";
                        } else if (tv_havruta_info.getText().toString().split(" ")[2].equals("11:00")) {
                            index = 1;
                            time = "11:00";

                        } else if (tv_havruta_info.getText().toString().split(" ")[2].equals("13:00")) {
                            index = 2;
                            time = "13:00";

                        } else if (tv_havruta_info.getText().toString().split(" ")[2].equals("15:00")) {
                            index = 3;
                            time = "15:00";

                        } else if (tv_havruta_info.getText().toString().split(" ")[2].equals("17:00")) {
                            index = 4;
                            time = "17:00";

                        } else if (tv_havruta_info.getText().toString().split(" ")[2].equals("19:00")) {
                            index = 5;
                            time = "19:00";

                        }

                        String whichDay = "";
                        if (tv_havruta_info.getText().toString().split(" ")[0].equals("월요일")) {
                            whichDay = "Mon";
                        } else if (tv_havruta_info.getText().toString().split(" ")[0].equals("화요일")) {
                            whichDay = "Tue";
                        } else if (tv_havruta_info.getText().toString().split(" ")[0].equals("수요일")) {
                            whichDay = "Wed";
                        } else if (tv_havruta_info.getText().toString().split(" ")[0].equals("목요일")) {
                            whichDay = "Thu";
                        } else if (tv_havruta_info.getText().toString().split(" ")[0].equals("금요일")) {
                            whichDay = "Fri";
                        } else if (tv_havruta_info.getText().toString().split(" ")[0].equals("토요일")) {
                            whichDay = "Sat";
                        } else if (tv_havruta_info.getText().toString().split(" ")[0].equals("일요일")) {
                            whichDay = "Sun";
                        }

                        if (tv_havruta_info.getText().toString().split(" ")[1].equals("1사무실")) {
                            whichRoom = "1사무실";
                        } else if (tv_havruta_info.getText().toString().split(" ")[1].equals("4사무실")) {
                            whichRoom = "4사무실";
                        } else if (tv_havruta_info.getText().toString().split(" ")[1].equals("5사무실")) {
                            whichRoom = "5사무실";
                        }





                        //반환할 자리 번호 파싱
                        //쉐어드 값 수정하기
                        _office_jsonArray = stringToJSONArrayInRoomData();
                        //자리정보 없어서 null일때 반환누르면 터짐 if문 추가
                        if (_office_jsonArray != null) {
                            Log.d(MainActivity.TAG,"!@#!$1");
                            for (int n = 0; n < _office_jsonArray.length(); n++) {
                                Log.d(MainActivity.TAG,"!@#!$2");
                                if (n == index) { //자리값에 해당하는 정보만 가져오기
                                    try {
                                        Log.d(MainActivity.TAG,"!@#!$3");
                                        JSONObject object = _office_jsonArray.getJSONObject(n);
                                        object.put("isOccupied", false);
                                        object.put("usedById", "");
                                        object.put("memo", "");
                                        Log.d(MainActivity.TAG, "파싱 후 제이슨 : " + _office_jsonArray);
                                        syncJsonWithSharedPrefHavruta(_office_jsonArray);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            tv_havruta_info.setText("null");
                            //TODO : 반환시 바로 연동이 안되는건 리스너가 붙은 branch가 달라서인가?
                            mFirebaseDatabaseReference.child(ROOM_CHILD).child(whichDay).child(whichRoom).child(time).setValue(new RoomFirebase(false,"",""));

                            initUserHavrutaInfo();
                        }


                    }

                    //TODO : 유저정보 변경하기
                    //TODO : 이미 유저에게 자리정보가 존재한다면, 이전자리의 처리
                    //TODO : 쉐어드 정보 기준으로, occupied true 이면, 색깔변경처리.
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "강의실 반환을 취소했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog = builder.create();
                builder.show();


            }
        });


        et_nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String _nick = et_nick.getText().toString();
                Map<String, ?> entries = userData.getAll();
                Set<String> keys = entries.keySet();
                for (String key : keys) {
                    if (!MainActivity.loginedUid.equals(key)) {
                        String _nickname = userData.getString(key, "").split(",")[2];
                        //중복된 닉네임이 있다면
                        if (_nick.equals(_nickname)) {
                            tv_nickChange.setText("이미 존재하는 닉네임입니다");
                            tv_nickChange.setTextColor(Color.parseColor("#FF0000"));
                            et_nick.requestFocus();
                            isNicknameUnique = false;
                            return;
                        }
                    }
                }
                isNicknameUnique = true;
                tv_nickChange.setText("사용가능한 닉네임입니다");
                tv_nickChange.setTextColor(Color.parseColor("#008000"));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        changeUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("이대로 정보를 수정 하시겠습니까?");
                builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //변경한 비밀번호가 규칙에 맞지 않는경우
                        //규칙 메세지 토스트로 띄움
                        if (!Pattern.matches(SignupActivity.PW_PATTERN, et_pw.getText().toString())) {
                            Log.d(MainActivity.TAG, "비번 규칙에 안맞음");
                            Toast.makeText(getApplicationContext(), "비밀번호는 영문,숫자,특수문자를 포함해서 8자 이상", Toast.LENGTH_SHORT).show();
                            et_pw.requestFocus();
                            return;
                        }
                        //그 다음 닉네임이 유일한 경우에만,
                        Log.d(MainActivity.TAG, "비번 규칙에 맞음");
                        if (isNicknameUnique) {
                            Log.d(MainActivity.TAG, "닉네임 유일함");

                            Toast.makeText(getApplicationContext(), "회원 정보 수정 완료", Toast.LENGTH_SHORT).show();
                            editUserDataPref();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("ID", MainActivity.loginedUid);
                            intent.putExtra("IS_NOT_FIRST_LOGIN", true);
                            startActivity(intent);
                            finish();
                            return;
                        } else {
                            Log.d(MainActivity.TAG, "닉네임 유일하지 않음");
                            Toast.makeText(getApplicationContext(), "다른 닉네임을 입력해 주세", Toast.LENGTH_SHORT).show();

                        }


                    }
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "회원 정보 수정 취소", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog = builder.create();
                builder.show();
            }
        });

    }

    private void editUserDataPref() {
        SharedPreferences.Editor editor = userData.edit();
        String pwChanged = et_pw.getText().toString();
        String nickChanged = et_nick.getText().toString();
        String userDataBefore = userData.getString(MainActivity.loginedUid, "");
        String[] tmp = userDataBefore.split(",");
        Log.d(MainActivity.TAG, Arrays.toString(tmp));
        tmp[1] = pwChanged;
        tmp[2] = nickChanged;
        Log.d(MainActivity.TAG, Arrays.toString(tmp));
        String userDataAfter = TextUtils.join(",", tmp);
        editor.putString(MainActivity.loginedUid, userDataAfter);

        editor.apply();
        Log.d(MainActivity.TAG, userData.getAll().toString());

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

    private void syncJsonWithSharedPref(JSONArray _office6_seatInfo) {
        SharedPreferences.Editor editor = office6_seatInfo.edit();
        editor.putString("office6_seatInfo", _office6_seatInfo.toString());
        editor.apply();
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

    //다른 데랑 다르게 여기서는 또 유저로그인드에서 싯정보 유저데이타에서 싯정보 다 바꾸는걸로 코딩햇네
    //아마도, 유저로그인드와 유저데이타가 메인화면에서 싱크되기때문에 나중엔 두번째거를 없애도 되지 않나?

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

    private void initUserHavrutaInfo() {
        SharedPreferences.Editor editor = userLogined.edit();
        String userInfoBefore = userLogined.getString(MainActivity.loginedUid, "");
        //가져와서, 자리정보값을 업데이트해준다
        String infoarr[] = userInfoBefore.split(",");
        infoarr[6] = "null";
        editor.putString(MainActivity.loginedUid, TextUtils.join(",", infoarr));
        editor.apply();
        mFirebaseDatabaseReference.child(USERLOGINED_CHILD).child(MainActivity.loginedUid).setValue(TextUtils.join(",", infoarr));
        mFirebaseDatabaseReference.child(USERS_CHILD).child(MainActivity.loginedUid).setValue(TextUtils.join(",", infoarr));

        initUserHavrutaInfoInUserData();

    }

    private void initUserHavrutaInfoInUserData() {
        SharedPreferences.Editor editor = userData.edit();
        String userInfoBefore = userData.getString(MainActivity.loginedUid, "");
        String infoarr[] = userInfoBefore.split(",");
        infoarr[6] = "null";
        editor.putString(MainActivity.loginedUid, TextUtils.join(",", infoarr));
        editor.apply();
    }


}
