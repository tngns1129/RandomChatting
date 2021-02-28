package com.familyset.randomchatting.ui.signIn.anonymous;

import android.util.Log;

import androidx.annotation.NonNull;

import com.familyset.randomchatting.data.user.User;
import com.familyset.randomchatting.data.user.UsersDataSource;
import com.familyset.randomchatting.data.user.UsersRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AnonymousSignInPresenter implements AnonymousSignInContractor.Presenter {
    private AnonymousSignInContractor.View mView;
    private UsersRepository mUsersRepository;

    private FirebaseAuth mAuth;

    public AnonymousSignInPresenter(@NonNull UsersRepository usersRepository, @NonNull AnonymousSignInContractor.View view) {
        mUsersRepository = usersRepository;
        mView = view;
        mAuth = FirebaseAuth.getInstance();

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        createUser();
    }

    private void showMatchingUI(User user) {
        mView.showMatchingUI(user);
    }

    private void createUser() {
        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User newUser = User.createNewUser(authResult.getUser().getUid());

                mUsersRepository.setUser(newUser, new UsersDataSource.SetUsersCallBack() {
                    @Override
                    public void onUserUpLoaded() {
                        showMatchingUI(newUser);
                    }

                    @Override
                    public void onUserNotUpLoaded() {

                    }
                });
            }
        });
    }
}