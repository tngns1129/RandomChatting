package com.familyset.randomchatting.data.userThumbnail;

import java.util.List;

public interface UserThumbnailsDataSource {

    interface LoadUserThumbnailsCallBack {
        void onUserThumbnailsLoaded(List<UserThumbnail> userThumbnails);
        void onDataNotAvailable();
    }

    interface GetUserThumbnailsCallBack {
        void onUserThumbnailLoaded(UserThumbnail userThumbnail);
        void onDataNotAvailable();
    }

    void getUserThumbnails(LoadUserThumbnailsCallBack callBack);
    void getUserThumbnail(String uid, GetUserThumbnailsCallBack callBack);
    void stopLoadingUserThumbnails();
}
