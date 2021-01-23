package com.familyset.randomchatting.signUp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.familyset.randomchatting.R;

public class SignUpFragment extends Fragment implements SingUpContractor.View {
    private SignUpPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_loading, container, false);

        return view;
    }

    public void setPresenter(SignUpPresenter presenter) {
        mPresenter = presenter;
    }
}
