package com.familyset.randomchatting.data.message;

import androidx.annotation.NonNull;

import java.util.List;

public interface MessagesDataSource {
    interface LoadMessagesCallBack {
        void onMessagesLoaded(List<Message> messages);
        void onDataNotAvailable();
    }

    interface GetMessagesCallBack {
        void onMessageLoaded(Message message);
    }

    void getMessages(LoadMessagesCallBack callBack);
    void getMessage(@NonNull String id, @NonNull MessagesDataSource.GetMessagesCallBack callBack);
    void saveMessage(@NonNull Message message);
    void stopLoadingMessages();
    void refreshMessages();
}
