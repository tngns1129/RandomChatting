package com.familyset.randomchatting.ui.expandedImage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.familyset.randomchatting.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class ExpandedImageFragment extends Fragment implements ExpandedImageContractor.View {

    private static final String ARGUMENT_IMAGE_URI = "IMAGE_URI";

    private ExpandedImageContractor.Presenter mPresenter;

    private PhotoView mPhotoView;

    public static ExpandedImageFragment newInstance(@Nullable String imageUri) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_IMAGE_URI, imageUri);
        ExpandedImageFragment expandedImageFragment = new ExpandedImageFragment();
        expandedImageFragment.setArguments(arguments);
        return expandedImageFragment;
    }

    @Override
    public void setPresenter(ExpandedImageContractor.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showImage(File file) {
        Glide.with(getView())
                .load(file)
                .into(mPhotoView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expanded_image, container, false);
        mPhotoView = view.findViewById(R.id.fragment_expanded_image_photo_view);
        return view;
    }
}
