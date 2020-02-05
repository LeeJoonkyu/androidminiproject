package com.example.spacenova.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;
import com.example.spacenova.User;
import com.example.spacenova.data.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private Button signupDoneButton;
    private EditText inputId;
    private EditText inputPw;
    private EditText checkInputPw;
    private EditText etName;
    private EditText inputNickName;
    private EditText inputPhoneNum;
    private EditText inputEmail;

    private String id;
    private TextView idRule;
    private Button checkId;
    private String pw;
    private String checkPw;
    private ImageView mark;
    private String name;
    private String nickname;
    private TextView nicknameRule;
    private String phonenum;
    private String email;
    private String seatInfo;
    private String havrutaInfo;

    private boolean isIdCheckedAsValid;
    private boolean isPwDoubleChecked;
    private boolean isNicknameUnique;

    boolean isSignedUp;
    boolean isHome;
    int userNo;
    //액티비티 인스턴스 변수는 onCreate에서 초기화되기보다는 그전에초기화된다. 다른액티비티 갓다가 오면 userNo가 계속 초기화되는 문제
    public static ArrayList<User> userList = new ArrayList<>();
    public SharedPreferences userData;

    public static final String ID_PATTERN = "^[a-zA-Z](?=.*[a-zA-Z]).{5,9}$";
    //7에서 11자까지만 유효 -> 5,9 : 6에서 10자

    public static final String PW_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*?,./\\\\<>|_-[+]=\\`~\\(\\)\\[\\]\\{\\}])[A-Za-z[0-9]!@#$%^&*?,./\\\\<>|_-[+]=\\`~\\(\\)\\[\\]\\{\\}]{8,20}$";
    public static final String PHONE_NUM_PATTERN = "^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
    public static final String NAME_PATTERN = "^[\\u3131-\\u318E\\uAC00-\\uD7A3]*$";

    private FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabaseReference = mfirebaseDatabase.getReference();
    public static final String USERS_CHILD = "users";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        signupDoneButton = (Button) findViewById(R.id.signupDoneButton);
        inputId = (EditText) findViewById(R.id.inputId);
        checkId = (Button) findViewById(R.id.isIdExists);
        idRule = (TextView) findViewById(R.id.idRule);
        inputPw = (EditText) findViewById(R.id.inputPw);
        checkInputPw = (EditText) findViewById(R.id.checkinputPw);
        mark = (ImageView) findViewById(R.id.signup_mark);
        etName = (EditText) findViewById(R.id.signup_et_name);
        inputNickName = (EditText) findViewById(R.id.inputNickname);
        nicknameRule = (TextView) findViewById(R.id.nicknameRule);
        inputPhoneNum = (EditText) findViewById(R.id.inputPhoneNum);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        load();


        //TODO:반드시 중복검사 되어야 가입 완성되도록 불리언 값 줘야함
        checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = inputId.getText().toString();
                if (id.length() == 0) {
                    inputId.requestFocus();
                    idRule.setText("아이디를 입력하세요");
                    idRule.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    //on shared
//                    Map<String, ?> entries = userData.getAll();
//                    Set<String> keys = entries.keySet();
//                    for (String key : keys) {
//                        //중복된 아이디가 있다면
//                        if (id.equals(key)) {
//                            idRule.setText("이미 존재하는 아이디입니다");
//                            idRule.setTextColor(Color.parseColor("#FF0000"));
//                            inputId.requestFocus();
//                            return;
//                        }
//                    }

                    //on firebase

                    mFirebaseDatabaseReference.child(USERS_CHILD).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String userdataOnFb = childSnapshot.getValue(String.class);
                                Log.d(MainActivity.TAG, dataSnapshot.getChildren().toString());
                                Log.d(MainActivity.TAG, childSnapshot.toString());
                                if (id.equals(userdataOnFb.split(",")[0])) {
                                    idRule.setText("이미 존재하는 아이디입니다");
                                    idRule.setTextColor(Color.parseColor("#FF0000"));
                                    inputId.requestFocus();
                                    return;
                                }
                            }
                            //구조를 해쉬맵으로 잡을경우 fail
