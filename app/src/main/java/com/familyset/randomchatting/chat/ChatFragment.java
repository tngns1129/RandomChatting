package com.familyset.randomchatting.chat;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter mPresenter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private EditText editText;
    private Button sendBtn;
    private Button rematchBtn;
    private Button fileSendBtn;

    private MessagesAdapter mAdapter;
    private MessagesAdapter.MessageItemListener mItemListener;

    String uid = "asdfs";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MessagesAdapter(new ArrayList<Message>(0), mItemListener);
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
                mPresenter.saveMessage(uid, editText.getText().toString());
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
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMessages(List<Message> messages) {
        mAdapter.replaceData(messages);
    }

    public void setPresenter(@NonNull ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private static class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Message> mMessages;
        private MessageItemListener mItemListener;

        public MessagesAdapter(List<Message> messages, MessageItemListener itemListener) {
            setList(messages);
            mItemListener = itemListener;
        }

        public void replaceData(List<Message> messages) {
            setList(messages);
            notifyDataSetChanged();
        }

        private void setList(List<Message> messages) {
            mMessages = messages;
        }

        private static class MessageViewHolder extends RecyclerView.ViewHolder {
            ImageView userPhotoView;
            TextView nameView;
            TextView msgView;
            TextView readCounterView;
            TextView timestampView;

            public MessageViewHolder(@NonNull View itemView) {
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
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
            final Message message = mMessages.get(position);

            if (messageViewHolder.nameView != null) {
                messageViewHolder.nameView.setText(message.getUid());
            }
            messageViewHolder.msgView.setText(message.getMsg());
        }

        @Override
        public int getItemViewType(int position) {
            Message message = mMessages.get(position);

            if (message.getUid().equals("asdfs")) {
                return R.layout.item_chat_msg_right;
            } else {
                return R.layout.item_chat_msg_left;
            }
        }

        @Override
        public int getItemCount() {
            return mMessages.size();
        }

        public interface MessageItemListener {

        }
    }
}
