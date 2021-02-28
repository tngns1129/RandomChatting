package com.familyset.randomchatting.ui.signIn.anonymous;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.user.User;
import com.familyset.randomchatting.data.user.UsersRepository;
import com.familyset.randomchatting.data.user.remote.UsersRemoteDataSource;
import com.familyset.randomchatting.util.ActivityUtils;

public class AnonymousSignInActivity extends AppCompatActivity {
    private AnonymousSignInPresenter mAnonymousSignInPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_sign_in);

        AnonymousSignInFragment anonymouslySignInFragment =
                (AnonymousSignInFragment) getSupportFragmentManager().findFragmentById(R.id.sign_in_frame_layout);

        if (anonymouslySignInFragment == null) {
            // Create the fragment
            anonymouslySignInFragment = AnonymousSignInFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), anonymouslySignInFragment, R.id.sign_in_frame_layout);
        }

        mAnonymousSignInPresenter = new AnonymousSignInPresenter(
                UsersRepository.getInstance(UsersRemoteDataSource.getInstance()), anonymouslySignInFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DSF", "destroy anony act");
    }
}