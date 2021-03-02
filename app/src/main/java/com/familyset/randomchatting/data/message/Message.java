package com.familyset.randomchatting.data.message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Message {
    private String id;
    private String uid;
    private String msg;
    private Date timestamp;
    private MessagesType type;
    private List<String> readUsers = new ArrayList<>();
    private String fileName;
    private String fileSize;

    public Message() {}

    // use this constructor to create an new message
    public Message(@Nullable String uid, @Nullable String msg, @Nullable MessagesType type, @Nullable FileInfo fileInfo) {
        this(UUID.randomUUID().toString(), uid, msg, Timestamp.now().toDate(), type, fileInfo);
    }

    public Message(@NonNull String id, @Nullable String uid, @Nullable String msg, @Nullable Date timestamp, @Nullable MessagesType type, @Nullable FileInfo fileInfo) {
        this.id = id;
        this.uid = uid;
        this.msg = msg;
        this.timestamp = timestamp;
        this.type = type;
        this.setFileInfo(fileInfo);
    }

    public static class FileInfo {
        private String fileName;
        private String fileSize;

        public FileInfo() {}

        public FileInfo(String fileName, String fileSize) {
            this.fileName = fileName;
            this.fileSize = fileSize;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileSize() {
            return fileSize;
        }
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

    @Exclude
    public String getRealTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(timestamp);
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public MessagesType getTypeAsEnum() {
        return this.type;
    }

    @Exclude
    public void setTypeAsEnum(MessagesType type) {
        this.type = type;
    }

    public String getType() {
        if (type == null) {
            return null;
        } else {
            return this.type.name();
        }
    }

    public void setType(String type) {
        if (type == null) {
            this.type = null;
        } else {
            this.type = MessagesType.valueOf(type);
        }
    }

    public List<String> getReadUsers() {
        return readUsers;
    }

    public void setReadUsers(List<String> readUsers) {
        this.readUsers = readUsers;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Exclude
    public void setFileInfo(FileInfo fileInfo) {
        if (fileInfo == null) {
            setFileName(null);
            setFileSize(null);
        } else {
            setFileName(fileInfo.fileName);
            setFileSize(fileInfo.fileSize);
        }
    }

    @Exclude
    public FileInfo getFileInfo() {
        if (fileName == null | fileSize == null) {
            return null;
        } else {
            return new FileInfo(fileName, fileSize);
        }
    }
}
