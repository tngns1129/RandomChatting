package com.familyset.randomchatting.data.userThumbnail;

import android.graphics.Bitmap;

import java.io.File;

public class UserThumbnail {
    private String uid;
    private String photoUrl;
    private String nickname;

    public UserThumbnail() {}

    public UserThumbnail(String uid, String photoUrl, String nickname) {
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
