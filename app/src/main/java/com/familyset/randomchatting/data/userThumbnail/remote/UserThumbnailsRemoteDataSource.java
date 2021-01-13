package com.familyset.randomchatting.data.userThumbnail.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnailsDataSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserThumbnailsRemoteDataSource implements UserThumbnailsDataSource {
    private static UserThumbnailsRemoteDataSource INSTANCE = null;
    private final CollectionReference usersThumbnailsColRef;

    public static UserThumbnailsRemoteDataSource getInstance(@NonNull String colRef, @NonNull String docRef) {
        if (INSTANCE == null) {
            INSTANCE = new UserThumbnailsRemoteDataSource(colRef, docRef);
        }

        return INSTANCE;
    }

    private UserThumbnailsRemoteDataSource(@NonNull String colRef, @NonNull String docRef) {
        usersThumbnailsColRef = FirebaseFirestore.getInstance().collection(colRef).document(docRef).collection("userThumbnails");
    }

    @Override
    public void getUserThumbnails(LoadUserThumbnailsCallBack callBack) {
        usersThumbnailsColRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<UserThumbnail> userThumbnails = value.toObjects(UserThumbnail.class);
                if (userThumbnails.isEmpty()) {
                    callBack.onDataNotAvailable();
                } else {
                    callBack.onUserThumbnailsLoaded(userThumbnails);
                }
            }
        });
    }
}