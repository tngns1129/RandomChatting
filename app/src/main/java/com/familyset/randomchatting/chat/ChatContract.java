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
        void showUserThumbnails(Map<String, UserThumbnail> users);
    }

    interface Presenter {
        void saveMessage(String uid, String msg);
        void startListening();
        void stopListening();
        void loadMessages();
        void loadUsers();
    }
}
