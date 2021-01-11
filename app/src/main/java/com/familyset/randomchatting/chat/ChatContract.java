package com.familyset.randomchatting.chat;

import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesDataSource;

import java.util.List;

public interface ChatContract {
    interface View {
        void setPresenter(Presenter presenter);

        boolean isActive();

        void showMessages(List<Message> messages);
    }

    interface Presenter {
        void saveMessage(String uid, String msg);
        void startListening();
        void stopListening();
        void loadMessages();
    }
}
