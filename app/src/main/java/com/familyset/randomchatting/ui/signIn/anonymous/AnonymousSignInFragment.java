package com.familyset.randomchatting.ui.signIn.anonymous;

import android.app.Activity;
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
import com.familyset.randomchatting.data.user.User;
import com.familyset.randomchatting.ui.main.MainActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class AnonymousSignInFragment extends Fragment implements AnonymousSignInContractor.View {

    private AnonymousSignInContractor.Presenter mPresenter;

    private AnonymousSignInFragment() {}

    public static AnonymousSignInFragment newInstance() {
        return new AnonymousSignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anonymous_sign_in, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter.start();
    }

    @Override
    public void setPresenter(AnonymousSignInContractor.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMatchingUI(User user) {
        getActivity().setResult(Activity.RESULT_OK,
                MainActivity.createIntent(getContext(), user));
        getActivity().finish();
    }
}