package com.familyset.randomchatting.data.user;


import java.util.List;

public interface UsersDataSource {
    interface LoadUsersCallBack {
    }

    interface GetUsersCallBack {
        void onUserLoaded(User user);
        void onDataNotAvailable();
    }

    void getUser(String uid, GetUsersCallBack callBack);
    void setUser(User user);
}
