package com.familyset.randomchatting.ui.chat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.familyset.randomchatting.ui.main.MainActivity;
import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.message.Message;
import com.familyset.randomchatting.data.message.MessagesType;
import com.familyset.randomchatting.data.userThumbnail.UserThumbnail;
import com.familyset.randomchatting.ui.expandedImage.ExpandedImageActivity;
import com.familyset.randomchatting.util.DiskIOUtil;

import java.io.File;

public class ChatFragment extends Fragment implements ChatContract.View, ChatContract.OnBackPressedListener{

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

    private int keyboardHeight = 0;
    private boolean isKeyboardShowing = false;
    private InputMethodManager imm;

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
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

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

        ((MainActivity)getActivity()).setOnBackPressedListener(this);


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFileSendLL.getVisibility() == View.VISIBLE && !isKeyboardShowing) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    hideFileSend();
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            // 시간 지난 후 실행할 코딩
                            showKeyboard();
                        }
                    }, 50);
                } else if (mFileSendLL.getVisibility() == View.GONE && !isKeyboardShowing) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getKeyboardHeight(view);
            }
        });



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
                controlFileSendContainer();
            }
        });



        mGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFileSendLL.getVisibility() == View.VISIBLE) {
                    mFileSendLL.setVisibility(View.GONE);
                }
                showGallery();
            }
        });

        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFileSendLL.getVisibility() == View.VISIBLE) {
                    mFileSendLL.setVisibility(View.GONE);
                }
                showTakePicture();
            }
        });

        mTakeVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFileSendLL.getVisibility() == View.VISIBLE) {
                    mFileSendLL.setVisibility(View.GONE);
                }
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




    private void controlFileSendContainer() {
        if(mFileSendLL.getVisibility() == View.GONE && !isKeyboardShowing) {
            showFileSend();
        }

        else if (mFileSendLL.getVisibility() == View.GONE && isKeyboardShowing) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    // 시간 지난 후 실행할 코딩
                    showFileSend();
                }
            }, 50);
            hideKeyboard();
        }

        else if (mFileSendLL.getVisibility() == View.VISIBLE && !isKeyboardShowing) {
            //if(mFileSendLL.getHeight() != keyboardHeight){
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                hideFileSend();
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        showKeyboard();
                    }
                }, 50);
            //}
            /*else {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        showKeyboard();
                    }
                }, 50);
            }

             */
        }

        else if (mFileSendLL.getVisibility() == View.VISIBLE && isKeyboardShowing) {
            hideKeyboard();
        }
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
        if(mFileSendLL.getVisibility() == View.VISIBLE)
            mFileSendLL.setVisibility(View.GONE);
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
            fileInfo.setFileSize(DiskIOUtil.size2String(file.length()));
        }
        // Content Scheme.
        else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor returnCursor = getContext().getContentResolver()
                    .query(uri, null, null, null, null);

            if (returnCursor != null && returnCursor.moveToFirst()) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                fileInfo.setFileName(returnCursor.getString(nameIndex));
                fileInfo.setFileSize(DiskIOUtil.size2String(returnCursor.getLong(sizeIndex)));
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

    private void getKeyboardHeight(View view) {
        Rect rectangle = new Rect();
        view.getWindowVisibleDisplayFrame(rectangle);
        int screenHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            if(getActivity().getWindow().getDecorView().getRootWindowInsets().getDisplayCutout() == null)
                screenHeight = displayMetrics.heightPixels; //파이이상 + not 노치
            else {
                int statusBarHeight = 0;
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                screenHeight = displayMetrics.heightPixels + statusBarHeight; //파이 이상 + 노치
            }
        } else {
            screenHeight = view.getRootView().getHeight();
        }

        int tempKeyboardSize = screenHeight - rectangle.bottom;

        if (tempKeyboardSize > screenHeight * 0.1) {
            keyboardHeight = screenHeight - rectangle.bottom;
            if (!isKeyboardShowing) {
                isKeyboardShowing = true;
            }
        } else {
        }
    }

    private void showFileSend() {
        ViewGroup.LayoutParams params = mFileSendLL.getLayoutParams();

        if(keyboardHeight != 0)
            params.height = keyboardHeight;
        mFileSendLL.setLayoutParams(params);
        mFileSendLL.setVisibility(View.VISIBLE);
    }

    private void hideFileSend() {
        mFileSendLL.setVisibility(View.GONE);
    }
    private void hideKeyboard() {
            /*getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            Handler mHandler2 = new Handler();
            mHandler2.postDelayed(new Runnable() {
                public void run() {
                    // 시간 지난 후 실행할 코딩
                    ViewGroup.LayoutParams params = layoutFileSend.getLayoutParams();
                    params.height = keyboardHeight;
                    layoutFileSend.setLayoutParams(params);
                    layoutFileSend.setVisibility(View.VISIBLE);
                }
            }, 50);



            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    // 시간 지난 후 실행할 코딩
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }, 50);

             */
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        isKeyboardShowing = false;
    }

    private void showKeyboard() {
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        isKeyboardShowing = true;
            /*Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    // 시간 지난 후 실행할 코딩
                    layoutFileSend.setVisibility(View.GONE);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }, 150);

             */

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

    @Override
    public void onBackPressed() {
        if(mFileSendLL.getVisibility()==View.VISIBLE){
            mFileSendLL.setVisibility(View.GONE);
        }
        else{
            getActivity().finish();
        }
    }

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
