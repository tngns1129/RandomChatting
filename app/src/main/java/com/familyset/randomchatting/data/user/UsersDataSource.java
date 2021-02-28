package com.familyset.randomchatting.data.user;


import java.util.List;

public interface UsersDataSource {
    interface LoadUsersCallBack {
    }

    interface GetUsersCallBack {
        void onUserLoaded(User user);
        void onDataNotAvailable();
    }

    interface SetUsersCallBack {
        void onUserUpLoaded();
        void onUserNotUpLoaded();
    }

    void getUser(String uid, GetUsersCallBack callBack);
    void setUser(User user, SetUsersCallBack callBack);
}
