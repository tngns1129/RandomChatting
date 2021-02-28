package com.familyset.randomchatting.ui.signIn.anonymous;

import android.content.Context;

import com.familyset.randomchatting.data.user.User;
import com.familyset.randomchatting.ui.BasePresenter;
import com.familyset.randomchatting.ui.BaseView;

public interface AnonymousSignInContractor {
    interface View extends BaseView<Presenter> {
        void showMatchingUI(User user);
    }

    interface Presenter extends BasePresenter {
    }
}
