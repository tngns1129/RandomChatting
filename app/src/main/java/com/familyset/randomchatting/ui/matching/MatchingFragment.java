package com.familyset.randomchatting.ui.matching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.familyset.randomchatting.ui.main.MainActivity;
import com.familyset.randomchatting.R;

public class MatchingFragment extends Fragment implements MatchingContractor.View {
    private MatchingContractor.Presenter mPresenter;

    private Button matchingBtn;
    /*
    private Button randomMatchingBtn;
    private Button oppositeSexMatchingBtn;

    private LinearLayout statusLinearLayout;
    private LinearLayout selectLinearLayout;
    */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matching, container, false);

        /*
        statusLinearLayout = view.findViewById(R.id.matching_linear_status);
        selectLinearLayout = view.findViewById(R.id.matching_linear_select);
        randomMatchingBtn = view.findViewById(R.id.matching_btn_random_matching);
        oppositeSexMatchingBtn = view.findViewById(R.id.matching_btn_opposite_sex_matching);
         */
        matchingBtn = view.findViewById(R.id.matching_btn_status);

        matchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showSelect();
                mPresenter.startMatch(null);
            }
        });

        matchingBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.enterDevRoom();
                return false;
            }
        });

        /*
        randomMatchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startMatch(uid, null);
            }
        });

        oppositeSexMatchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO mPresenter.startMatch(uid, sex);
                mPresenter.startMatch(uid, 0);
            }
        });

        randomMatchingBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.enterDevRoom(uid);
                return false;
            }
        });
        */

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("MATCH_STATE", mPresenter.getMatchState());
    }

    /*
        private void showSelect() {
            matchingBtn.setEnabled(false);
            statusLinearLayout.setVisibility(View.GONE);
            selectLinearLayout.setVisibility(View.VISIBLE);
        }
        */

    @Override
    public void initView(int matchState) {
        matchingBtn.setEnabled(true);

        if (matchState == 0) {
            matchingBtn.setText("매칭 시작");
        } else {
            matchingBtn.setText("재매칭 시작");
        }
    }

    @Override
    public void showOnBackPressed() {
        // TODO if matching select possible
    }

    // 채팅 프래그먼트로 전환, Room 정보를 인자로 주는 게 나아보임
    @Override
    public void showChatFragment(String rid) {
        if (rid.equals("devRoom")) {
            Toast.makeText(getContext(), "개발자 방 입장", Toast.LENGTH_SHORT).show();
        }

        ((MainActivity)getActivity()).showChatFragment(rid);
    }

    // 빈 방 탐색 뷰
    @Override
    public void showSearching() {
        //selectLinearLayout.setVisibility(View.GONE);
        //statusLinearLayout.setVisibility(View.VISIBLE);
        matchingBtn.setEnabled(false);
        matchingBtn.setText("빈 방 검색중");
    }

    // 빈 방 생성 뷰
    @Override
    public void showCreating() {
        matchingBtn.setText("새로운 방 생성 중");
    }

    // 빈 방 생성 후 상대방 대기 뷰
    @Override
    public void showWaiting() {
        matchingBtn.setText("매칭 대기 중");
    }

    @Override
    public void setPresenter(MatchingContractor.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
