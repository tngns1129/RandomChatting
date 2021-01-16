package com.familyset.randomchatting.matching;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.familyset.randomchatting.MainActivity;
import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.randomRoom.RandomRoomsRepository;

public class MatchingFragment extends Fragment {
    private Button matchingBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matching, container, false);

        matchingBtn = view.findViewById(R.id.matching_btn_matching);

        matchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return view;
    }

    void showDialog() {
        DialogFragment dialogFragment = new MatchingDialogFragment();
        dialogFragment.show(getFragmentManager(), "MatchingDialogFragment");
    }
}
