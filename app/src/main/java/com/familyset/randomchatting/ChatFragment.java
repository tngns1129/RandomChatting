package com.familyset.randomchatting;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChatFragment extends Fragment {

    Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat,container, false);

        button = view.findViewById(R.id.menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu p = new PopupMenu(getContext(),view);
                getActivity().getMenuInflater().inflate(R.menu.optionmenu, p.getMenu());
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.itemRed:
                                Toast.makeText(getContext(),"red",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.itemBlue:
                                Toast.makeText(getContext(),"blue",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.itemGreen:
                                Toast.makeText(getContext(),"green",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                p.show();
            }
        });
        return view;
    }


}
