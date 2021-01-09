package com.familyset.randomchatting.data.randomRoom;

import javax.annotation.Nullable;

public interface RandomRoomsDataSource {

    interface SearchCallBack {
        void onSearchFinished(@Nullable String rid);
    }

    interface CreateCallBack {
        void onCreateFinished();
        void onMatchFinished(String rid);
    }

    void searchEmptyRoom(String uid, SearchCallBack callBack);
    void createRoom(String uid, CreateCallBack callBack);
}
