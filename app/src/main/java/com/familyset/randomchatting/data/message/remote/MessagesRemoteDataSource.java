package com.familyset.randomchatting.data.message.remote;

import com.familyset.randomchatting.data.message.MessagesDataSource;

public class MessagesRemoteDataSource implements MessagesDataSource {
    private static MessagesRemoteDataSource INSTANCE;

    public static MessagesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessagesRemoteDataSource();
        }

        return INSTANCE;
    }

    private MessagesRemoteDataSource() {}


}
