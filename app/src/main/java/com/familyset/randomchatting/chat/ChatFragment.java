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
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.familyset.randomchatting.R;
import com.familyset.randomchatting.data.message.MessagesRepository;

public class ChatFragment extends Fragment implements ChatContract.View {

    private ChatContract.Presenter mPresenter;
    private RecyclerView recyclerView;
    private EditText editText;
    private Button sendBtn;
    private Button rematchBtn;
    private Button fileSendBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container, false);

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        editText = view.findViewById(R.id.chat_edit_text_msg);
        sendBtn = view.findViewById(R.id.chat_btn_send);
        rematchBtn = view.findViewById(R.id.chat_btn_rematch);
        fileSendBtn = view.findViewById(R.id.chat_btn_file_send);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

}
