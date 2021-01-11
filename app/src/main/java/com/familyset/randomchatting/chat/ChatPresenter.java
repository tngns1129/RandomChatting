package com.familyset.randomchatting.chat;

import androidx.annotation.NonNull;

import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesDataSource;
import com.familyset.randomchatting.data.message.MessagesRepository;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private MessagesRepository mMessagesRepository;
    private boolean mFirstLoad = true;

    public ChatPresenter(@NonNull ChatContract.View view, @NonNull MessagesRepository messagesRepository) {
        mView = view;
        mMessagesRepository = messagesRepository;

        mView.setPresenter(this);
    }

    @Override
    public void saveMessage(String uid, String msg) {
        createMessage(uid, msg);
    }

    @Override
    public void startListening() {
        loadMessages();
    }

    @Override
    public void stopListening() {

    }

    @Override
    public void loadMessages() {
        if (mFirstLoad) {
            mFirstLoad = false;
        }

        mMessagesRepository.getMessages(new MessagesDataSource.LoadMessagesCallBack() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                List<Message> messagesToShow = new ArrayList<Message>();

                for (Message message : messages) {
                    messagesToShow.add(message);
                }

                if (!mView.isActive()) {
                    return;
                }

                processMessages(messagesToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void processMessages(List<Message> messages) {
        if (messages.isEmpty()) {

        } else {
            // Show the list of messages
            mView.showMessages(messages);
        }
    }

    private void createMessage(String uid, String msg) {
        Message newMessage = new Message(uid, msg);
        if (newMessage == null) {
            //mView.showEmptyMessageError();
        } else {
            mMessagesRepository.saveMessage(newMessage);
        }
    }
}
