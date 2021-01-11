package com.familyset.randomchatting.data.randomRoom;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomRoomsRepository implements RandomRoomsDataSource {
    private static final String LOG_TAG = "RANDROOMSREPO";
    private FirebaseFirestore db;
    private CollectionReference randomRoomsColRef;

    private ListenerRegistration matchedListenRegistration;
    private List<DocumentSnapshot> roomsList;

    public RandomRoomsRepository() {
        db = FirebaseFirestore.getInstance();
        randomRoomsColRef = db.collection("randomRooms");
    }

    public void enterDevRoom(String uid, SearchCallBack callBack) {
        randomRoomsColRef.document("devRoom").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Integer> users = (Map<String, Integer>) documentSnapshot.get("users");
                users.put(uid, 0);
                documentSnapshot.getReference().update("users", users);
                callBack.onSearchFinished(documentSnapshot.getId());
            }
        });
    }

    @Override
    public void searchEmptyRoom(String uid, SearchCallBack callBack) {
        // TODO 블랙리스트 확인
        // 사용자 uid도 블랙리스트에 넣어서 whereNotIn으로 확인
        randomRoomsColRef.whereEqualTo("isMatched", 0).orderBy("createdTimestamp")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                roomsList = queryDocumentSnapshots.getDocuments();
                Log.d(LOG_TAG, roomsList.size() + "개의 방 검색 완료");

                searchEmptyRoom(0, uid, callBack);
            }
        });
    }

    // 트랜잭션을 이용한 방탐색
    private void searchEmptyRoom(int i, String uid, SearchCallBack callBack) {
        if (roomsList == null) return;

        // 빈방이 없으므로 새로운 방 생성 필요
        if (i == roomsList.size()) {
            // Only the original thread that created a view hierarchy can touch its views.
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callBack.onSearchFinished(null);
                }
            });
            roomsList.clear();
            return;
        }

        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(roomsList.get(i).getReference());

                Map<String, Integer> users = (Map<String, Integer>) snapshot.get("users");

                if (users.size() >= 2) {
                    searchEmptyRoom(i+1, uid, callBack);
                } else {
                    // 유저목록에 추가
                    users.put(uid, 0);
                    transaction.update(snapshot.getReference(), "users", users, "isMatched", 1);

                    Log.d(LOG_TAG, "매칭 성공");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSearchFinished(snapshot.getString("id"));
                        }
                    });
                }
                return null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            }
        });
    }

    @Override
    public void createRoom(String uid, CreateCallBack callBack) {
        randomRoomsColRef.document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) return;

                DocumentSnapshot roomSnapshot = task.getResult();

                WriteBatch batch = db.batch();
                RandomRoom room = new RandomRoom();
                room.setId(roomSnapshot.getId());
                room.setUsers(new HashMap<String, Integer>() {{put(uid, 0);}});
                room.setCreatedTimestamp(Timestamp.now());

                batch.set(roomSnapshot.getReference(), room);

                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callBack.onCreateFinished();
                        Log.d(LOG_TAG, "방 생성 완료, 다른 사용자 대기");

                        matchedListenRegistration = roomSnapshot.getReference().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (((Map<String, Integer>)value.get("users")).size() >= 2) {
                                    Log.d(LOG_TAG, "다른 사용자 입장, 채팅 시작");
                                    callBack.onMatchFinished(room.getId());
                                    removeMatchedListener();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void removeMatchedListener() {
        if (matchedListenRegistration != null) {
            matchedListenRegistration.remove();
        }
    }
}
