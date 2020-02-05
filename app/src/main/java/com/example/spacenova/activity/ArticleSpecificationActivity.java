package com.example.spacenova.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacenova.R;

public class ArticleSpecificationActivity extends AppCompatActivity {

    private EditText article_title;
    private EditText article_content;
    private Button article_update;
    private Button article_delete;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_specification_layout);

        article_title = (EditText) findViewById(R.id.article_spec_title);
        article_content = (EditText) findViewById(R.id.article_spec_content);
        article_update = (Button) findViewById(R.id.update_article);
        article_delete = (Button) findViewById(R.id.delete_article);

        Intent intent = getIntent();
        article_title.setText(intent.getStringExtra("article_title"));
        article_content.setText(intent.getStringExtra("article_content"));
//        position = Integer.parseInt(intent.getStringExtra("article_position"));
        position = intent.getIntExtra("article_position",-1);
        Log.d(MainActivity.TAG,"아이템어댑터에서 포지션이 잘 넘어왓는지 확인" + position);
//        final int position = intent.getIntExtra("article_position",0);

        //why not getIntExtra?

        article_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleSpecificationActivity.this);
                builder.setMessage("게시물을 수정하시겠어요?");
                builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent1 = new Intent(getApplicationContext(), BoardActivity.class);
//                        intent1.putExtra("article_position", String.valueOf(position));
                        intent1.putExtra("article_position",position);
                        Log.d(MainActivity.TAG,"노티스액티비티로 포지션이 잘 넘어가는지 확인" + position);

                        intent1.putExtra("article_title_updated", article_title.getText().toString());
                        intent1.putExtra("article_content_updated", article_content.getText().toString());
                        intent1.putExtra("is_article_updated", true);
                        setResult(RESULT_OK, intent1);
                        //이거로 인텐트도 넘길수있고 startactivty(intent)할필요없고 원래 스타트액티비티포리저트 실행된 액티비티로 넘어갈수잇다.
                        finish();
                        //여기서 노티스액티비티로 넘어가서
                        //거기서 삭제해준다

                    }
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        setResult(RESULT_CANCELED);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        article_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleSpecificationActivity.this);
                builder.setMessage("게시물을 삭제하시겠어요?");
                builder.setCancelable(false).setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent1 = new Intent(getApplicationContext(), BoardActivity.class);
//                        startActivity(intent1);
                        //여기가 문제엿다. 온크리에이트를 불러와버림
                        intent1.putExtra("article_position",position);
                        Log.d(MainActivity.TAG,"노티스액티비티로 포지션이 잘 넘어가는지 확인" + position);
                        setResult(RESULT_OK, intent1);
                        //이거로 인텐트도 넘길수있고 startactivty(intent)할필요없고 원래 스타트액티비티포리저트 실행된 액티비티로 넘어갈수잇다.
                        finish();
                        //여기서 노티스액티비티로 넘어가서
                        //거기서 삭제해준다

                    }
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        setResult(RESULT_CANCELED);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }


//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ARTICLE_DELETING_REQUEST && resultCode==RESULT_OK){
//
//        }
//
//    }
}
