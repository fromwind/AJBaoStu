package com.udit.aijiabao.entitys;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class RespMessageInfo {

    private String title;

    private List<MessageEntity>messageList;

    public RespMessageInfo(String title, List<MessageEntity> messageList) {
        this.title = title;
        this.messageList = messageList;
    }

    public RespMessageInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MessageEntity> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageEntity> messageList) {
        this.messageList = messageList;
    }
}
