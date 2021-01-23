package com.familyset.randomchatting.matching;

import android.provider.Settings;

import androidx.annotation.Nullable;

import com.familyset.randomchatting.data.randomRoom.RandomRoomsDataSource;
import com.familyset.randomchatting.data.randomRoom.RandomRoomsRepository;
import com.familyset.randomchatting.data.user.User;
import com.familyset.randomchatting.data.user.UsersDataSource;
import com.familyset.randomchatting.data.user.UsersRepository;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.familyset.randomchatting.util.PreferenceManager;

public class MatchingPresenter implements MatchingContractor.Presenter {
    private MatchingContractor.View mView;
    private RandomRoomsRepository mRandomRoomsRepository;
    private UsersRepository mUsersRepository;

    private int matchState = 0; // not matched = 0, matched = 1
    private String mUid = null;
    private String mRid = null;

    private UserThumbnail mUserThumbnail;

    public MatchingPresenter(String uid, MatchingContractor.View view, RandomRoomsRepository randomRoomsRepository, UsersRepository usersRepository) {
        mUid = uid;

        mView = view;
        mRandomRoomsRepository = randomRoomsRepository;
        mUsersRepository = usersRepository;

        setUserThumbnail();
        mView.setPresenter(this);
    }

    public void setUserThumbnail() {
        //mUserThumbnail = userThumbnail;

        mUsersRepository.getUser(mUid, new UsersDataSource.GetUsersCallBack() {
            @Override
            public void onUserLoaded(User user) {
                mUserThumbnail = user.getUserThumbnail();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void enterDevRoom() {
        mRandomRoomsRepository.enterDevRoom(mUid, new RandomRoomsDataSource.SearchCallBack() {
            @Override
            public void onSearchFinished(String rid) {
                mView.showChatFragment(rid);

                mRid = rid;
            }
        });
    }

    @Override
    public void quitRoom() {
        if (mUid != null & mRid != null) {
            mRandomRoomsRepository.quitRoom(mUid, mRid);
        }

        matchState = 0;
        mRid = null;
    }

    @Override
    public void startMatch(@Nullable Integer sex) {
        mView.showSearching();
        mRandomRoomsRepository.searchEmptyRoom(mUserThumbnail, new RandomRoomsDataSource.SearchCallBack() {
            @Override
            public void onSearchFinished(String rid) {
                if (rid == null) {
                    mView.showCreating();
                    mRandomRoomsRepository.createRoom(mUserThumbnail, new RandomRoomsDataSource.CreateCallBack() {
                        @Override
                        public void onCreateFinished() {
                            mView.showWaiting();
                        }

                        @Override
                        public void onMatchFinished(String rid) {
                            matchState = 1;
                            mRid = rid;

                            mView.showChatFragment(mRid);
                        }
                    });
                } else {
                    matchState = 1;
                    mRid = rid;

                    mView.showChatFragment(mRid);
                }
            }
        });
    }
}
