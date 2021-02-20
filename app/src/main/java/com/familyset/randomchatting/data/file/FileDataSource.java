package com.familyset.randomchatting.data.file;

import android.net.Uri;

import java.io.File;

public interface FileDataSource {
    interface LoadFilesCallBack {

    }

    interface GetFilesCallBack {
        void onFileLoaded();
        void onDataNotAvailable();
    }

    interface UploadFilesCallBack {

    }

    interface SetFileCallBack {
        void onFileUploaded();
        void onDataNotUploaded();
    }

    void getFile(String uri, File file, GetFilesCallBack callBack);

    void setFile(Uri uri, SetFileCallBack callBack);
}
