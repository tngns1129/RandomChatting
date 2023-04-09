package com.familyset.randomchatting.ui.chat;

import androidx.annotation.Nullable;

import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesType;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;

import java.util.Date;

public interface ChatContract {

    interface OnBackPressedListener{
        void onBackPressed();
    }

    interface View {
        void setPresenter(Presenter presenter);

        boolean isActive();

        void showMessage(int position);
        void showMessages();
        void showUserThumbnail(int position);
        void showUserThumbnails();

        void clearEditText();

        void showExpandedImageUi(String imagePath, String imageUri);

        void showMatchingFragment();

        void showOnBackPressed();
    }

    interface RecyclerRowView {
        void setUserThumbnail(UserThumbnail userThumbnail);
        void setMsg(String msg);
        void setImageMsg();

        void setTimeStamp(String realTimestamp);
    }

    interface Presenter {
        void sendMessage(String msg, MessagesType msgType, @Nullable Message.FileInfo fileInfo);
        void startListening();
        void stopListening();
        void loadMessages();
        void getUserThumbnail(String uid, int position);
        void loadUserThumbnails();
        void rematching();
        void openExpandedImage(int position);
        void reportUser();

        void onBindViewHolder(int position, ChatContract.RecyclerRowView holder);
        MessagesType getItemViewType(int position);
        int getItemCount();

        void onBackPressed();
    }
}
