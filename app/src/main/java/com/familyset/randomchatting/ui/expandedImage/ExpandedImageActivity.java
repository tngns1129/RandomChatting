package com.familyset.randomchatting.ui.expandedImage;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.file.FilesRepository;
import com.familyset.randomchatting.data.file.remote.FilesRemoteDataSource;

public class ExpandedImageActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_PATH = "IMAGE_PATH";
    public static final String EXTRA_IMAGE_URI = "IMAGE_URI";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_image);

        ExpandedImageFragment expandedImageFragment =
                (ExpandedImageFragment) getSupportFragmentManager().findFragmentById(R.id.expanded_image_frame_layout);

        // Get the requested image path, uri
        String imagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
        String imageUri = getIntent().getStringExtra(EXTRA_IMAGE_URI);

        if (expandedImageFragment == null) {
            // create the fragment
            expandedImageFragment = ExpandedImageFragment.newInstance(imageUri);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.expanded_image_frame_layout, expandedImageFragment).commit();
        }

        new ExpandedImagePresenter(imageUri,
                FilesRepository.getInstance(FilesRemoteDataSource.getInstance(imagePath)),
                expandedImageFragment);
    }
}