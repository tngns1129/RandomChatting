package com.familyset.randomchatting.data.randomRoom;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
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
    private UserThumbnail mUserThumbnail;

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
    public void searchEmptyRoom(UserThumbnail userThumbnail, SearchCallBack callBack) {
        mUserThumbnail = userThumbnail;

        // TODO 블랙리스트 확인
        // 사용자 uid도 블랙리스트에 넣어서 whereNotIn으로 확인
        randomRoomsColRef.whereEqualTo("isMatched", 0).orderBy("createdTimestamp")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                roomsList = queryDocumentSnapshots.getDocuments();
                Log.d(LOG_TAG, roomsList.size() + "개의 방 검색 완료");

                searchEmptyRoom(0, callBack);
            }
        });
    }

    // 트랜잭션을 이용한 방탐색
    private void searchEmptyRoom(int i, SearchCallBack callBack) {
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
        }

        db.runTransaction(new Transaction.Function<String>() {
            @Nullable
            @Override
            public String apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot roomSnapshot = transaction.get(roomsList.get(i).getReference());

                Map<String, Integer> users = (Map<String, Integer>) roomSnapshot.get("users");

                if (users.size() < 2) {
                    // 유저목록에 추가
                    users.put(mUserThumbnail.getUid(), 0);
                    transaction.update(roomSnapshot.getReference(), "users", users, "isMatched", 1);
                    transaction.set(roomSnapshot.getReference().collection("userThumbnail").document(mUserThumbnail.getUid()), mUserThumbnail);
                    return roomSnapshot.getString("id");
                }

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String id) {
                // 빈방 탐색 완료
                if (id != null) {
                    Log.d(LOG_TAG, "매칭 성공");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSearchFinished(id);
                        }
                    });

                    roomsList.clear();
                    mUserThumbnail = null;
                } else {
                    searchEmptyRoom(i + 1, callBack);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            }
        });
    }

    @Override
    public void createRoom(UserThumbnail userThumbnail, CreateCallBack callBack) {
        randomRoomsColRef.document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) return;

                DocumentSnapshot roomSnapshot = task.getResult();

                WriteBatch batch = db.batch();
                RandomRoom room = new RandomRoom();
                room.setId(roomSnapshot.getId());
                room.setUsers(new HashMap<String, Integer>() {{put(userThumbnail.getUid(), 0);}});
                room.setCreatedTimestamp(Timestamp.now());

                batch.set(roomSnapshot.getReference(), room);

                batch.set(roomSnapshot.getReference().collection("userThumbnails").document(userThumbnail.getUid()), userThumbnail);

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

    @Override
    public void quitRoom(String uid, String rid) {
        randomRoomsColRef.document(rid).update("users." + uid, FieldValue.delete()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LOG_TAG, uid + " quit. room:" + rid);
            }
        });
    }

    private void removeMatchedListener() {
        if (matchedListenRegistration != null) {
            matchedListenRegistration.remove();
        }
    }
}
