package com.example.spacenova.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacenova.R;
import com.example.spacenova.adapter.ArticleAdapter;
import com.example.spacenova.data.Article;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BoardActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Article> articleList = new ArrayList<>();
    ArrayList<Article> list = new ArrayList<>();
    ArticleAdapter articleAdapter;
    private Button makeItemButton;
    private boolean isThisOnActivityResult = false;
    private Article article;

    public SharedPreferences articleData;
    public SharedPreferences userLogined;
    public SharedPreferences articleCount;

    public int articleTotal;
    public static int staticArticleCount;

    final static int ARTICLE_ADDING_REQUEST = 0;
    final static int ARTICLE_DELETING_REQUEST = 1;
    //    final static int ARTICLE_SPEC_REQUEST = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        articleData = getSharedPreferences("articleData", MODE_PRIVATE);
        articleCount = getSharedPreferences("articleCount", MODE_PRIVATE);
        staticArticleCount = articleCount.getInt("articleCount", 0);
        //멘토님 조언대로 온크리에이트에서 한번 초기화. 대신 초기화 값은 쉐어드에 저장된 값 기반.

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        makeItemButton = findViewById(R.id.makeitem);


        Date today = new Date();
        SimpleDateFormat timeNow = new SimpleDateFormat("a K:mm");
        loadArticle();

        articleAdapter = new ArticleAdapter(BoardActivity.this, articleList);
        //앱컨텍스트가 아니라 액티비티 컨텍스트를 넘겨야 함 아니면 어댑터에서 스타트액티비티 포 리절트가 아됨
        mRecyclerView.setAdapter(articleAdapter);
        makeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //버튼 클릭 시 게시물 작성 액티비티로 전환
                Intent intent = new Intent(getApplicationContext(), ArticleCreationActivity.class);
                startActivityForResult(intent, ARTICLE_ADDING_REQUEST);
                //게시물의 등록을 기다리기 위함. 여기서 작성 단계로 갓다가 온액티비티리저트에서 done.
            }
        });


        //아이템 클릭시 해당 아이템의 포지션 getadapterposition
        //해당 아이템의 제목과 내용을 intent로 넘기기
        //수정 삭제 처리하기

