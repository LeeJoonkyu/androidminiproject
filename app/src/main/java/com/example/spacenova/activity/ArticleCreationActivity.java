package com.example.spacenova.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;
import com.example.spacenova.data.Article;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticleCreationActivity extends AppCompatActivity {

    private EditText article_title;
    private EditText article_content;
    private Button article_submit;
//    public SharedPreferences articleData;
//    public SharedPreferences userLogined;

    private String title;
    private String content;
    private String author;
    private Article article;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);

//        articleData = getSharedPreferences("articleData", MODE_PRIVATE);
//        userLogined = getSharedPreferences("userLogined", MODE_PRIVATE);

//        author = userLogined.getAll().toString().split("=")[0].substring(1);

        article_title = (EditText) findViewById(R.id.article_title);
        article_content = (EditText) findViewById(R.id.article_spec_content);
        article_submit = (Button) findViewById(R.id.writearticle);
        //레이아웃 내의 요소들 객체화

        article_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //제출 시에 게시물 번호 하나씩 증가
                //증가된 게시물 번호가 키
                //밸류는 이제 나머지.
                title = article_title.getText().toString();
                content = article_content.getText().toString();
//
//                Date today = new Date();
//                SimpleDateFormat timeNow = new SimpleDateFormat("yyyy-MM-dd");
//                article = new Article(R.drawable.seatnova55, 0, title, content, author,timeNow.format(today));
//                //버튼 눌러서 제출할때 게시글 번호 증가

                Intent intent = new Intent();
                intent.putExtra("article_title", title);
                intent.putExtra("article_content", content);
                //일단 성공한 결과값만 돌려주자
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ArticleCreationActivity.this);
            builder.setMessage("글쓰기를 종료하시겠어요?");
            builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
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
}
