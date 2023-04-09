package com.familyset.randomchatting.data.userThumbnail.remote;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsDataSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class UserThumbnailsRemoteDataSource implements UserThumbnailsDataSource {
    private static UserThumbnailsRemoteDataSource INSTANCE = null;
    private final CollectionReference mUserThumbnailsColRef;
    private final StorageReference mUserThumbnailPhotosStorageRef;
    private ListenerRegistration mUserThumbnailsListenerRegistration;

    private List<UserThumbnail> mUserThumbnails;

    public static UserThumbnailsRemoteDataSource getInstance(@NonNull String colRef, @NonNull String docRef) {
        if (INSTANCE == null) {
            INSTANCE = new UserThumbnailsRemoteDataSource(colRef, docRef);
        }

        return INSTANCE;
    }

    private UserThumbnailsRemoteDataSource(@NonNull String colRef, @NonNull String docRef) {
        mUserThumbnailsColRef = FirebaseFirestore.getInstance().collection(colRef).document(docRef).collection("userThumbnails");
        mUserThumbnailPhotosStorageRef = FirebaseStorage.getInstance().getReference("user_photos/");
    }

    @Override
    public void getUserThumbnails(LoadUserThumbnailsCallBack callBack) {
        mUserThumbnailsListenerRegistration = mUserThumbnailsColRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //List<UserThumbnail> userThumbnails = value.toObjects(UserThumbnail.class);
                mUserThumbnails = value.toObjects(UserThumbnail.class);
                if (mUserThumbnails.isEmpty()) {
                    callBack.onDataNotAvailable();
                } else {
                    //callBack.onUserThumbnailsLoaded(userThumbnails);
                    processUrlToFile(0, callBack);
                }
            }
        });
    }

    @Override
    public void getUserThumbnail(String uid, GetUserThumbnailsCallBack callBack) {
        mUserThumbnailsColRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserThumbnail userThumbnail = documentSnapshot.toObject(UserThumbnail.class);

                if (!userThumbnail.getPhotoUrl().equals("")) {
                    processUrlToFile(userThumbnail, new GetUserThumbnailsCallBack() {
                        @Override
                        public void onUserThumbnailLoaded(UserThumbnail userThumbnail) {
                            callBack.onUserThumbnailLoaded(userThumbnail);
                        }

                        @Override
                        public void onDataNotAvailable() {

                        }
                    });
                } else {
                    callBack.onUserThumbnailLoaded(userThumbnail);
                }

            }
        });
    }

    @Override
    public void stopLoadingUserThumbnails() {
        if (mUserThumbnailsListenerRegistration != null) {
            mUserThumbnailsListenerRegistration.remove();
        }
    }

    private void processUrlToFile(UserThumbnail userThumbnail, GetUserThumbnailsCallBack callBack) {
        try {
            File file = File.createTempFile("images", "jpg");

            mUserThumbnailPhotosStorageRef.child(userThumbnail.getUid()).child(userThumbnail.getPhotoUrl() + ".jpg").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("ThumbnailsUTF", file.toString());

                    userThumbnail.setPhotoUrl(file.getPath());
                    callBack.onUserThumbnailLoaded(userThumbnail);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processUrlToFile(int i, LoadUserThumbnailsCallBack callBack) {
        if (i == mUserThumbnails.size()) {
            callBack.onUserThumbnailsLoaded(mUserThumbnails);
            return;
        }

        try {
            File file = File.createTempFile("image", "jpg");

            mUserThumbnailPhotosStorageRef.child(mUserThumbnails.get(i).getUid())
                    .child(mUserThumbnails.get(i).getPhotoUrl()+ ".jpg").getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("ThumbnailsUTF", file.getPath());

                            mUserThumbnails.get(i).setPhotoUrl(file.getPath());
                            processUrlToFile(i+1, callBack);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("SDF", e.getMessage());
                        }
                    });

            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}