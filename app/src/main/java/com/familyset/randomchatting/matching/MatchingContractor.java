package com.familyset.randomchatting.matching;

public interface MatchingContractor {
    interface View {
        void showChatFragment(String rid);
        void showSearching();
        void showCreating();
        void showWaiting();
    }

    interface Presenter {
        void startMatch(String uid);
        void enterDevRoom(String uid);
    }
}
