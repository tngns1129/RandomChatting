package com.familyset.randomchatting.chat;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.familyset.randomchatting.MainActivity;
import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.familyset.randomchatting.util.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter mPresenter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private EditText editText;
    private Button sendBtn;
    private Button rematchBtn;
    private Button fileSendBtn;

    private ChatAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ChatAdapter(new HashMap<>(0), new ArrayList<Message>(0), mItemListener);
        mAdapter.setUid(PreferenceManager.getString(getContext(), "UID"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container, false);

        // set up messages view
        recyclerView = view.findViewById(R.id.chat_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        // set up chat view
        editText = view.findViewById(R.id.chat_edit_text_msg);
        sendBtn = view.findViewById(R.id.chat_btn_send);
        rematchBtn = view.findViewById(R.id.chat_btn_rematch);
        fileSendBtn = view.findViewById(R.id.chat_btn_file_send);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveMessage(editText.getText().toString());
            }
        });

        rematchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.rematching();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMessages(List<Message> messages) {
        mAdapter.replaceMessagesData(messages);
    }

    @Override
    public void showUserThumbnail(UserThumbnail userThumbnail, int position) {
        mAdapter.replaceUserThumbnailData(userThumbnail, position);
    }

    @Override
    public void showUserThumbnails(Map<String, UserThumbnail> userThumbnails) {

    }

    @Override
    public void clearEditText() {
        editText.setText("");
    }

    @Override
    public void showMatchingDialog() {
        ((MainActivity)getActivity()).showMatchingFragment();
    }

    public void setPresenter(@NonNull ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    ChatAdapter.ChatItemListener mItemListener = new ChatAdapter.ChatItemListener() {
        @Override
        public void getUserThumbnail(String uid, int position) {
            mPresenter.getUserThumbnail(uid, position);
        }
    };

    private static class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private String mUid;
        private Map<String, UserThumbnail> mUserThumbnails;
        private List<Message> mMessages;
        private ChatItemListener mItemListener;

        public ChatAdapter(Map<String, UserThumbnail> userThumbnails, List<Message> messages, ChatItemListener itemListener) {
            setUsersMap(userThumbnails);
            setMessagesList(messages);
            mItemListener = itemListener;
        }

        public void replaceUserThumbnailData(UserThumbnail userThumbnail, int position) {
            putUserThumbnail(userThumbnail);
            notifyItemChanged(position);
        }

        public void replaceMessagesData(List<Message> messages) {
            setMessagesList(messages);
            notifyDataSetChanged();
        }

        public void setUid(String uid) {
            mUid = uid;
        }

        private void setMessagesList(List<Message> messages) {
            mMessages = messages;
        }

        private void setUsersMap(Map<String, UserThumbnail> userThumbnails) { mUserThumbnails = userThumbnails; }

        private void putUserThumbnail(UserThumbnail userThumbnail) {
            mUserThumbnails.put(userThumbnail.getUid(), userThumbnail);
        }

        private static class ChatViewHolder extends RecyclerView.ViewHolder {
            ImageView userPhotoView;
            TextView nameView;
            TextView msgView;
            TextView readCounterView;
            TextView timestampView;

            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);

                userPhotoView = itemView.findViewById(R.id.chat_msg_image_view_user_photo);
                nameView = itemView.findViewById(R.id.chat_msg_text_view_user_name);
                msgView = itemView.findViewById(R.id.chat_msg_text_view_msg);
                readCounterView = itemView.findViewById(R.id.chat_msg_text_view_read_counter);
                timestampView = itemView.findViewById(R.id.chat_msg_text_view_timestamp);

                if (userPhotoView != null) {
                    userPhotoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }

            public View getView() {
                return itemView;
            }
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final ChatViewHolder chatViewHolder = (ChatViewHolder) holder;
            final Message message = mMessages.get(position);

            if (chatViewHolder.nameView != null) {
                String uid = message.getUid();

                // TODO map이 null일 때
                if (mUserThumbnails.get(uid) != null) {
                    chatViewHolder.nameView.setText(mUserThumbnails.get(uid).getNickname());

                    Glide.with(chatViewHolder.getView())
                            .asBitmap()
                            .load(BitmapFactory.decodeFile(mUserThumbnails.get(uid).getPhotoUrl()))
                            .into(chatViewHolder.userPhotoView);
                } else {
                    mItemListener.getUserThumbnail(uid, position);
                }
            }
            chatViewHolder.msgView.setText(message.getMsg());
        }

        @Override
        public int getItemViewType(int position) {
            Message message = mMessages.get(position);

            if (message.getUid().equals(mUid)) {
                return R.layout.item_chat_msg_right;
            } else {
                return R.layout.item_chat_msg_left;
            }
        }

        @Override
        public int getItemCount() {
            return mMessages.size();
        }

        public interface ChatItemListener {
            void getUserThumbnail(String uid, int position);
        }
    }
}
