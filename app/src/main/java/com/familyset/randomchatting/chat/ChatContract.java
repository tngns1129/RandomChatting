package com.familyset.randomchatting.chat;

public interface ChatContract {
    interface View {

    }

    interface Presenter {
        void sendMsg(String msg);
    }
}
