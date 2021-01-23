package com.familyset.randomchatting.signUp;

import androidx.annotation.NonNull;

import com.familyset.randomchatting.data.user.User;
import com.familyset.randomchatting.data.user.UsersRepository;

public class SignUpPresenter {
    private SingUpContractor.View mView;
    private UsersRepository mUsersRepository;
    private String mUid;
    private User mUser;

    public SignUpPresenter(@NonNull String uid, @NonNull UsersRepository usersRepository, @NonNull SingUpContractor.View view){
        mUid = uid;
        mUsersRepository = usersRepository;
        mView = view;
    }

}
