package com.familyset.randomchatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.familyset.randomchatting.chat.ChatFragment;
import com.familyset.randomchatting.chat.ChatPresenter;
import com.familyset.randomchatting.data.message.MessagesRepository;
import com.familyset.randomchatting.data.message.remote.MessagesRemoteDataSource;
import com.familyset.randomchatting.data.randomRoom.RandomRoomsRepository;
import com.familyset.randomchatting.data.user.UsersRepository;
import com.familyset.randomchatting.data.user.remote.UsersRemoteDataSource;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsRepository;
import com.familyset.randomchatting.data.userThumbnail.remote.UserThumbnailsRemoteDataSource;
import com.familyset.randomchatting.matching.MatchingFragment;
import com.familyset.randomchatting.matching.MatchingPresenter;
import com.familyset.randomchatting.util.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private ChatFragment mChatFragment;
    private MatchingFragment mMatchingFragment;

    private ChatPresenter mChatPresenter;
    private MatchingPresenter mMatchingPresenter;

    private String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUID();

        mMatchingFragment = new MatchingFragment();

        mFragmentManager = getSupportFragmentManager();

        showMatchingFragment();
    }

    public void setUID() {
        if ((mUid = PreferenceManager.getString(getApplicationContext(), "UID")).equals("")) {
            mUid = UUID.randomUUID().toString();
            PreferenceManager.setString(getApplicationContext(), "UID", mUid);
        }

        Log.d("UID", mUid);
    }

    public void showChatFragment(String rid) {
        mChatFragment = new ChatFragment();
        mChatPresenter = new ChatPresenter(mUid, mChatFragment,
                UserThumbnailsRepository.getInstance(UserThumbnailsRemoteDataSource.getInstance("randomRooms", rid)),
                MessagesRepository.getInstance(MessagesRemoteDataSource.getInstance("randomRooms", rid)));
        mFragmentManager.beginTransaction().replace(R.id.main_frame_layout, mChatFragment).commit();

        if (mMatchingFragment.isAdded()) {
            mFragmentManager.beginTransaction().hide(mMatchingFragment).commit();
        }
    }

    public void showMatchingFragment() {
        if (!mMatchingFragment.isAdded()) {
            mMatchingPresenter = new MatchingPresenter(mUid, mMatchingFragment,
                    new RandomRoomsRepository(),
                    UsersRepository.getInstance(UsersRemoteDataSource.getInstance()));
            mFragmentManager.beginTransaction().add(R.id.main_frame_layout, mMatchingFragment).commit();
        }

        //mMatchingFragment.showSelect();
        mFragmentManager.beginTransaction().show(mMatchingFragment).commit();
    }

}