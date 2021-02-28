package com.familyset.randomchatting.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.user.User;
import com.familyset.randomchatting.data.user.UsersDataSource;
import com.familyset.randomchatting.data.user.UsersRepository;
import com.familyset.randomchatting.data.user.remote.UsersRemoteDataSource;
import com.familyset.randomchatting.ui.main.MainActivity;
import com.familyset.randomchatting.ui.signIn.authMethodPick.AuthMethodPickActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_RandomChatting);
        super.onCreate(savedInstanceState);

        checkSignedIn();
    }

    private void checkSignedIn() {
        // TODO 인터넷 연결 확인

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // not signed in
            showAuthMethodPickUI();
        } else {
            // already signed in
            showMainUI();
        }
    }

    private void showAuthMethodPickUI() {
        Intent intent = new Intent(getApplicationContext(), AuthMethodPickActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMainUI() {
        UsersRepository.getInstance(UsersRemoteDataSource.getInstance())
                .getUser(FirebaseAuth.getInstance().getUid(), new UsersDataSource.GetUsersCallBack() {
                    @Override
                    public void onUserLoaded(User user) {
                        startActivity(MainActivity.createIntent(getApplicationContext(), user));
                        finish();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }
}
