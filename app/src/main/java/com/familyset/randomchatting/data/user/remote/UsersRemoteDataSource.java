package com.familyset.randomchatting.data.user.remote;

import androidx.annotation.Nullable;

import com.familyset.randomchatting.data.user.UsersDataSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class UsersRemoteDataSource implements UsersDataSource {
    private static UsersRemoteDataSource INSTANCE;
    private CollectionReference usersColRef;

    public static UsersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRemoteDataSource();
        }

        return INSTANCE;
    }

    private UsersRemoteDataSource() {
        usersColRef = FirebaseFirestore.getInstance().collection("users");
    }

}
