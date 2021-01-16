package com.familyset.randomchatting.data.userThumbnail;

import android.graphics.Bitmap;

import java.io.File;

public class UserThumbnail {
    private String uid;
    private File photo;
    private String photoUrl;
    private String nickname;

    public UserThumbnail() {}

    public UserThumbnail(String uid, File photo, String photoUrl, String nickname) {
        this.uid = uid;
        this.photo = photo;
        this.photoUrl = photoUrl;
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
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
