package com.familyset.randomchatting.ui.signIn.authMethodPick;

import com.familyset.randomchatting.ui.BasePresenter;
import com.familyset.randomchatting.ui.BaseView;

public interface AuthMethodPickContract {
    interface View extends BaseView<Present> {
        void showAnonymousSignInUI();
    }

    interface Present extends BasePresenter {
        //void showAnonymousSignInUI();
    }
}
