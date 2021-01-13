package com.familyset.randomchatting.data.message;

import androidx.annotation.NonNull;

import com.familyset.randomchatting.data.message.remote.MessagesRemoteDataSource;

import java.util.List;

public class MessagesRepository implements MessagesDataSource {
    private static MessagesRepository INSTANCE = null;
    private final MessagesRemoteDataSource mMessagesRemoteDataSource;

    private MessagesRepository(@NonNull MessagesRemoteDataSource messagesRemoteDataSource) {
        mMessagesRemoteDataSource = messagesRemoteDataSource;
    }

    public static MessagesRepository getInstance(MessagesRemoteDataSource messagesRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MessagesRepository(messagesRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getMessages(@NonNull LoadMessagesCallBack callBack) {
        getMessagesFromRemoteDataSource(callBack);
    }

    @Override
    public void getMessage(String id, GetMessagesCallBack callBack) {
        mMessagesRemoteDataSource.getMessage(id, new GetMessagesCallBack() {
            @Override
            public void onMessageLoaded(Message message) {
                callBack.onMessageLoaded(message);
            }
        });
    }

    @Override
    public void saveMessage(@NonNull Message message) {
        mMessagesRemoteDataSource.saveMessage(message);
    }

    private void getMessagesFromRemoteDataSource(@NonNull LoadMessagesCallBack callBack) {
        mMessagesRemoteDataSource.getMessages(new LoadMessagesCallBack() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                callBack.onMessagesLoaded(messages);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}