package com.familyset.randomchatting.ui.signIn.authMethodPick;

public class AuthMethodPickPresenter implements AuthMethodPickContract.Present {

    private AuthMethodPickContract.View mView;

    public AuthMethodPickPresenter(AuthMethodPickContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        showAnonymousSignInUI();
    }

    private void showAnonymousSignInUI() {
        mView.showAnonymousSignInUI();
    }
}
