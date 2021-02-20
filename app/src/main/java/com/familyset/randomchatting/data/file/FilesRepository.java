package com.familyset.randomchatting.data.file;

import android.net.Uri;

import com.familyset.randomchatting.data.file.remote.FilesRemoteDataSource;

import java.io.File;

public class FilesRepository implements FileDataSource {
    private static FilesRepository INSTANCE = null;
    private final FilesRemoteDataSource mFilesRemoteDataSource;

    private FilesRepository(FilesRemoteDataSource filesRemoteDataSource) {
        mFilesRemoteDataSource = filesRemoteDataSource;
    }

    public static FilesRepository getInstance(FilesRemoteDataSource filesRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FilesRepository(filesRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getFile(String uri, File file, GetFilesCallBack callBack) {
        mFilesRemoteDataSource.getFile(uri, file, callBack);
    }

    @Override
    public void setFile(Uri uri, SetFileCallBack callBack) {
        mFilesRemoteDataSource.setFile(uri, callBack);
    }
}
