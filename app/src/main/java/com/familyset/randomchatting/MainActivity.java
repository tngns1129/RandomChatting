package com.familyset.randomchatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.familyset.randomchatting.data.file.FilesRepository;
import com.familyset.randomchatting.data.file.remote.FilesRemoteDataSource;
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
import com.familyset.randomchatting.util.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ChatContract.OnBackPressedListener listener;

    private FragmentManager mFragmentManager;
    private ChatFragment mChatFragment;
    private MatchingFragment mMatchingFragment;

    private ChatPresenter mChatPresenter;
    private MatchingPresenter mMatchingPresenter;

    private String mUid;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMatchingFragment = new MatchingFragment();

        mFragmentManager = getSupportFragmentManager();

        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                setUID();

                showMatchingFragment();
            }
        });
    }

    @Override
    public void onBackPressed() {
            if (listener != null) {
                listener.onBackPressed();
            } else {
                super.onBackPressed();
            }

    }

    public void setUID() {
        /*if ((mUid = PreferenceManager.getString(getApplicationContext(), "UID")).equals("")) {
            mUid = mAuth.getUid();
            PreferenceManager.setString(getApplicationContext(), "UID", mUid);
        }

         */

        mUid = "d3d29ed6-6397-4219-99de-ce8653ef9bde";
        Log.d("UID", mUid);
    }

    public void showChatFragment(String rid) {
        mChatFragment = new ChatFragment();
        mChatPresenter = new ChatPresenter(mUid, rid, mChatFragment,
                UserThumbnailsRepository.getInstance(UserThumbnailsRemoteDataSource.getInstance("randomRooms", rid)),
                MessagesRepository.getInstance(MessagesRemoteDataSource.getInstance("randomRooms", rid)),
                FilesRepository.getInstance(FilesRemoteDataSource.getInstance(rid)));

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

    public void setOnBackPressedListener(ChatContract.OnBackPressedListener listener) {
        this.listener = listener;
    }
}