package com.example.spacenova.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spacenova.R;
import com.example.spacenova.activity.ArticleSpecificationActivity;
import com.example.spacenova.activity.MainActivity;
import com.example.spacenova.data.Article;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int ARTICLE_SPEC_REQUEST = 3000;
    private Context context;
    private ArrayList<Article> itemList;

    public ArticleAdapter(Context activityContext, ArrayList<Article> itemList){
        this.context = activityContext;
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_in_notice, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.img_item.setImageResource(itemList.get(position).getResId());
        itemViewHolder.img_title.setText(itemList.get(position).getArticleTitle());
        //여기서 클릭리스너 달 수 있다.

    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView img_title;

        ItemViewHolder(View view){
            super(view);
            img_item = view.findViewById(R.id.img_item);
            img_title = view.findViewById(R.id.title_item);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ArticleSpecificationActivity.class);
                    getAdapterPosition();
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        Article item = itemList.get(position);
                        intent.putExtra("article_title",item.getArticleTitle());
                        intent.putExtra("article_content",item.getArticleContent());
//                        intent.putExtra("article_position",String.valueOf(position));
                        intent.putExtra("article_position",position);
                        //포지션 값을 넘길때 "article_position",position이면 castexception발생. Integer cannot be cast to String
                        //그래서, 게시물조회 액티비티에서 찍어보면 null이 반환된다
                        Log.d(MainActivity.TAG,"아이템어댑터에서 포지션 : "+position);
                        //액티비티위의 프래그먼트위에 위치한 리사이클러뷰의 아이템이므로, 아래의 컨텍스트의 액티비티는 리사이클러뷰가 위치한 노티스액티비티이다
                        //또한 컨텍스트를 매개변수로 넘길때, getApplicationContext가 아니라, Activity.this로 넘겨주어야 아래에서 에러가 안난다
                        //그러므로 노티스 액티비티에서 온액티비티리저트에서 처리를 해주어야하는 것.
                        //포지션 값을 비롯한 타이틀, 내용을 넘기기위해 클릭시 인텐트를 넘긴다.
//                        context.startActivity(intent);
                        ((Activity)context).startActivityForResult(intent,ARTICLE_SPEC_REQUEST);

                        //이렇게 인텐트를 넘기면
                        //게시물 조회 액티비티가 실행된다
                        //그럼 게시물 조회 액티비티에서, 받은 포지션값으로 아이템리스트를 지우면되는데,
                        //게시물 조회 액티비티에는 아이템리스트가 없다.
                        //그래서 게시물 조회액티비티에서, 노티스 액티비티로 받은 포지션 값을 다시넘겨서,
                        //노티스 액티비티에서 삭제처리하려고한다.
                        //onActivityResult는 클릭이 실행된 노티스액티비티에서 처리해주어야하나?
                    }

                }
            });
        }
    }



}
