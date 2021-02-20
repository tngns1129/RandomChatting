package com.familyset.randomchatting.ui.matching;

import androidx.annotation.Nullable;

public interface MatchingContractor {
    interface View {
        void showChatFragment(String rid);
        void showSearching();
        void showCreating();
        void showWaiting();
        void setPresenter(Presenter presenter);
        void initView(int matchState);
    }

    interface Presenter {
        void startMatch(@Nullable Integer sex);
        void enterDevRoom();
        void quitRoom();
        void restartMatch(@Nullable Integer sex);
        int getMatchState();
        void setMatchState(int matchState);
    }
}
