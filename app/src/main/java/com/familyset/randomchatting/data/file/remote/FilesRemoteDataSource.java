package com.familyset.randomchatting.data.file.remote;

import android.net.Uri;

import com.familyset.randomchatting.data.file.FileDataSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FilesRemoteDataSource implements FileDataSource {
    private static FilesRemoteDataSource INSTANCE = null;
    private final StorageReference fileStorageRef;

    public static FilesRemoteDataSource getInstance(String path) {
        if (INSTANCE == null) {
            INSTANCE = new FilesRemoteDataSource(path);
        }

        return INSTANCE;
    }

    private FilesRemoteDataSource(String path) {
        fileStorageRef = FirebaseStorage.getInstance().getReference("files").child(path);
    }

    @Override
    public void getFile(String uri, File file, GetFilesCallBack callBack) {
        fileStorageRef.child(uri).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                callBack.onFileLoaded();
            }
        });
    }

    @Override
    public void setFile(Uri uri, SetFileCallBack callBack) {
        fileStorageRef.child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callBack.onFileUploaded();
            }
        });
    }
}
