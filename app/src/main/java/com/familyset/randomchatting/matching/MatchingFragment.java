package com.familyset.randomchatting.matching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.familyset.randomchatting.MainActivity;
import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.randomRoom.RandomRoomsRepository;

public class MatchingFragment extends Fragment implements MatchingContractor.View {
    private MatchingContractor.Presenter mPresenter;
    private String uid;
    private Button matchingBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matching, container, false);

        uid = "asdfas";
        mPresenter = new MatchingPresenter(this, new RandomRoomsRepository());

        matchingBtn = view.findViewById(R.id.matching_btn_matching);

        matchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startMatch(uid);
            }
        });

        matchingBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.enterDevRoom(uid);
                return true;
            }
        });

        return view;
    }

    // 채팅 프래그먼트로 전환, Room 정보를 인자로 주는 게 나아보임
    @Override
    public void showChatFragment(String rid) {
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new ChatFragment()).commit();
        if (rid.equals("devRoom")) {
            Toast.makeText(getContext(), "개발자 방 입장", Toast.LENGTH_SHORT).show();
        }

        ((MainActivity)getActivity()).showChatFragment(rid);
    }

    // 빈 방 탐색 뷰
    @Override
    public void showSearching() {
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

}
