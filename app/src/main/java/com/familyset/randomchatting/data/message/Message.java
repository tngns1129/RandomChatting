package com.familyset.randomchatting.data.message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.UUID;

public class Message {
    private String id;
    private String uid;
    private String msg;
    private Date timestamp;

    public Message() {}

    // use this constructor to create an new message
    public Message(@Nullable String uid, @Nullable String msg) {
        this(UUID.randomUUID().toString(), uid, msg, Timestamp.now().toDate());
    }

    public Message(@NonNull String id, @Nullable String uid, @Nullable String msg, @Nullable Date timestamp) {
        this.id = id;
        this.uid = uid;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