//                            ArrayList<String> stringArrayList = new ArrayList<>();
//                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                                stringArrayList.add(childSnapshot.getKey());
//                                Log.d("중복확인 차일드스냅샷 겟키", ":"+childSnapshot.getKey());
//                            }
//                            if (stringArrayList.contains(id)) {
//                                idRule.setText("이미 존재하는 아이디입니다");
//                                idRule.setTextColor(Color.parseColor("#FF0000"));
//                                inputId.requestFocus();
//                                return;
//                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                //여기에 왔다는건 0도아니고 이미 존재하는 것도 아님
                //형식 검사
                if (!Pattern.matches(ID_PATTERN, id)) {
                    idRule.setText("유효하지 않은 아이디입니다");
                    idRule.setTextColor(Color.parseColor("#FF0000"));
                    inputId.requestFocus();
                    return;
                } else {
                    idRule.setText("사용가능한 아이디입니다");
                    idRule.setTextColor(Color.parseColor("#008000"));
                    isIdCheckedAsValid = true;
                }


            }
        });

        checkInputPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pw = inputPw.getText().toString();
                checkPw = checkInputPw.getText().toString();
                if (pw.equals(checkPw)) {
                    mark.setImageResource(R.drawable.check_mark);
                    isPwDoubleChecked = true;
                } else {
                    mark.setImageResource(R.drawable.x_mark);
                    isPwDoubleChecked = false;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        inputNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nickname = inputNickName.getText().toString();
//                Map<String, ?> entries = userData.getAll();
//                Set<String> keys = entries.keySet();
//                for (String key : keys) {
//                    Log.d(MainActivity.TAG,key);
//                    Log.d(MainActivity.TAG,userData.getString(key, "").split(",")[2]);
//                    String _nickname = userData.getString(key, "").split(",")[2];
//                    //중복된 닉네임이 있다면
//                    if (nickname.equals(_nickname)) {
//                        nicknameRule.setText("이미 존재하는 닉네임입니다");
//                        nicknameRule.setTextColor(Color.parseColor("#FF0000"));
//                        inputNickName.requestFocus();
//                        isNicknameUnique = false;
//                        return;
//                    }
//                    //TODO : 공백 제거 정규식 만들기
//                }

                mFirebaseDatabaseReference.child(USERS_CHILD).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                            String userdataOnFb = childSnapshot.getValue(String.class);
//                            Log.d(MainActivity.TAG, childSnapshot.toString());
//                            if (nickname.equals(userdataOnFb.split(",")[2])) {
//                                nicknameRule.setText("이미 존재하는 닉네임입니다");
//                                nicknameRule.setTextColor(Color.parseColor("#FF0000"));
//                                inputNickName.requestFocus();
//                                isNicknameUnique = false;
//                                return;
//                            }
//                        }
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            Log.d(MainActivity.TAG,key);
                            String userdataOnFb = childSnapshot.getValue(String.class);
                            Log.d(MainActivity.TAG, childSnapshot.toString());
                            if (nickname.equals(userdataOnFb.split(",")[2])) {
                                nicknameRule.setText("이미 존재하는 닉네임입니다");
                                nicknameRule.setTextColor(Color.parseColor("#FF0000"));
                                inputNickName.requestFocus();
                                isNicknameUnique = false;
                                return;
                            }
                        }
