package com.familyset.randomchatting.matching;

import androidx.annotation.Nullable;

public interface MatchingContractor {
    interface View {
        void showChatFragment(String rid);
        void showSearching();
        void showCreating();
        void showWaiting();
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void startMatch(@Nullable Integer sex);
        void enterDevRoom();
        void quitRoom();
    }
}
