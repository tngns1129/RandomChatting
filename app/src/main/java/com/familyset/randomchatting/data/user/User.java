package com.familyset.randomchatting.data.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.google.firebase.firestore.Exclude;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(
                in.readString(),
                in.readString(),
                in.readString());
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String uid;
    private String nickname;
    private String photoUrl;

    public User() {}

    public User(String uid, String nickname, String photoUrl) {
        this.uid = uid;
        this.nickname = nickname;
        this.photoUrl = photoUrl;
    }

    @Exclude
    public static User createNewUser(String uid) {
        //return new User(uid, "", RandomName.getName());
        return new User(uid, "new_user_" + uid.substring(0, 3),"" );
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

    @Exclude
    public UserThumbnail getUserThumbnail() {
        UserThumbnail userThumbnail = new UserThumbnail(uid, photoUrl, nickname);

        return userThumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(photoUrl);
        dest.writeString(nickname);
    }
}
