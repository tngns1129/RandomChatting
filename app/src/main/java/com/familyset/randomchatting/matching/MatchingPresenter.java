package com.familyset.randomchatting.matching;

import com.familyset.randomchatting.data.randomRoom.RandomRoomsDataSource;
import com.familyset.randomchatting.data.randomRoom.RandomRoomsRepository;

public class MatchingPresenter implements MatchingContractor.Presenter {
    private MatchingContractor.View mView;
    private RandomRoomsRepository mRandomRoomsRepository;

    public MatchingPresenter(MatchingContractor.View view, RandomRoomsRepository randomRoomsRepository) {
        mView = view;
        mRandomRoomsRepository = randomRoomsRepository;
    }

    @Override
    public void enterDevRoom(String uid) {
        mRandomRoomsRepository.enterDevRoom(uid, new RandomRoomsDataSource.SearchCallBack() {
            @Override
            public void onSearchFinished(String rid) {
                mView.showChatFragment(rid);
            }
        });
    }

    @Override
    public void startMatch(String uid) {
        mView.showSearching();
        mRandomRoomsRepository.searchEmptyRoom(uid, new RandomRoomsDataSource.SearchCallBack() {
            @Override
            public void onSearchFinished(String rid) {
                if (rid == null) {
                    mView.showCreating();
                    mRandomRoomsRepository.createRoom(uid, new RandomRoomsDataSource.CreateCallBack() {
                        @Override
                        public void onCreateFinished() {
                            mView.showWaiting();
                        }

                        @Override
                        public void onMatchFinished(String rid) {
                            mView.showChatFragment(rid);
                        }
                    });
                } else {
                    mView.showChatFragment(rid);
                }
            }
        });
    }
}
