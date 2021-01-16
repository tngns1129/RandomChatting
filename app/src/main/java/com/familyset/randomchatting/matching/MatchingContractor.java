package com.familyset.randomchatting.matching;

import androidx.annotation.Nullable;

public interface MatchingContractor {
    interface View {
        void showChatFragment(String rid);
        void showSearching();
        void showCreating();
        void showWaiting();
    }

    interface Presenter {
        void startMatch(String uid, @Nullable Integer sex);
        void enterDevRoom(String uid);
    }
}
