package com.familyset.randomchatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

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

    private FragmentManager fragmentManager;
    private ChatFragment chatFragment;
    private MatchingFragment matchingFragment;
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

                chatFragment = new ChatFragment();
                matchingFragment = new MatchingFragment();

                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_frame_layout, matchingFragment).commit();

            }
        });


    }

    public void showChatFragment(String rid) {
        mChatPresenter = new ChatPresenter(chatFragment,
                UserThumbnailsRepository.getInstance(UserThumbnailsRemoteDataSource.getInstance("randomRooms", "devRoom")),
                MessagesRepository.getInstance(MessagesRemoteDataSource.getInstance("randomRooms", "devRoom")));
        fragmentManager.beginTransaction().replace(R.id.main_frame_layout, chatFragment).commit();
    }
}