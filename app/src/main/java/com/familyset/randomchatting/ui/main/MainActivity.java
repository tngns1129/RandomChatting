package com.familyset.randomchatting.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.file.FilesRepository;
import com.familyset.randomchatting.data.file.remote.FilesRemoteDataSource;
import com.familyset.randomchatting.data.user.User;
import com.familyset.randomchatting.ui.chat.ChatContract;
import com.familyset.randomchatting.ui.chat.ChatFragment;
import com.familyset.randomchatting.ui.chat.ChatPresenter;
import com.familyset.randomchatting.data.message.MessagesRepository;
import com.familyset.randomchatting.data.message.remote.MessagesRemoteDataSource;
import com.familyset.randomchatting.data.randomRoom.RandomRoomsRepository;
import com.familyset.randomchatting.data.user.UsersRepository;
import com.familyset.randomchatting.data.user.remote.UsersRemoteDataSource;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsRepository;
import com.familyset.randomchatting.data.userThumbnail.remote.UserThumbnailsRemoteDataSource;
import com.familyset.randomchatting.ui.matching.MatchingFragment;
import com.familyset.randomchatting.ui.matching.MatchingPresenter;
import com.familyset.randomchatting.util.ActivityUtils;
import com.familyset.randomchatting.util.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ChatFragment mChatFragment;
    private MatchingFragment mMatchingFragment;

    private ChatPresenter mChatPresenter;
    private MatchingPresenter mMatchingPresenter;

    public static final String USER = "CURRENT_USER";
    private User mUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);   //캡쳐 금지
        setContentView(R.layout.activity_main);

        mUser = getIntent().getParcelableExtra(USER);

        showMatchingFragment();
    }

    @Override
    public void onBackPressed() {
            if (mMatchingFragment.isActive() && mMatchingPresenter != null) {
                mMatchingPresenter.onBackPressed();
            } else if (mChatFragment.isActive() && mChatPresenter != null) {
                mChatPresenter.onBackPressed();
            } else {
                super.onBackPressed();
            }
    }

    public void showChatFragment(String rid) {
        mChatFragment = new ChatFragment();
        mChatPresenter = new ChatPresenter(mUser.getUid(), rid, mChatFragment,
                UserThumbnailsRepository.getInstance(UserThumbnailsRemoteDataSource.getInstance("randomRooms", rid)),
                MessagesRepository.getInstance(MessagesRemoteDataSource.getInstance("randomRooms", rid)),
                FilesRepository.getInstance(FilesRemoteDataSource.getInstance(rid)));

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, mChatFragment).commit();

        if (mMatchingFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(mMatchingFragment).commit();
        }
    }

    public void showMatchingFragment() {
        if (mMatchingFragment == null || !mMatchingFragment.isAdded()) {
            mMatchingFragment = new MatchingFragment();

            mMatchingPresenter = new MatchingPresenter(mUser.getUid(), mMatchingFragment,
                    new RandomRoomsRepository(),
                    UsersRepository.getInstance(UsersRemoteDataSource.getInstance()));
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout, mMatchingFragment).commit();
        }

        //mMatchingFragment.showSelect();
        getSupportFragmentManager().beginTransaction().show(mMatchingFragment).commit();
    }

    public static Intent createIntent(Context context, User user) {
        Intent intent = ActivityUtils.createBaseIntent(context, MainActivity.class);
        intent.putExtra(MainActivity.USER, user);
        return intent;
    }
}