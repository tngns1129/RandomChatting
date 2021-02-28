package com.familyset.randomchatting.ui.signIn.authMethodPick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.familyset.randomchatting.R;
import com.familyset.randomchatting.ui.signIn.anonymous.AnonymousSignInActivity;

public class AuthMethodPickFragment extends Fragment implements AuthMethodPickContract.View {

    private AuthMethodPickContract.Present mAuthMethodPickPresenter;

    private AuthMethodPickFragment() {}

    public static AuthMethodPickFragment newInstance() {
        return new AuthMethodPickFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_method_pick, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuthMethodPickPresenter.start();
    }

    @Override
    public void setPresenter(AuthMethodPickContract.Present presenter) {
        mAuthMethodPickPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showAnonymousSignInUI() {
        Intent intent = new Intent(getContext(), AnonymousSignInActivity.class);
        getActivity().startActivityForResult(intent, AuthMethodPickActivity.RC_SIGN_IN);
    }
}