//                        ArrayList<String> stringArrayList = new ArrayList<>();
//                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                            HashMap<String,Object> hashMap = (HashMap<String,Object>)childSnapshot.getValue();
//                            for ( Map.Entry<String, Object> entry : hashMap.entrySet()) {
//                                String key = entry.getKey();
//                                Object val = entry.getValue();
//                                stringArrayList.add(val.toString().split(",")[2]);
//                                Log.d("닉네임?", val.toString().split(",")[2]);
//                                // do something with key and/or tab
//                            }
//                            Log.d("해쉬맵엔트리셋?", ":"+hashMap.entrySet());
//                            Log.d("해쉬맵밸류?", ":"+hashMap.values());
//                            Log.d("해쉬맵키셋?", ":"+hashMap.keySet());
//
//                        }
//                        if (stringArrayList.contains(nickname)) {
//                            nicknameRule.setText("이미 존재하는 닉네임입니다");
//                            nicknameRule.setTextColor(Color.parseColor("#FF0000"));
//                            inputNickName.requestFocus();
//                            isNicknameUnique = false;
//                            return;
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                isNicknameUnique = true;
                nicknameRule.setText("사용가능한 닉네임입니다");
                nicknameRule.setTextColor(Color.parseColor("#008000"));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        signupDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                id = inputId.getText().toString();
                pw = inputPw.getText().toString();
                checkPw = checkInputPw.getText().toString();
                nickname = inputNickName.getText().toString();
                phonenum = inputPhoneNum.getText().toString();
                email = inputEmail.getText().toString();
                seatInfo = "null";
                havrutaInfo = "null";
                name = etName.getText().toString();

                if (!isIdCheckedAsValid) {
                    inputId.requestFocus();
                    Toast.makeText(getApplicationContext(), "아이디 중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isPwDoubleChecked) {
                    checkInputPw.requestFocus();
                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isNicknameUnique) {
                    inputNickName.requestFocus();
                    Toast.makeText(getApplicationContext(), "다른 닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;

                }

                //누락된 값이 없는지 확인
                if (!checkIfInputHasValue()) {
                    return;
                }

                if (!checkIfInputValid()) {
                    return;
                }

//                User user = new User(userNo, id, pw, nickname, phonenum, email, null, null, null);
//                userList.add(user);
                save();

                Log.d(MainActivity.TAG, "현재의 유저 넘버" + userNo);
                Log.d(MainActivity.TAG, "유저리스트의 유저 넘버" + userList.size());
                //온크리에이트에 넣어두니까 당연히, 처음 액티비티 객체 생성이후에는 먹힐 리가 없지. 계속 1
                //메소드를 만들어서 거기서 진행을 한다음 다른데서 꺼내든지, 리턴을 하든지.
                Log.d(MainActivity.TAG, "" + userNo);
                isSignedUp = true;
                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();

                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkIfInputHasValue() {
        if (id.length() == 0) {
            Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            inputId.requestFocus();
            return false;
        } else if (pw.length() == 0) {
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            inputPw.requestFocus();
            return false;
        } else if (checkPw.length() == 0) {
            Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            checkInputPw.requestFocus();
            return false;
        } else if (name.length() == 0) {
            Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return false;
        } else if (nickname.length() == 0) {
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
            inputNickName.requestFocus();
            return false;
        } else if (phonenum.length() == 0) {
            Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            inputPhoneNum.requestFocus();
            return false;
        } else if (email.length() == 0) {
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            inputEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkIfInputValid() {
        //TODO: 만약 이미존재하는 아이디면 idRule.setText()
        //중복확인 버튼에 일임

        if (!Pattern.matches(PW_PATTERN, pw)) {
            Toast.makeText(getApplicationContext(), "유효하지 않은 비밀번호 입니다", Toast.LENGTH_SHORT).show();
            inputPw.requestFocus();
            return false;
        }

        //TODO: 만약 닉네임이 이미 존재한다면 nicknameRule.setText("")
        //성공적이라면 setText("사용가능한 닉네임입니다.")

        if (!Pattern.matches(NAME_PATTERN, name)) {
            Log.d(MainActivity.TAG, name);
            Toast.makeText(getApplicationContext(), "유효하지 않은 이름입니다", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return false;
        }


        if (!Pattern.matches(PHONE_NUM_PATTERN, phonenum)) {
            Toast.makeText(getApplicationContext(), "유효하지 않은 핸드폰 번호 입니다", Toast.LENGTH_SHORT).show();
            inputPhoneNum.requestFocus();
            return false;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "유효하지 않은 이메일 입니다", Toast.LENGTH_SHORT).show();
            inputEmail.requestFocus();
            return false;
        }


        return true;

    }


    public void save() {
        SharedPreferences.Editor editor = userData.edit();
        //입력창에 입력된 아이디 비번을 쉐어드에 저장한다
        String appendedValue = "";
        editor.putString(id, id);
        //"123" : ""
        Log.d(MainActivity.TAG, "0 아이디 " + id);
        appendedValue += id;


        appendedValue += "," + pw;
        //"123"
        editor.putString(id, appendedValue);
        //"123" : "123"
        Log.d(MainActivity.TAG, "1 비번 " + appendedValue);


        appendedValue = appendedValue + "," + nickname;
        //"123,123"
        editor.putString(id, appendedValue);
        //"123" : "123,123"
        Log.d(MainActivity.TAG, "2 닉네임 " + appendedValue);

        appendedValue = appendedValue + "," + email;
        //"123,123,123"
        editor.putString(id, appendedValue);
        //"123" : "123,123,123"
        Log.d(MainActivity.TAG, "3 이메일 " + appendedValue);

        appendedValue = appendedValue + "," + phonenum;
        //"123,123,123,123"
        editor.putString(id, appendedValue);
        //"123" : "123,123,123,123"
        Log.d(MainActivity.TAG, "4 폰번호 " + appendedValue);

        appendedValue += "," + seatInfo;
        editor.putString(id, appendedValue);
        Log.d(MainActivity.TAG, "5 자리정보 " + appendedValue);


        appendedValue += "," + havrutaInfo;
        editor.putString(id, appendedValue);
        Log.d(MainActivity.TAG, "6 하브루타정보 " + appendedValue);

        appendedValue += "," + name;
        editor.putString(id, appendedValue);
        Log.d(MainActivity.TAG, "7 이름 " + appendedValue);


//        editor.putBoolean("SAVE_USER_DATA", true);
        editor.apply();
        mFirebaseDatabaseReference.child(USERS_CHILD).child(id).setValue(appendedValue);

    }

    public void load() {
        //아무것도 없을때, 초기화 하기
        //두번째로드부터는 빈값이 아니라 다른게 들어오겟지?
        id = userData.getString(id, "");
        pw = userData.getString(id, "");
        nickname = userData.getString(id, "");
        phonenum = userData.getString(id, "");
        email = userData.getString(id, "");
        seatInfo = userData.getString(id, "");
        havrutaInfo = userData.getString(id, "");
        name = userData.getString(id, "");

//        isUserDataSavedBefore = userData.getBoolean("SAVE_USER_DATA", false);
//        if (!isUserDataSavedBefore) {
//            //처음에 아무것도 저장된 적 없을때 초기화
//            id = userData.getString(id, "");
//            pw = userData.getString(id, "");
//            nickname = userData.getString(id, "");
//            phonenum = userData.getString(id, "");
//            email = userData.getString(id, "");
//            seatInfo = userData.getString(id, "");
//            havrutaInfo = userData.getString(id, "");
//        }
        //저장된 이름 없을경우 기본값
        //이전에 세이브했던 아이디 비번을 가져온다.
        //이를 가장 처음에 실행하는데
        //만약 로그아웃을 하고나면, 앱데이터는 삭제되고 이 액티비티는 온크리에이트부터 다시실행됨
        //그러니까 찌꺼기가 안남으려면, load전에 clear해주면됨

    }

    private void clear() {
        SharedPreferences.Editor editor = userData.edit();
        editor.clear();
        editor.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        isHome = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //resume 에서 스타트액티비티 아래에 위치할때보다 현저히 늦게 뜬다
        if (isSignedUp == false && isHome == false) {
            Toast.makeText(getApplicationContext(), "가입이 취소되었습니다", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onDestroy();
    }
}
