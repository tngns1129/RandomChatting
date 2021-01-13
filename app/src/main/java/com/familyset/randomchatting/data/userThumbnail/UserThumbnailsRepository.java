package com.familyset.randomchatting.data.userThumbnail;

import androidx.annotation.NonNull;

import com.familyset.randomchatting.data.userThumbnail.remote.UserThumbnailsRemoteDataSource;

public class UserThumbnailsRepository implements UserThumbnailsDataSource {
    private static UserThumbnailsRepository INSTANCE = null;
    private final UserThumbnailsRemoteDataSource mUsersThumbnailsRemoteDataSource;

    private UserThumbnailsRepository(@NonNull UserThumbnailsRemoteDataSource usersThumbnailsRemoteDataSource) {
        mUsersThumbnailsRemoteDataSource = usersThumbnailsRemoteDataSource;
    }

    public static UserThumbnailsRepository getInstance(UserThumbnailsRemoteDataSource usersThumbnailsRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UserThumbnailsRepository(usersThumbnailsRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getUserThumbnails(LoadUserThumbnailsCallBack callBack) {
        mUsersThumbnailsRemoteDataSource.getUserThumbnails(callBack);
    }
}