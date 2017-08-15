package com.example.a15056112.c302_dmsdchatapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 15056112 on 15/8/2017.
 */

public class ChatAdapter extends ArrayAdapter<Chat> {

    private TextView tvNames, tvTime, tvMessage;
    private Context context;
    private int resource;
    private ArrayList<Chat> chatList;

    public ChatAdapter(Context context, int resource, ArrayList<Chat> alChat) {
        super(context, resource, alChat);
        this.context = context;
        this.resource = resource;
        this.chatList = alChat;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.row, parent, false);

        tvNames = (TextView)view.findViewById(R.id.tvNames);
        tvTime = (TextView)view.findViewById(R.id.tvTime);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);

        Chat chat = chatList.get(position);

        tvNames.setText(chat.getUser());
        tvMessage.setText(chat.getText());
        tvTime.setText(chat.getTime());

        return view;
    }

}
