package com.familyset.randomchatting.ui.expandedImage;

import com.familyset.randomchatting.data.file.FileDataSource;
import com.familyset.randomchatting.data.file.FilesRepository;

import java.io.File;
import java.io.IOException;

public class ExpandedImagePresenter implements ExpandedImageContractor.Presenter {
    private final FilesRepository mFilesRepository;

    private final ExpandedImageContractor.View mExpandedImageView;

    private String mExpandedImageUri;

    public ExpandedImagePresenter(String expandedImageUri,
                                  FilesRepository filesRepository,
                                  ExpandedImageContractor.View expandedImageView) {
        mExpandedImageUri = expandedImageUri;
        mFilesRepository = filesRepository;
        mExpandedImageView = expandedImageView;

        mExpandedImageView.setPresenter(this);
    }

    @Override
    public void start() {
        openImage();
    }

    private void openImage() {
        if (mExpandedImageUri == null) {
            // TODO 예외상황
            return;
        }

        try {
            File tempFile = File.createTempFile("image", "JPEG");

            mFilesRepository.getFile(mExpandedImageUri, tempFile, new FileDataSource.GetFilesCallBack() {
                @Override
                public void onFileLoaded() {
                    showImage(tempFile);
                }

                @Override
                public void onDataNotAvailable() {

                }
            });

            tempFile.deleteOnExit();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    private void showImage(File file) {
        mExpandedImageView.showImage(file);
    }
}
