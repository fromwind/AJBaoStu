package com.udit.aijiabao.entitys;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/5/18.
 */
@Table(name="MessageEntity",onCreated = "CREATE UNIQUE INDEX index_name ON MessageEntity(notify_id)")
public class MessageEntity {

    @Column(name = "content")
    private String content;
    @Column(name = "msg_type")
    private String msg_type;
    @Column(name = "created_at")
    private String created_at;
    @Column(name = "notify_id",isId = true,autoGen = false)
    private int notify_id;
    @Column(name = "read")
    private boolean read = false;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name="notify_time")
    private String notify_time;
    @Column(name="user")
    private String user;


    public MessageEntity(){}
    public MessageEntity(String content, String msg_type, String created_at, int notify_id,boolean read,boolean deleted, String notify_time,String user) {
        this.content = content;
        this.msg_type = msg_type;
        this.created_at = created_at;
        this.notify_id = notify_id;
        this.read=read;
        this.deleted=deleted;
        this.notify_time=notify_time;
        this.user=user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(int notify_id) {
        this.notify_id = notify_id;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    @Override
    public String toString() {
        return content+","+msg_type+","+created_at+","+notify_id+","+read+","+deleted+","+notify_time;
    }
}
