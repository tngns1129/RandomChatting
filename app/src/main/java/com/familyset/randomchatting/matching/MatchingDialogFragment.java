package com.familyset.randomchatting.matching;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.familyset.randomchatting.MainActivity;
import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.randomRoom.RandomRoomsRepository;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MatchingDialogFragment extends BottomSheetDialogFragment implements MatchingContractor.View {
    private MatchingContractor.Presenter mPresenter;
    private String uid;

    private Button statusBtn;
    private Button randomMatchingBtn;
    private Button oppositeSexMatchingBtn;

    private LinearLayout statusLinearLayout;
    private LinearLayout selectLinearLayout;

    private Dialog mDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //mPresenter.init();

            return mDialog;
        }
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());

        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_matching_select, null);

        uid = "asdfs";
        mPresenter = new MatchingPresenter(this, new RandomRoomsRepository());

        statusLinearLayout = view.findViewById(R.id.dialog_matching_select_linear_status);
        selectLinearLayout = view.findViewById(R.id.dialog_matching_select_linear_select);
        statusBtn = view.findViewById(R.id.dialog_matching_select_btn_status);
        randomMatchingBtn = view.findViewById(R.id.dialog_matching_select_btn_random_matching);
        oppositeSexMatchingBtn = view.findViewById(R.id.dialog_matching_select_btn_opposite_sex_matching);

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
        bottomSheetDialog.setContentView(view);
        //builder.setView(view);
        //mDialog = builder.create();
        return bottomSheetDialog;
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
        selectLinearLayout.setVisibility(View.GONE);
        statusLinearLayout.setVisibility(View.VISIBLE);
        statusBtn.setText("빈 방 검색중");
    }

    // 빈 방 생성 뷰
    @Override
    public void showCreating() {
        statusBtn.setText("새로운 방 생성 중");
    }

    // 빈 방 생성 후 상대방 대기 뷰
    @Override
    public void showWaiting() {
        statusBtn.setText("매칭 대기 중");
    }
}
