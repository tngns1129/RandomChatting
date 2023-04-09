package com.familyset.randomchatting.data.message;

import android.util.Log;

import androidx.annotation.NonNull;

import com.familyset.randomchatting.data.message.remote.MessagesRemoteDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MessagesRepository implements MessagesDataSource {
    private static MessagesRepository INSTANCE = null;
    private final MessagesRemoteDataSource mMessagesRemoteDataSource;

    private Map<String, Message> mCachedMessages;
    private boolean mCacheIsDirty = false;

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
        // 캐쉬가 이용가능하다면 캐쉬 데이터를 반환
        if (mCachedMessages != null && !mCacheIsDirty) {
            callBack.onMessagesLoaded(new ArrayList<>(mCachedMessages.values()));
            return;
        }

        if (mCacheIsDirty) {
            getMessagesFromRemoteDataSource(callBack);
        } else {
            // get local storage
        }

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
    public void sendMessage(@NonNull Message message) {
        mMessagesRemoteDataSource.sendMessage(message);

        if (mCachedMessages == null) {
            mCachedMessages = new LinkedHashMap<>();
        }
        mCachedMessages.put(message.getId(), message);
    }

    @Override
    public void stopLoadingMessages() {
        mMessagesRemoteDataSource.stopLoadingMessages();
    }

    private void getMessagesFromRemoteDataSource(@NonNull LoadMessagesCallBack callBack) {
        mMessagesRemoteDataSource.getMessages(new LoadMessagesCallBack() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                refreshCache(messages);
                //refreshLocalDataSource(messages);

                callBack.onMessagesLoaded(new ArrayList<>(mCachedMessages.values()));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void refreshMessages() {
        mCacheIsDirty = true;
    }

    private void refreshCache(List<Message> messages) {
        if (mCachedMessages == null) {
            mCachedMessages = new LinkedHashMap<>();
        }
        mCachedMessages.clear();
        for (Message message : messages) {
            mCachedMessages.put(message.getId(), message);
        }
        mCacheIsDirty = false;
    }
}