package com.familyset.randomchatting.chat;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
import com.bumptech.glide.request.RequestOptions;
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
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private EditText editText;
    private Button sendBtn;
    private Button rematchBtn;
    private Button fileSendBtn;

    private ChatAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ChatAdapter(mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container, false);

        // set up messages view
        mRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

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

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.scrollBy(0, oldBottom - bottom + editText.getHeight());
                        }
                    });
                }
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
    public void showMessages() {
        mAdapter.notifyDataSetChanged();

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("asdf2", "sdf");

                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                mRecyclerView.scrollBy(0, editText.getHeight());
            }
        }, 100);
    }

    @Override
    public void showUserThumbnail(int position) {
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void showUserThumbnails() {
        mAdapter.notifyDataSetChanged();


        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("asdf2", "sdf");

                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                mRecyclerView.scrollBy(0, editText.getHeight());
            }
        }, 100);
    }

    @Override
    public void clearEditText() {
        if (editText.getText().length() > 0) {
            editText.getText().clear();
        }
    }

    @Override
    public void showMatchingFragment() {
        ((MainActivity)getActivity()).showMatchingFragment();
    }

    public void setPresenter(@NonNull ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    ChatAdapter.ChatItemListener mItemListener = new ChatAdapter.ChatItemListener() {

    };

    private static class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        private ChatContract.Presenter mPresenter;

        public ChatAdapter(ChatContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            mPresenter.onBindViewHolder(position, holder);
        }

        @Override
        public int getItemViewType(int position) {
            if (mPresenter.getItemViewType(position)) {
                return R.layout.item_chat_msg_right;
            } else {
                return R.layout.item_chat_msg_left;
            }
        }

        @Override
        public int getItemCount() {
            return mPresenter.getItemCount();
        }

        private static class ChatViewHolder extends RecyclerView.ViewHolder implements ChatContract.RecyclerRowView {
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

            @Override
            public void setUserThumbnail(UserThumbnail userThumbnail) {
                nameView.setText(userThumbnail.getNickname());

                if (userThumbnail.getPhotoUrl().equals("")) {
                    Glide.with(itemView)
                            .load(R.drawable.dog2_1)
                            .circleCrop()
                            .apply(new RequestOptions().override(userPhotoView.getWidth(), userPhotoView.getHeight()))
                            .into(userPhotoView);
                } else {
                    Glide.with(itemView)
                            .asBitmap()
                            .circleCrop()
                            .apply(new RequestOptions().override(userPhotoView.getWidth(), userPhotoView.getHeight()))
                            .load(BitmapFactory.decodeFile(userThumbnail.getPhotoUrl()))
                            .into(userPhotoView);
                }

            }

            @Override
            public void setMsg(String msg) {
                msgView.setText(msg);
            }
        }

        public interface ChatItemListener {
        }
    }
}
