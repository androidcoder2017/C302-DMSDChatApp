package com.example.a15056112.c302_dmsdchatapp;

import java.io.Serializable;

/**
 * Created by 15056112 on 15/8/2017.
 */

public class Chat implements Serializable {
    private String id;
    private String messageText;
    private String messageTime;
    private String messageUser;

    public Chat() {

    }

    public Chat(String messageText, String messageTime, String messageUser) {
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.messageUser = messageUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return messageText;
    }

    public void setText(String messageText) {
        this.messageText = messageText;
    }

    public String getTime() {
        return messageTime;
    }

    public void setTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getUser() {
        return messageUser;
    }

    public void setUser(String messageUser) {
        this.messageUser = messageUser;
    }
}
