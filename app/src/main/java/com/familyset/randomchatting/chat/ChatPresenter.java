package com.familyset.randomchatting.chat;

import com.familyset.randomchatting.data.message.MessagesRepository;

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private MessagesRepository mMessagesRepository;

    public ChatPresenter(ChatContract.View view, MessagesRepository messagesRepository) {

    }

    @Override
    public void sendMsg(String msg) {

    }
}
