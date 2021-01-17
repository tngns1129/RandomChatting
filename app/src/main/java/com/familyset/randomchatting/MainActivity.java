package com.familyset.randomchatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.familyset.randomchatting.chat.ChatFragment;
import com.familyset.randomchatting.chat.ChatPresenter;
import com.familyset.randomchatting.data.message.MessagesRepository;
import com.familyset.randomchatting.data.message.remote.MessagesRemoteDataSource;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsRepository;
import com.familyset.randomchatting.data.userThumbnail.remote.UserThumbnailsRemoteDataSource;
import com.familyset.randomchatting.matching.MatchingFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private ChatFragment mChatFragment;
    private MatchingFragment mMatchingFragment;
    private FirebaseAuth mAuth;

    private ChatPresenter mChatPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //mAuth.getCurrentUser().getUid();

                mChatFragment = new ChatFragment();
                mMatchingFragment = new MatchingFragment();

                mFragmentManager = getSupportFragmentManager();
                mFragmentManager.beginTransaction().replace(R.id.main_frame_layout, mMatchingFragment).commit();
            }
        });


    }

    public void showChatFragment(String rid) {
        mChatPresenter = new ChatPresenter(mChatFragment,
                UserThumbnailsRepository.getInstance(UserThumbnailsRemoteDataSource.getInstance("randomRooms", "devRoom")),
                MessagesRepository.getInstance(MessagesRemoteDataSource.getInstance("randomRooms", "devRoom")));
        mFragmentManager.beginTransaction().replace(R.id.main_frame_layout, mChatFragment).commit();

        if (mMatchingFragment.isAdded()) {
            mFragmentManager.beginTransaction().hide(mMatchingFragment).commit();
        }
    }

    public void showMatchingSelectFragment() {
        if (!mMatchingFragment.isAdded()) {
            Log.d("LOGCHECK", "CHCd");
            mFragmentManager.beginTransaction().add(R.id.main_frame_layout, mMatchingFragment).commit();
        }

        //mMatchingFragment.showSelect();
        Log.d("LOGCHECK", "CHCH");
        mFragmentManager.beginTransaction().show(mMatchingFragment).commit();

        Log.d("LOGCHECK", "CHCx");
    }

}