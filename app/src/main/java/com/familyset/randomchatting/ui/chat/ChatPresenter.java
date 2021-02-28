package com.familyset.randomchatting.ui.chat;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.familyset.randomchatting.data.file.FileDataSource;
import com.familyset.randomchatting.data.file.FilesRepository;
import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesDataSource;
import com.familyset.randomchatting.data.message.MessagesRepository;
import com.familyset.randomchatting.data.message.MessagesType;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsDataSource;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private UserThumbnailsRepository mUserThumbnailsRepository;
    private MessagesRepository mMessagesRepository;
    private FilesRepository mFilesRepository;

    private boolean mFirstLoad = true;
    private String mUid;
    private String mRid;
    private List<Message> mMessages;
    private Map<String, UserThumbnail> mUserThumbnails;

    public ChatPresenter(String uid, String rid, @NonNull ChatContract.View view
            , @NonNull UserThumbnailsRepository userThumbnailsRepository
            , @NonNull MessagesRepository messagesRepository
            , @NonNull FilesRepository filesRepository) {
        mUid = uid;
        mRid = rid;
        mView = view;
        mUserThumbnailsRepository = userThumbnailsRepository;
        mMessagesRepository = messagesRepository;
        mFilesRepository = filesRepository;

        mMessages = new ArrayList<>();
        mUserThumbnails = new HashMap<>();

        mView.setPresenter(this);
    }

    @Override
    public void sendMessage(String msg, MessagesType type, Message.FileInfo fileInfo) {
        if (!msg.equals("")) {
            createMessage(mUid, msg, type, fileInfo);

            mView.clearEditText();
        }
    }

    @Override
    public void startListening() {
        loadUserThumbnails();

        loadMessages();
    }

    @Override
    public void stopListening() {
        //stopLoadingUserThumbnails();

        //stopLoadingMessages();
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
                //Map<String, UserThumbnail> userThumbnailsToShow = new HashMap<String, UserThumbnail>();
                mUserThumbnails = new HashMap<>();

                for (UserThumbnail userThumbnail : userThumbnails) {
                    //userThumbnailsToShow.put(userThumbnail.getUid(), userThumbnail);
                    mUserThumbnails.put(userThumbnail.getUid(), userThumbnail);
                }

                if (!mView.isActive()) {
                    return;
                }

                //processUserThumbnails(userThumbnailsToShow);
                processUserThumbnails(mUserThumbnails);
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
    public void openExpandedImage(int position) {
        Message message = mMessages.get(position);
        if (message.getTypeAsEnum() == MessagesType.TEXT) {
            return;
        }

        String imagePath = mRid;
        String imageUri = message.getMsg();

        mView.showExpandedImageUi(imagePath, imageUri);
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

        if (message.getTypeAsEnum() == MessagesType.TEXT) {
            holder.setMsg(message.getMsg());
        } else if(message.getTypeAsEnum() == MessagesType.IMAGE) {
            holder.setImageMsg();
        }
    }

    @Override
    public MessagesType getItemViewType(int position) {
        Message message = mMessages.get(position);

        if (message.getUid().equals(mUid)) {
            return MessagesType.valueOf(message.getType());
        } else {
            switch (MessagesType.valueOf(message.getType())) {
                case TEXT:
                default: return MessagesType.O_TEXT;
                case IMAGE: return MessagesType.O_IMAGE;
                case VIDEO: return MessagesType.O_VIDEO;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public void onBackPressed() {
        mView.showOnBackPressed();
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

    private void createMessage(String uid, String msg, MessagesType type, Message.FileInfo fileInfo) {
        Message newMessage = new Message(uid, msg, type, fileInfo);

        if (newMessage == null) {
            //mView.showEmptyMessageError();
        } else {
            if (type == MessagesType.TEXT) {
                mMessagesRepository.sendMessage(newMessage);
            }
            else if (type == MessagesType.IMAGE) {
                Uri uri = Uri.parse(msg);

                mFilesRepository.setFile(uri, new FileDataSource.SetFileCallBack() {
                    @Override
                    public void onFileUploaded() {
                        newMessage.setMsg(uri.getLastPathSegment());
                        mMessagesRepository.sendMessage(newMessage);
                    }

                    @Override
                    public void onDataNotUploaded() {

                    }
                });
            }
        }
    }
}
