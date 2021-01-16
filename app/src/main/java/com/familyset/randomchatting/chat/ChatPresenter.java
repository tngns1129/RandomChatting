package com.familyset.randomchatting.chat;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesDataSource;
import com.familyset.randomchatting.data.message.MessagesRepository;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsDataSource;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsRepository;
import com.familyset.randomchatting.matching.MatchingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private UserThumbnailsRepository mUserThumbnailsRepository;
    private MessagesRepository mMessagesRepository;
    private boolean mFirstLoad = true;

    public ChatPresenter(@NonNull ChatContract.View view, @NonNull UserThumbnailsRepository userThumbnailsRepository, @NonNull MessagesRepository messagesRepository) {
        mView = view;
        mUserThumbnailsRepository = userThumbnailsRepository;
        mMessagesRepository = messagesRepository;
        mView.setPresenter(this);
    }

    @Override
    public void saveMessage(String uid, String msg) {
        if (!msg.equals("")) {
            createMessage(uid, msg);

            mView.clearEditText();
        }
    }

    @Override
    public void startListening() {
        //loadUserThumbnails();

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

    @Override
    public void loadUserThumbnails() {
        mUserThumbnailsRepository.getUserThumbnails(new UserThumbnailsDataSource.LoadUserThumbnailsCallBack() {
            @Override
            public void onUserThumbnailsLoaded(List<UserThumbnail> userThumbnails) {
                Map<String, UserThumbnail> userThumbnailsToShow = new HashMap<String, UserThumbnail>();

                for (UserThumbnail userThumbnail : userThumbnails) {
                    userThumbnailsToShow.put(userThumbnail.getUid(), userThumbnail);
                }

                if (!mView.isActive()) {
                    return;
                }

                processUserThumbnails(userThumbnailsToShow);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void loadUserThumbnail(String uid, int position) {
        mUserThumbnailsRepository.getUserThumbnail(uid, new UserThumbnailsDataSource.GetUserThumbnailsCallBack() {
            @Override
            public void onUserThumbnailLoaded(UserThumbnail userThumbnail) {
                mView.showUserThumbnail(userThumbnail, position);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void rematching() {
        mView.showMatchingDialog();
    }

    private void processUserThumbnails(Map<String, UserThumbnail> userThumbnails) {
        if (userThumbnails.isEmpty()) {

        } else {
            mView.showUserThumbnails(userThumbnails);
        }
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
