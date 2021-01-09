package com.familyset.randomchatting.data.message;

public interface MessagesDataSource {
    interface LoadMessagesCallBack {

    }

    interface GetMessagesCallBack {

    }

    void getMessages();
}
