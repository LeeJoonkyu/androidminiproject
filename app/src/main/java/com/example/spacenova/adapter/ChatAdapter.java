package com.example.spacenova.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacenova.activity.MainActivity;
import com.example.spacenova.data.Chat;
import com.example.spacenova.R;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //cannot resolve symbol ItemViewHolder
    //따로 아이템 뷰홀더 클래스를 만들어줘야함
    private Context context;
    private ArrayList<Chat> chatList = new ArrayList<>();
//    private int layoutid;
//    private LayoutInflater inflater;
//    private String id;


    public ChatAdapter(Context applicationContext, ArrayList<Chat> chatList){
        Log.d(MainActivity.TAG,"채팅 어댑터 Construct");
        this.context = applicationContext;
//        this.layoutid = layoutid;
//        this.chatList = chatList;
//        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.id = id;
        addItems(chatList);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Layout Inflating 은 액티비티만 가능하고, 어댑터클래스는 불가능함 앱 컨텍스트에서 인플레이터 추출
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View itemView = inflater.inflate(R.layout.my_chat_item,parent,false);
        View view;
        if (viewType==0){
            Log.d(MainActivity.TAG,"온크리에이트뷰홀더 : 나(0)인경우");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_chat_item,parent,false);
            return new MyItemViewHolder(view);
        } else if (viewType==1){
            Log.d(MainActivity.TAG,"온크리에이트뷰홀더 : 상대방(1)인경우");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.others_chat_item,parent,false);
            return new OthersItemViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.others_chat_item,parent,false);
        return new OthersItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = chatList.get(position);
        Log.d(MainActivity.TAG,"겟아이템뷰타입"+ chat.getWho());
        if (chat.getWho()==0){
            return 0;
        } else{
            return 1;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //final 선언을 해야하는 이유?
        Chat chat = chatList.get(position);

        Log.d(MainActivity.TAG,"온바인드뷰홀더 : " + position + " " + chat.getChatContent());
        Log.d(MainActivity.TAG,"온바인드뷰홀더 : " + chat.getTime());
        if (chat.getWho()==0){
            MyItemViewHolder myItemViewHolder = (MyItemViewHolder)holder;
            myItemViewHolder.my_msgtime.setText(chat.getTime());
            myItemViewHolder.my_msg.setText(chat.getChatContent());

        } else {
            OthersItemViewHolder othersItemViewHolder = (OthersItemViewHolder)holder;
            othersItemViewHolder.nickname.setText(chat.getNickname());
            othersItemViewHolder.img.setImageResource(R.drawable.ic_launcher_foreground);
            othersItemViewHolder.others_msgtime.setText(chat.getTime());
            othersItemViewHolder.others_msg.setText(chat.getChatContent());

        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class OthersItemViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView nickname;
        TextView others_msg;
        TextView others_msgtime;

        OthersItemViewHolder(View itemView){
            //View 하나를 인자로 받아서,
            //recyclerview.widget.RecyclerView.ViewHolder엔 디폴트 생성자가 없다.(super를 쓰라는 설계인듯)
            super(itemView);
            img = itemView.findViewById(R.id.chat_profile_img);
            nickname = itemView.findViewById(R.id.chat_nickname);
            others_msg = itemView.findViewById(R.id.others_chat_view);
            others_msgtime = itemView.findViewById(R.id.others_chat_time);
        }



    }

    public class MyItemViewHolder extends RecyclerView.ViewHolder{
        TextView my_msg;
        TextView my_msgtime;

        MyItemViewHolder(View itemView){
            //View 하나를 인자로 받아서,
            //recyclerview.widget.RecyclerView.ViewHolder엔 디폴트 생성자가 없다.(super를 쓰라는 설계인듯)
            super(itemView);
            my_msg = itemView.findViewById(R.id.my_chat_view);
            my_msgtime = itemView.findViewById(R.id.my_chat_time);

        }

    }

    public void addItem(Chat chat){
        chatList.add(chat);
//        Log.d(TAG,"애드아이템 : " + chat.getChatContent());
    }

    public void addItems(ArrayList<Chat> chatList){
        this.chatList = chatList;
    }

    public void clear(){
        chatList.clear();
    }



}
