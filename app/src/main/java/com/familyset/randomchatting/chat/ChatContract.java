package com.familyset.randomchatting.chat;

import androidx.recyclerview.widget.RecyclerView;

import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;

import java.util.List;
import java.util.Map;

public interface ChatContract {
    interface View {
        void setPresenter(Presenter presenter);

        boolean isActive();

        void showMessages();
        void showUserThumbnail(int position);
        void showUserThumbnails();

        void clearEditText();
        void showMatchingFragment();
    }

    interface RecyclerRowView {
        void setUserThumbnail(UserThumbnail userThumbnail);
        void setMsg(String msg);
    }

    interface Presenter {
        void saveMessage(String msg);
        void startListening();
        void stopListening();
        void loadMessages();
        void getUserThumbnail(String uid, int position);
        void loadUserThumbnails();
        void rematching();

        void onBindViewHolder(int position, ChatContract.RecyclerRowView holder);
        boolean getItemViewType(int position);
        int getItemCount();
    }
}
