package com.familyset.randomchatting.ui.expandedImage;

import java.io.File;

public interface ExpandedImageContractor {
    interface View {
        void setPresenter(Presenter presenter);

        void showImage(File file);
    }

    interface Presenter {
        void start();
    }
}
