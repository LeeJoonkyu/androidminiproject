package com.example.spacenova.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences appData;
    private SharedPreferences userData;
    private EditText idText;
    private EditText pwText;
    private Boolean isAppDataSavedBefore;
    private String id;
    private String pw;

    private FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabaseReference = mfirebaseDatabase.getReference();
    public static final String USERS_CHILD = "users";

    // 구글로그인 result 상수
    private static final int RC_SIGN_IN = 900;
    // 구글api클라이언트
    private GoogleSignInClient googleSignInClient;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    // 구글  로그인 버튼
    private SignInButton buttonGoogle;
    // 페이스북 콜백 매니저
    private CallbackManager callbackManager;
    private LoginButton buttonFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        //이미 만들어져있는, userData 저장소의 정보를 가져온다.

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d(MainActivity.TAG, "앱데이터 로딩 완료");
        //처음엔 false니까 id,pw에 아무것도 없겟지.
        setUI();
        setGoogleLogin();
        setFacebookLogin();


    }

    private void setFacebookLogin() {
        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        // 페이스북 콜백 등록
        callbackManager = CallbackManager.Factory.create();

        buttonFacebook = findViewById(R.id.btn_facebookSignIn);
        //페이스북 사용자의 어떤 정보를 얻어올 것인지?
        //이부분 인자값 잘못쓰면 sorry, 어쩌구 에러 발생
        //아마 FacebookGraphApi와 연관이 있는듯함
        buttonFacebook.setReadPermissions("public_profile", "email");
        buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("qwerqwerqwer", "1");
                //페이스북 로그인에 성공하였다면 해당 액세스토큰을 받아서 정보를 처리한다
                handleFacebookAccessToken(loginResult.getAccessToken());
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    private void handleFacebookAccessToken(final AccessToken token) {
        //페이스북로그인은 여기가 핵심. 액세스토큰을 어떻게 관리하는지
        // 사용자가 정상적으로 로그인한 후 페이스북 로그인 버튼의 onSuccess (콜백)메소드에서 로그인한 사용자의
        // 액세스 토큰을 가져와서 Firebase 사용자 인증 정보로 교환하고,
        // Firebase 사용자 인증 정보를 사용해 Firebase에 인증.

        Log.d("qwerqwerqwer", "a");
        //액세스 토큰에서 겟토큰(실제 토큰을 가져오겠다는 것)한 것을 인증 자격 변수(credential)에 담는다.
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("qwerqwerqwer", "b");
                        if (task.isSuccessful()) {
                            Log.d("qwerqwerqwer", "c");
                            Toast.makeText(LoginActivity.this, "페이스북 로그인 성공", Toast.LENGTH_SHORT).show();
                            Log.d("qwerqwerqwer", "d");
                            //로그인 성공인데, 넘어온 토큰에서 facebook 프로필 정보를 가공해야하므로, 해당 메소드 실행
                            saveSocialLoginInfoToUserDataFb(token);

                        } else {
                            // 로그인 실패
                            Log.d("qwerqwerqwer", "e");

                            Toast.makeText(LoginActivity.this, "페이스북 로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void setGoogleLogin() {

        buttonGoogle = findViewById(R.id.btn_googleSignIn);

        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //구글사인인 클라이언트를 기반으로 인텐트를 생성
                Intent signInIntent = googleSignInClient.getSignInIntent();
                //이 인텐트를 기반으로 구글에 로그인하는 액티비티스타트
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 페이스북 콜백 등록
        // 스타트 액티비티 포 리절트까지 함께 해주는 듯
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        // 구글로그인 리퀘스트코드에 한정하여 실행
        if (requestCode == RC_SIGN_IN) {
            //GoogleSignInAccount 자료형이 들어간 Task 객체에 로그인에 성공한 구글사인인객체를 담는다
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //바로 GoogleSignInAccount account = GoogleSignIn.get~ 이 아닌 것은 설계상 그렇게 되어있을 수도있고,
                //이유는 명확하게 알지 못했다
                //task에서 구글사인인계정을 받아오기 위해서는 task.getResult가 필요한데,
                //getResult의 인자는 ApiException이고 이에 대한 exception처리가 필요
                //아마도, 구글사인인에 성공하면 getResult가 exception없이 성공하는 듯?
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //이렇게 받아온 계정정보를 이제 파이어베이스 인증을 한다
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // 사용자가 구글에 정상적으로 로그인하여 얻어진 구글사인인 acct
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        // GoogleSignInAccount 객체에서 ID 토큰을 가져와서 파이어베이스 인증 자격 변수(credential)에 넣는다

        // 이를 파이어베이스 로그인에 credential을 넣어주는 것을 통해 Firebase 사용자 인증 정보로 교환하고
        // Firebase 사용자 인증 정보를 사용해 Firebase에 인증한다.
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, "구글 로그인 성공", Toast.LENGTH_SHORT).show();
                            // 마침내 구글로그인 성공 + 파이어베이스 인증까지 완료되었음
                            // 다만 받아온 구글사인인 객체에서 필요한 정보를 내 앱에 맞게 커스텀해야한다
                            Intent intent = saveSocialLoginInfoToUserData();
                            startActivity(intent);
                            finish();
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, "구글 로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private Intent saveSocialLoginInfoToUserData() {
        SharedPreferences.Editor editor = userData.edit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        //구글 사인인에서 지난번 로그인한 정보로부터 유저 프로필 정보를 가져오는 부분
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        if (acct != null) {
            //계정 전시 이름으로 이름설정
            String personName = acct.getDisplayName();
            //비밀번호는 임시로 지정
            String personPw = "tmppwforsociallogin!!";
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Log.d(MainActivity.TAG, personName + personGivenName + personFamilyName + personEmail + personId + personPhoto);

            String appendedValue = "";
            editor.putString(personId, personId);
            //"123" : ""
            Log.d(MainActivity.TAG, "0 아이디 " + personId);
            appendedValue += personId;


            appendedValue += "," + personPw;
            //"123"
            editor.putString(personId, appendedValue);
            //"123" : "123"
            Log.d(MainActivity.TAG, "1 비번 " + appendedValue);


            appendedValue = appendedValue + "," + personGivenName;
            //"123,123"
            editor.putString(personId, appendedValue);
            //"123" : "123,123"
            Log.d(MainActivity.TAG, "2 닉네임 " + appendedValue);

            appendedValue = appendedValue + "," + personEmail;
            //"123,123,123"
            editor.putString(personId, appendedValue);
            //"123" : "123,123,123"
            Log.d(MainActivity.TAG, "3 이메일 " + appendedValue);

            appendedValue = appendedValue + "," + "010-0000-0000";
            //"123,123,123,123"
            editor.putString(personId, appendedValue);
            //"123" : "123,123,123,123"
            Log.d(MainActivity.TAG, "4 폰번호 " + appendedValue);

            appendedValue += "," + "null";
            editor.putString(personId, appendedValue);
            Log.d(MainActivity.TAG, "5 자리정보 " + appendedValue);


            appendedValue += "," + "null";
            editor.putString(personId, appendedValue);
            Log.d(MainActivity.TAG, "6 하브루타정보 " + appendedValue);

            appendedValue += "," + "구글계정";
            editor.putString(personId, appendedValue);
            Log.d(MainActivity.TAG, "7 이름 " + appendedValue);


//        editor.putBoolean("SAVE_USER_DATA", true);
            editor.apply();
            mFirebaseDatabaseReference.child(USERS_CHILD).child(personId).setValue(appendedValue);
            intent.putExtra("ID", personId);
        }
        return intent;
        //입력창에 입력된 아이디 비번을 쉐어드에 저장한다

    }

    private void saveSocialLoginInfoToUserDataFb(AccessToken token) {
        final SharedPreferences.Editor editor = userData.edit();
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Log.d(MainActivity.TAG, "????????1");
        //페이스북은 프로필 정보를 조회하려면 GraphApi를 사용하여야한다
        if (token != null) {
            Log.d(MainActivity.TAG, "????????2");
            //토큰을 기반으로 새로운 GraphRequest를 생성, 이를 기반으로 객체를 생성한다.
            //Graphrequest를 생성시에 Graph형태의 JSONObject가 콜백값으로 넘어온다
            GraphRequest request = GraphRequest.newMeRequest(
                    token,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        //가장 핵심은 이 콜백이 완료가 되고 나서야, onCompleted메소드가 실행된다는 것이다
                        //당연한 것처럼 보이지만
                        //코드가 순서대로 읽히는 느낌은 아니고,
                        //아래에서 executeAsync가 완전히 수행되고 나서야
                        //오버라이딩된 이메소드가 실행된다
                        //executAsync가 완료되는 시점에 호출됨
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.d(MainActivity.TAG, "????????3");
                            Log.d(MainActivity.TAG, response.toString());
                            // Application code
                            String fb_email = "";
                            String fb_id = "";
                            String fb_last_name = "";
                            String fb_name = "";
                            try {
                                fb_email = object.getString("email");
                                fb_id = object.getString("id");
                                fb_last_name = object.getString("last_name");
                                fb_name = object.getString("name");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String fb_pw = "tmppwforsociallogin!!";
                            String appendedValue = "";
                            editor.putString(fb_id, fb_id);

                            Log.d(MainActivity.TAG, "0 아이디 " + fb_id);
                            appendedValue += fb_id;


                            appendedValue += "," + fb_pw;
                            //"123"
                            editor.putString(fb_id, appendedValue);
                            //"123" : "123"
                            Log.d(MainActivity.TAG, "1 비번 " + appendedValue);


                            appendedValue = appendedValue + "," + "페북계정" + fb_id;
                            //"123,123"
                            editor.putString(fb_id, appendedValue);
                            //"123" : "123,123"
                            Log.d(MainActivity.TAG, "2 닉네임 " + appendedValue);

                            appendedValue = appendedValue + "," + fb_email;
                            //"123,123,123"
                            editor.putString(fb_id, appendedValue);
                            //"123" : "123,123,123"
                            Log.d(MainActivity.TAG, "3 이메일 " + appendedValue);

                            appendedValue = appendedValue + "," + "010-0000-0000";
                            //"123,123,123,123"
                            editor.putString(fb_id, appendedValue);
                            //"123" : "123,123,123,123"
                            Log.d(MainActivity.TAG, "4 폰번호 " + appendedValue);

                            appendedValue += "," + "null";
                            editor.putString(fb_id, appendedValue);
                            Log.d(MainActivity.TAG, "5 자리정보 " + appendedValue);


                            appendedValue += "," + "null";
                            editor.putString(fb_id, appendedValue);
                            Log.d(MainActivity.TAG, "6 하브루타정보 " + appendedValue);

                            appendedValue += "," + fb_name;
                            editor.putString(fb_id, appendedValue);
                            Log.d(MainActivity.TAG, "7 이름 " + appendedValue);

                            editor.apply();

                            mFirebaseDatabaseReference.child(USERS_CHILD).child(fb_id).setValue(appendedValue);
                            //콜백이 다 완료되었으므로 스타트액티비티를 해준다.
                            //이 액티비티 스타트 지점을 찾는 데서 다소 헤매었다.
                            intent.putExtra("ID", fb_id);
                            startActivity(intent);
                        }

                    });
            Log.d(MainActivity.TAG, "????????4");
            Bundle parameters = new Bundle();
            //번들을 생성하여서,
            //해당 번들의 키값인 fields에 GraphAPI 의 id, name, email, last_name 값을 넣어둔다
            //그래프API에서 가져올 필요한 값만 설정해두면, 셋파라미터에서 해당 스트링에 해당되는 정보를 가져오는 듯?
            parameters.putString("fields", "id,name,email,last_name");
            //이를 바탕으로 파라미터값을 지정하고
            request.setParameters(parameters);
            //비동기로 리퀘스트를 처리한다.
            request.executeAsync();
            Log.d(MainActivity.TAG, "????????5");

        }
        Log.d(MainActivity.TAG, "????????6");
    }


    private void save() {
        SharedPreferences.Editor editor = appData.edit();
        //입력창에 입력된 아이디 비번을 쉐어드에 저장한다
        editor.putString("ID", idText.getText().toString());
        editor.putString("PW", pwText.getText().toString());
        //아무 입력값 없을땐 어떻게 세이브하나?
        editor.putBoolean("SAVE_LOGIN_DATA", true);
        editor.apply();

    }

    private void load() {
        isAppDataSavedBefore = appData.getBoolean("SAVE_LOGIN_DATA", false);
        //저장된 이름 없을경우 기본값
        //이전에 세이브했던 아이디 비번을 가져온다.
        //이를 가장 처음에 실행하는데
        //만약 로그아웃을 하고나면, 앱데이터는 삭제되고 이 액티비티는 온크리에이트부터 다시실행됨
        //그러니까 찌꺼기가 안남으려면, load전에 clear해주면됨
        id = appData.getString("ID", "");
        pw = appData.getString("PW", "");
    }

    private void clear() {
        SharedPreferences.Editor editor = appData.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.TAG, "로그인 액티비티 온디스트로이");
        //피니쉬를 걸엇으니까 무조건 실행겟지.
        //온디스트로이 여도 전역변수 값이 남아있네?
        //전역변수 초기화는 언제되지?
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    private void setUI() {
        //뷰에 존재하는 인풋아이디값을 저장하기 위한 것
        idText = (EditText) findViewById(R.id.login_inputId);
        pwText = (EditText) findViewById(R.id.login_inputPw);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button signupButton = (Button) findViewById(R.id.signupButton);
//        Button findidButton = (Button) findViewById(R.id.findidButton);
//        Button findpwButton = (Button) findViewById(R.id.findpwButton);
        //뷰에 존재하는 로그인 버튼을 버튼 객체에 담음
        //여기에 온클릭리스너를 붙이기 위함


        Intent intent = getIntent();
        if (intent.getBooleanExtra("LOGOUT", false)) {
            clear();
            //로그아웃을 했다면 기존에 존재하는 앱데이터를 삭제한다
            Log.d(MainActivity.TAG, "로그아웃 이후 앱데이터 삭제");
            idText.setText("");
            pwText.setText("");
            //그리고 아이디 비번 입력창 초기화한다.
        } else if (isAppDataSavedBefore) {
            Log.d(MainActivity.TAG, "저장된 앱데이터가 존재함");
            //앱데이터가 존재하면 해당 앱데이터로 셋텍스트 하는 거고
            idText.setText(id);
            pwText.setText(pw);
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent1.putExtra("ID", id);
            intent1.putExtra("PW", pw);
            intent1.putExtra("IS_AUTO_LOGIN", true);
            startActivity(intent1);
            finish(); //액티비티 스택에서 삭제

        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //저장하는건 하는거고
                id = idText.getText().toString();
                pw = pwText.getText().toString();

//                여기는 파베말고 로컬에서만 확인 시
//                if (userData.contains(id)){
//                    //아이디에 해당하는 키값이 있으면
//                    Log.d(MainActivity.TAG,"아이디 존재 검증 성공 - 입력 id : " + id + "쉐어드 정보 : " + userData.getAll());
//                    if (pw.equals(userData.getString(id,"").split(",")[1])){
//                        Log.d(MainActivity.TAG,"비밀 번호 일치 검증 성공 - 입력 pw : " + pw + "쉐어드 정보 : " + userData.getAll());
//                        save();
//                        //올바른 정보여야 저장함.
//                        Log.d(MainActivity.TAG,"저장된 앱데이터가 없어서 앱데이터 세이브 완료");
//                        intent.putExtra("ID",id);
//                        intent.putExtra("PW",pw);
//                        startActivity(intent);
//                        finish(); //액티비티 스택에서 삭제
//
//                    } else {
//                        Log.d(MainActivity.TAG,"비밀 번호 일치 검증 실패 - 입력 pw : " + pw + "쉐어드 정보 : " + userData.getAll());
//                        Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.d(MainActivity.TAG,"아이디 존재 검증 실패 - 입력 id : " + id + "쉐어드 정보 : " + userData.getAll());
//                    Toast.makeText(getApplicationContext(),"존재하지 않는 아이디입니다",Toast.LENGTH_SHORT).show();
//                }
                final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Log.d("????????",""+intent.getStringExtra("ID"));

                mFirebaseDatabaseReference.child(USERS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isIdValid = false;
                        boolean isPwValid = false;
                        Log.d(MainActivity.TAG, "온데이터체인지 언제실행?" + isIdValid + isPwValid);
                        Log.d(MainActivity.TAG, "아이디 : " + id + " 비번 : " + pw);
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String userdataOnFb = childSnapshot.getValue(String.class);
                            Log.d(MainActivity.TAG, childSnapshot.toString());
                            if (id.equals(userdataOnFb.split(",")[0])) {
                                isIdValid = true;
                                Log.d(MainActivity.TAG, "아이디 존재 검증 성공 - 입력 id : " + id + "쉐어드 정보 : " + userData.getAll());
                                if (pw.equals(userdataOnFb.split(",")[1])) {
                                    isPwValid = true;
                                    Log.d(MainActivity.TAG, "비밀 번호 일치 검증 성공 - 입력 pw : " + pw + "쉐어드 정보 : " + userData.getAll());
                                    save();
                                    //올바른 정보여야 저장함.
                                    Log.d(MainActivity.TAG, "저장된 앱데이터가 없어서 앱데이터 세이브 완료");
                                    intent.putExtra("ID", id);
                                    intent.putExtra("PW", pw);
                                    Log.d("????????",""+intent.getStringExtra("ID"));
                                    startActivity(intent);
                                    Log.d("????????",""+intent.getStringExtra("ID"));

                                    finish(); //액티비티 스택에서 삭제
                                }
                            }
                        }
                        if (!isIdValid) {
                            Log.d(MainActivity.TAG, "아이디 존재 검증 실패 - 입력 id : " + id + "쉐어드 정보 : " + userData.getAll());
                            Toast.makeText(getApplicationContext(), "존재하지 않는 아이디입니다", Toast.LENGTH_SHORT).show();
                        }
                        if (isIdValid && !isPwValid) {
                            Log.d(MainActivity.TAG, "비밀 번호 일치 검증 실패 - 입력 pw : " + pw + "쉐어드 정보 : " + userData.getAll());
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                        Log.d(MainActivity.TAG, "온데이터체인지 언제실행?2" + isIdValid + isPwValid);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

//        findidButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), FindidActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        findpwButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), FindpwActivity.class);
//                startActivity(intent);
//            }
//        });
    }

}
