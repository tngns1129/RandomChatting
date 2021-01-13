package com.familyset.randomchatting.data.userThumbnail;

public class UserThumbnail {
    private String uid;
    private String nickname;

    public UserThumbnail() {}

    public UserThumbnail(String uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