//        String html = "<html><head><title>첫번째 에제입니다.</title></head>"
//                + "<body><h1>테스트</h1><p>간단히 HTML을 파싱해 보는 샘플예제입니다.</p></body></html>";
//        try{
//            String connUrl = "https://cafe.naver.com";
//
//            Document doc = Jsoup.connect(connUrl).get();
//            Log.d(MainActivity.TAG, "네이버카페 : " + doc);
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        Elements title = doc.select("title");
//        Log.d(MainActivity.TAG, "doc : "+doc);
//        Log.d(MainActivity.TAG,"title : "+title);

        new Description().execute();
    }


    private class Description extends AsyncTask<Void, Void, Void> {
        private ProgressBar progressBar = findViewById(R.id.board_progressBar);
        // # Constants used in this example
        final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36";
        final String LOGIN_FORM_URL = "https://nid.naver.com/nidlogin.login?url=https://cafe.naver.com/otodev";
        final String USERNAME = "ljk6463";
        final String PASSWORD = "asfg2262@@";

        public void checkElement(String name, Element elem) {
            if (elem == null) {
                throw new RuntimeException("Unable to find " + name);
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String connUrl = "https://cafe.naver.com/ArticleList.nhn?search.clubid=29412673&search.menuid=2&search.boardtype=L";
                Document doc = Jsoup.connect(connUrl).get();
//                Log.d(MainActivity.TAG,doc.toString());

                Elements mElementDataSize = doc.select("div[class=article-board m-tcol-c] table tbody").select("tr"); //필요한 녀석만 꼬집어서 지정
                Log.d(MainActivity.TAG, "" + mElementDataSize.toString());
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.
                Log.d(MainActivity.TAG, ""+ mElementSize);
                int i=0;
                for(Element elem : mElementDataSize){ //이렇게 요긴한 기능이
                    String my_title = elem.select("td[class=td_article] a").text();
                    String my_link = elem.select("tr div[class=board-list] a").attr("href");
                    //TODO: 공부를 하고 쓰자
                    //TODO : 파베 그냥 통신용으로 냅두고 가져와서 똒같이쓰자.
                    String my_author = elem.select("td[class=p-nick]").text();
                    String my_imgUrl = elem.select("tr div[class=board-list] a").attr("href");
//                    Element rElem = elem.select("dl[class=board-list] dt").next().first();
//                    Element dElem = elem.select("dt[class=board-list]").next().first();
                    //Log.d("test", "test" + mTitle);
                    //ArrayList에 계속 추가한다.
                    if (i%2 == 0){
//                        list.add(new Article(R.drawable.seatnova55_occupied,articleCountUp(), my_title, "https://cafe.naver.com"+my_link+"\n"+my_author,my_link,my_imgUrl));
                        articleList.add(new Article(R.drawable.seatnova55_occupied,articleCountUp(), my_title, "https://cafe.naver.com"+my_link+"\n"+my_author,my_link,my_imgUrl));
                        Log.d(MainActivity.TAG, i + my_title);
                    }
                    i++;
                }

                //추출한 전체 <li> 출력해 보자.
            } catch (IOException e) {
                e.printStackTrace();
            }





            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //ArraList를 인자로 해서 어답터와 연결한다.
            articleAdapter = new ArticleAdapter(BoardActivity.this,articleList);
//            ArticleAdapter myAdapter = new ArticleAdapter(BoardActivity.this, list);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(articleAdapter);
//            mRecyclerView.setAdapter(myAdapter);
            progressBar.setVisibility(View.GONE);
            //TODO : 뷰에 띄우는 거는 이친구들인데 눌럿을때는 연결되도록 해당링크로


        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.TAG, "보드액티비티 온디스트로이");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        isThisOnActivityResult = true;
        super.onActivityResult(requestCode, resultCode, data);
        //일단 글쓰기에 성공한 상황만 코딩
        if (requestCode == ARTICLE_ADDING_REQUEST && resultCode == RESULT_OK) {

            articleData = getSharedPreferences("articleData", MODE_PRIVATE);
            userLogined = getSharedPreferences("userLogined", MODE_PRIVATE);

            String article_title = data.getStringExtra("article_title");
            String article_content = data.getStringExtra("article_content");
            //글의 저자는 닉네임값으로 식별 아이디는 오바
            String author = userLogined.getString(MainActivity.loginedUid, "").split(",")[2];

            Date today = new Date();
            SimpleDateFormat timeNow = new SimpleDateFormat("yyyy-MM-dd");

            article = new Article(R.drawable.seatnova55, articleCountUp(), article_title, article_content, author, timeNow.format(today));
//            Log.d(MainActivity.TAG,"게시물 생성 시 아티클넘버 및 아티클 토탈");

            articleList.add(article);
            articleAdapter.notifyDataSetChanged();
            //뷰상의 처리
            saveArticle();
            //쉐어드 상의 처리
            Toast.makeText(getApplicationContext(), "글쓰기 완료", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
        } else if (requestCode == ArticleAdapter.ARTICLE_SPEC_REQUEST && resultCode == RESULT_OK) {
            if (data.getBooleanExtra("is_article_updated", false)) {
                //게시글 수정 버튼 눌렀을 경우
                int position = data.getIntExtra("article_position", -1);
                Log.d(MainActivity.TAG, "노티스액티비티로 포지션이 잘 넘어왔는지 확인" + position);
                String updated_title = data.getStringExtra("article_title_updated");
                String updated_content = data.getStringExtra("article_content_updated");

                updateArticle(position, updated_title, updated_content);

                articleList.get(position).setArticleTitle(updated_title);
                articleList.get(position).setArticleContent(updated_content);
                articleAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "게시물이 정상적으로 수정되었습니다", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED);
            } else {
                //게시글 삭제 버튼 눌렀을 경우
                //아이템어댑터에서 조회 후 조회액티비티에서 삭제요청이 제대로 이루어진 경우
//                int position =Integer.parseInt(data.getStringExtra("article_position"));
                int position = data.getIntExtra("article_position", -1);
                Log.d(MainActivity.TAG, "노티스액티비티로 포지션이 잘 넘어왔는지 확인" + position);

                deleteArticle(position);

                articleList.remove(position);
                articleAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "게시물이 정상적으로 삭제되었습니다", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED);
            }

        }
    }

    public int articleCountUp() { //게시물 생성될때의 키값으로 스태틱 아티클카운트 사용, 마찬가지로 추가할때도 스태틱 아티클 카운트 사용해야 안겹친다
        SharedPreferences.Editor editor = articleCount.edit();
        editor.putInt("articleCount", ++staticArticleCount);
        editor.apply();
        int articleCounted = articleCount.getInt("articleCount", -1);
        return articleCounted;
    }

    public void syncArticleTotal() {
        articleTotal = articleData.getAll().size();

    }
    //데이터 시간을
    //불리언값을 굳이파일내에 주지말고
    //따로 불리언을 관리하는 저장소를 만드는게 나을것.

    public void saveArticle() {
        syncArticleTotal();
        SharedPreferences.Editor editor = articleData.edit();
        editor.putString(String.valueOf(staticArticleCount), article.getArticleTitle() + "," + article.getArticleContent() + "," + article.getAuthor() + "," + article.getDate());
        Log.d(MainActivity.TAG, staticArticleCount + "번째 게시물 저장 : " + article.getArticleTitle() + "," + article.getArticleContent() + "," + article.getAuthor() + "," + article.getDate());
        editor.apply();
        syncArticleTotal();
    }

    public void loadArticle() {
        String articleInfo;
        Map<String, ?> entries = articleData.getAll();
        Log.d(MainActivity.TAG, "엔트리가 어떻게 들어왓나" + entries.toString());
        Set<String> keys = entries.keySet();
        Log.d(MainActivity.TAG, "키셋은 어떻게 들어왓나" + keys.toString());
        syncArticleTotal();
        for (String key : keys) {
            articleInfo = articleData.getString(String.valueOf(key), "");
            articleList.add(new Article(R.drawable.seatnova55, Integer.parseInt(key), articleInfo.split(",")[0], articleInfo.split(",")[1], articleInfo.split(",")[2], articleInfo.split(",")[3]));
        }
    }

    public void deleteArticle(int position) {
        SharedPreferences.Editor editor = articleData.edit();
        int articleNoForDeletion = articleList.get(position).getArticleNo();
        editor.remove(String.valueOf(articleNoForDeletion));
        editor.apply();
        syncArticleTotal();
        Log.d(MainActivity.TAG, "삭제 후 쉐어드 정보 : " + articleData.getAll());
    }

    public void updateArticle(int position, String updated_title, String updated_content) {
        SharedPreferences.Editor editor = articleData.edit();
        int articleNoForUpdate = articleList.get(position).getArticleNo();
//        Log.d(MainActivity.TAG,"수정할 게시물 번호 : " + articleNoForUpdate);
        String articleInfoBefore = articleData.getString(String.valueOf(articleNoForUpdate), "");
        String[] beforeArr = articleInfoBefore.split(",");
        beforeArr[0] = updated_title;
        beforeArr[1] = updated_content;
        String articleInfoAfter = TextUtils.join(",", beforeArr);
        Log.d(MainActivity.TAG, "수정 후, 조인 후의 스트링 값 : " + articleInfoAfter);
        editor.putString(String.valueOf(articleNoForUpdate), articleInfoAfter);
        editor.apply();
        syncArticleTotal();
        Log.d(MainActivity.TAG, "수정 후 쉐어드 정보 : " + articleData.getAll());


    }


    //어느 생명주기에서 이 메소드가 가동되는지 확인해볼것
    //static으로 하면position을 사용해볼수잇지.
    //예제 적용전에 일단 한번 체크
    //전역과 지역변수


}
