package com.familyset.randomchatting.chat;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesDataSource;
import com.familyset.randomchatting.data.message.MessagesRepository;
import com.familyset.randomchatting.data.user.User;
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
    private String mUid;
    private List<Message> mMessages;
    private Map<String, UserThumbnail> mUserThumbnails;

    public ChatPresenter(String uid, @NonNull ChatContract.View view, @NonNull UserThumbnailsRepository userThumbnailsRepository, @NonNull MessagesRepository messagesRepository) {
        mUid = uid;
        mView = view;
        mUserThumbnailsRepository = userThumbnailsRepository;
        mMessagesRepository = messagesRepository;

        mMessages = new ArrayList<>();
        mUserThumbnails = new HashMap<>();

        mView.setPresenter(this);
    }

    @Override
    public void saveMessage(String msg) {
        if (!msg.equals("")) {
            createMessage(mUid, msg);

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
        stopLoadingUserThumbnails();

        stopLoadingMessages();
    }

    @Override
    public void loadMessages() {
        loadMessages(mFirstLoad);
        mFirstLoad = false;
    }

    private void loadMessages(boolean forceUpdate) {
        if (forceUpdate) {
            mMessagesRepository.refreshMessages();
        }

        mMessagesRepository.getMessages(new MessagesDataSource.LoadMessagesCallBack() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                //List<Message> messagesToShow = new ArrayList<Message>();
                mMessages = new ArrayList<>();

                for (Message message : messages) {
                    //messagesToShow.add(message);
                    mMessages.add(message);
                }

                if (!mView.isActive()) {
                    return;
                }

                //processMessages(messagesToShow);
                processMessages(mMessages);
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
    public void getUserThumbnail(String uid, int position) {
        mUserThumbnailsRepository.getUserThumbnail(uid, new UserThumbnailsDataSource.GetUserThumbnailsCallBack() {
            @Override
            public void onUserThumbnailLoaded(UserThumbnail userThumbnail) {
                 mUserThumbnails.put(userThumbnail.getUid(), userThumbnail);

                 if (!mView.isActive()) {
                     return;
                 }

                mView.showUserThumbnail(position);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void rematching() {
        mView.showMatchingFragment();
    }

    @Override
    public void onBindViewHolder(int position, ChatContract.RecyclerRowView holder) {
        Message message = mMessages.get(position);

        String uid = message.getUid();

        if (!uid.equals(mUid)) {
            if (mUserThumbnails.get(uid) != null) {
                holder.setUserThumbnail(mUserThumbnails.get(uid));
            } else {
                //getUserThumbnail(uid, position);
            }
        }

        holder.setMsg(message.getMsg());
    }

    @Override
    public boolean getItemViewType(int position) {
        Message message = mMessages.get(position);

        if (message.getUid().equals(mUid)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    private void stopLoadingUserThumbnails() {
        mUserThumbnailsRepository.stopLoadingUserThumbnails();
    }

    private void stopLoadingMessages() {
        mMessagesRepository.stopLoadingMessages();
    }

    private void processUserThumbnails(Map<String, UserThumbnail> userThumbnails) {
        if (userThumbnails.isEmpty()) {

        } else {
            mView.showUserThumbnails();
        }
    }

    private void processMessages(List<Message> messages) {
        if (messages.isEmpty()) {

        } else {
            // Show the list of messages
            mView.showMessages();
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
