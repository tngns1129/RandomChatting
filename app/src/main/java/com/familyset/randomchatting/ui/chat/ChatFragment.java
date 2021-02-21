package com.familyset.randomchatting.ui.chat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.familyset.randomchatting.data.message.MessagesType;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.familyset.randomchatting.ui.expandedImage.ExpandedImageActivity;
import com.familyset.randomchatting.util.MyUtil;

import java.io.File;

public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private EditText editText;
    private Button sendBtn;
    private Button rematchBtn;
    private Button fileSendBtn;

    private LinearLayout mFileSendLL;
    private Button mGalleryBtn;
    private Button mTakePictureBtn;
    private Button mTakeVideoBtn;

    private static final int GALLERY = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int TAKE_VIDEO = 2;

    private ChatAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ChatAdapter(mPresenter, mItemListener);
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
        mFileSendLL = view.findViewById(R.id.chat_layout_file_send);
        mGalleryBtn = view.findViewById(R.id.chat_btn_gallery);
        mTakePictureBtn = view.findViewById(R.id.chat_btn_take_picture);
        mTakeVideoBtn = view.findViewById(R.id.chat_btn_take_video);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.sendMessage(editText.getText().toString(), MessagesType.TEXT, null);
            }
        });

        rematchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.rematching();
            }
        });

        fileSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFileSendLL.getVisibility() == View.GONE) {
                    showFileSend();
                } else {
                    hideFileSend();
                }
            }
        });

        mGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGallery();
            }
        });

        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTakePicture();
            }
        });

        mTakeVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTakeVideo();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == GALLERY) {
            Uri uri = data.getData();

            Message.FileInfo fileInfo = getFileInfoFromUri(uri);

            mPresenter.sendMessage(uri.toString(), MessagesType.IMAGE, fileInfo);
        }

        if (requestCode == TAKE_PICTURE) {

        }

        if (requestCode == TAKE_VIDEO) {

        }
    }

    private Message.FileInfo getFileInfoFromUri(Uri uri) {
        if (uri == null) { return null; }

        Message.FileInfo fileInfo = new Message.FileInfo();

        // File Scheme.
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            File file = new File(uri.getPath());
            fileInfo.setFileName(file.getName());
            fileInfo.setFileSize(MyUtil.size2String(file.length()));
        }
        // Content Scheme.
        else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor returnCursor = getContext().getContentResolver()
                    .query(uri, null, null, null, null);

            if (returnCursor != null && returnCursor.moveToFirst()) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                fileInfo.setFileName(returnCursor.getString(nameIndex));
                fileInfo.setFileSize(MyUtil.size2String(returnCursor.getLong(sizeIndex)));
                returnCursor.close();
            }
        }

        return fileInfo;
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
    public void showExpandedImageUi(String imagePath, String imageUri) {
        Intent intent = new Intent(getContext(), ExpandedImageActivity.class);
        intent.putExtra(ExpandedImageActivity.EXTRA_IMAGE_PATH, imagePath);
        intent.putExtra(ExpandedImageActivity.EXTRA_IMAGE_URI, imageUri);
        startActivity(intent);
    }

    @Override
    public void showMatchingFragment() {
        ((MainActivity)getActivity()).showMatchingFragment();
    }

    public void setPresenter(@NonNull ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void showFileSend() {
        mFileSendLL.setVisibility(View.VISIBLE);
    }

    private void hideFileSend() {
        mFileSendLL.setVisibility(View.GONE);
    }

    private void showGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY);
    }

    private void showTakePicture() {

    }

    private void showTakeVideo() {

    }

    ChatItemListener mItemListener = new ChatItemListener() {

    };

    private static class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
        private ChatContract.Presenter mPresenter;
        private ChatItemListener mItemListener;

        public ChatAdapter(ChatContract.Presenter presenter, ChatItemListener itemListener) {
            mPresenter = presenter;
            mItemListener = itemListener;
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
            switch (mPresenter.getItemViewType(position)) {
                case TEXT:
                default: return R.layout.item_chat_msg_right;
                case O_TEXT: return R.layout.item_chat_msg_left;
            }
        }

        @Override
        public int getItemCount() {
            return mPresenter.getItemCount();
        }

        private class ChatViewHolder extends RecyclerView.ViewHolder implements ChatContract.RecyclerRowView {
            ImageView userPhotoView;
            TextView nameView;
            TextView msgView;
            TextView readCounterView;
            TextView timestampView;

            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);

                userPhotoView = itemView.findViewById(R.id.chat_image_view_user_photo);
                nameView = itemView.findViewById(R.id.chat_text_view_user_name);
                msgView = itemView.findViewById(R.id.chat_text_view_msg);
                readCounterView = itemView.findViewById(R.id.chat_text_view_read_counter);
                timestampView = itemView.findViewById(R.id.chat_text_view_timestamp);

                if (userPhotoView != null) {
                    userPhotoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

                if (msgView != null) {
                    msgView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.openExpandedImage(getAdapterPosition());
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
                            .into(userPhotoView);
                } else {
                    Glide.with(itemView)
                            .load(userThumbnail.getPhotoUrl())
                            .circleCrop()
                            .into(userPhotoView);
                }
            }

            @Override
            public void setMsg(String msg) {
                // TODO msg가 null일 때
                if (msg == null | msgView == null) return;
                msgView.setText(msg);
            }

            @Override
            public void setImageMsg() {
                if (msgView == null) return;
                msgView.setText(R.string.chat_image_msg);
            }
        }
    }

    public interface ChatItemListener {
    }
}
