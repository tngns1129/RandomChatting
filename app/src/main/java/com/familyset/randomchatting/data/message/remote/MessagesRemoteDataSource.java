package com.familyset.randomchatting.data.message.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesDataSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MessagesRemoteDataSource implements MessagesDataSource {
    private static MessagesRemoteDataSource INSTANCE;
    private CollectionReference messagesColRef;

    private final static Map<String, Message> MESSAGES_REMOTE_DATA = new LinkedHashMap<>();

    public static MessagesRemoteDataSource getInstance(String collectionRef, String documentRef) {
        if (INSTANCE == null) {
            INSTANCE = new MessagesRemoteDataSource(collectionRef, documentRef);
        }

        return INSTANCE;
    }

    private MessagesRemoteDataSource(String collectionRef, String documentRef) {
        messagesColRef = FirebaseFirestore.getInstance().collection(collectionRef).document(documentRef).collection("messages");
    }

    @Override
    public void getMessages(LoadMessagesCallBack callBack) {
        messagesColRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Message> messages = value.toObjects(Message.class);
                if (messages.isEmpty()) {
                    callBack.onDataNotAvailable();
                } else {
                    callBack.onMessagesLoaded(messages);
                }
            }
        });
    }

    @Override
    public void getMessage(String id, GetMessagesCallBack callBack) {
        messagesColRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Message message = documentSnapshot.toObject(Message.class);
                callBack.onMessageLoaded(message);
            }
        });
    }

    @Override
    public void saveMessage(@NonNull Message message) {
        messagesColRef.document(message.getId()).set(message);
    }
}
