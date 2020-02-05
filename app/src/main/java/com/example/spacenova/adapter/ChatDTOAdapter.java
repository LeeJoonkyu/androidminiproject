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

import com.example.spacenova.R;
import com.example.spacenova.activity.MainActivity;
import com.example.spacenova.data.ChatDTO;

import java.util.ArrayList;

public class ChatDTOAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //cannot resolve symbol ItemViewHolder
    //따로 아이템 뷰홀더 클래스를 만들어줘야함
    private Context context;
    private ArrayList<ChatDTO> chatDTOArrayList = new ArrayList<>();

    public ChatDTOAdapter(Context applicationContext, ArrayList<ChatDTO> chatDTOArrayList){
        this.context = applicationContext;
        addItems(chatDTOArrayList);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Layout Inflating 은 액티비티만 가능하고, 어댑터클래스는 불가능함 앱 컨텍스트에서 인플레이터 추출
        View view;
        if (viewType==0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_chat_item,parent,false);
            return new ChatDTOAdapter.MyItemViewHolder(view);
        } else if (viewType==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.others_chat_item,parent,false);
            return new ChatDTOAdapter.OthersItemViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.others_chat_item,parent,false);
        return new ChatDTOAdapter.OthersItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        ChatDTO chatDTO = chatDTOArrayList.get(position);
        Log.d(MainActivity.TAG,"겟아이템뷰타입"+ chatDTO.getWho());
        if (chatDTO.getWho()==0){
            return 0;
        } else{
            return 1;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatDTO chatDTO = chatDTOArrayList.get(position);

        if (chatDTO.getWho()==0){
            ChatAdapter.MyItemViewHolder myItemViewHolder = (ChatAdapter.MyItemViewHolder)holder;
            myItemViewHolder.my_msgtime.setText(chatDTO.getDate().toString());
            myItemViewHolder.my_msg.setText(chatDTO.getMessage());

        } else {
            ChatAdapter.OthersItemViewHolder othersItemViewHolder = (ChatAdapter.OthersItemViewHolder)holder;
            othersItemViewHolder.nickname.setText(chatDTO.getUserNickname());
            othersItemViewHolder.img.setImageResource(R.drawable.ic_launcher_foreground);
            othersItemViewHolder.others_msgtime.setText(chatDTO.getDate().toString());
            othersItemViewHolder.others_msg.setText(chatDTO.getMessage());

        }

    }

    @Override
    public int getItemCount() {
        return chatDTOArrayList.size();
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

    public void addItem(ChatDTO chatDTO){
        chatDTOArrayList.add(chatDTO);
    }

    public void addItems(ArrayList<ChatDTO> chatDTOList){
        this.chatDTOArrayList = chatDTOList;
    }


}
