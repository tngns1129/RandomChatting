package com.familyset.randomchatting.ui.matching;

import androidx.annotation.Nullable;

import com.familyset.randomchatting.ui.BasePresenter;
import com.familyset.randomchatting.ui.BaseView;

public interface MatchingContractor {
    interface View extends BaseView<Presenter> {
        void showChatFragment(String rid);
        void showSearching();
        void showCreating();
        void showWaiting();
        void setPresenter(Presenter presenter);
        void initView(int matchState);

        void showOnBackPressed();
    }

    interface Presenter extends BasePresenter {
        void onBackPressed();

        void startMatch(@Nullable Integer sex);
        void enterDevRoom();
        void quitRoom();
        void restartMatch(@Nullable Integer sex);
        int getMatchState();
        void setMatchState(int matchState);
    }
}
