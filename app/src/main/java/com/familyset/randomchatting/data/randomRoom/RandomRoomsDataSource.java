package com.familyset.randomchatting.data.randomRoom;

import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;

import javax.annotation.Nullable;

public interface RandomRoomsDataSource {

    interface SearchCallBack {
        void onSearchFinished(@Nullable String rid);
    }

    interface CreateCallBack {
        void onCreateFinished();
        void onMatchFinished(String rid);
    }

    void searchEmptyRoom(UserThumbnail userThumbnail, SearchCallBack callBack);
    void createRoom(UserThumbnail userThumbnail, CreateCallBack callBack);
    void quitRoom(String uid, String rid);
}
