package com.familyset.randomchatting.data.user;

import androidx.annotation.NonNull;

import com.familyset.randomchatting.data.user.remote.UsersRemoteDataSource;
import com.google.firebase.firestore.DocumentReference;

public class UsersRepository implements UsersDataSource {
    private static UsersRepository INSTANCE = null;
    private final UsersRemoteDataSource mUsersRemoteDataSource;

    private UsersRepository (@NonNull UsersRemoteDataSource usersRemoteDataSource) {
        mUsersRemoteDataSource = usersRemoteDataSource;
    }

    public static UsersRepository getInstance(UsersRemoteDataSource usersRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(usersRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getUser(String uid, GetUsersCallBack callBack) {
        mUsersRemoteDataSource.getUser(uid, callBack);
    }

    @Override
    public void setUser(User user) {
        mUsersRemoteDataSource.setUser(user);
    }
}
