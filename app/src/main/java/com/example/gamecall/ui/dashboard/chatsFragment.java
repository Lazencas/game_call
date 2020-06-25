package com.example.gamecall.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gamecall.MainActivity;
import com.example.gamecall.R;

public class chatsFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
    //    dashboardViewModel =
      //          ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.chats, container, false);
      /*  final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        //테스트대화창넘어가기
        Button talk = root.findViewById(R.id.talkBTN);
        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        com.example.gamecall.talk.class);
               // intent.putExtra("email", name);
                startActivity(intent);
            }
        });



        return root;
    }
}