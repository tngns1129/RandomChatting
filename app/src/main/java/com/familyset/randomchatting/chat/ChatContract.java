package com.familyset.randomchatting.chat;

import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;

import java.util.List;
import java.util.Map;

public interface ChatContract {
    interface View {
        void setPresenter(Presenter presenter);

        boolean isActive();

        void showMessages(List<Message> messages);
        void showUserThumbnail(UserThumbnail userThumbnail, int position);
        void showUserThumbnails(Map<String, UserThumbnail> userThumbnails);

        void clearEditText();
        void showMatchingFragment();
    }

    interface Presenter {
        void saveMessage(String msg);
        void startListening();
        void stopListening();
        void loadMessages();
        void getUserThumbnail(String uid, int position);
        void loadUserThumbnails();
        void rematching();
    }
}
