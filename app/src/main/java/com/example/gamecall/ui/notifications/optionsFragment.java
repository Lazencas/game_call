package com.example.gamecall.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gamecall.R;
import com.example.gamecall.changepw;
import com.example.gamecall.deleteid;
import com.example.gamecall.login;
import com.example.gamecall.notifyoption;
import com.example.gamecall.option;
import com.example.gamecall.profile;
import com.google.firebase.auth.FirebaseAuth;

public class optionsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // notificationsViewModel =
         //       ViewModelProviders.of(this).get(NotificationsViewModel.class);
        final View root = inflater.inflate(R.layout.option, container, false);
       /*final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
//프로필
        Button profileenter =  root.findViewById(R.id.profileenterBTN);
        profileenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        profile.class);
                startActivity(intent);

            }
        });

        //알림설정창넘어가기

        Button noti = root.findViewById(R.id.notiBTN);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        notifyoption.class);
                startActivity(intent);

            }
        });


//계정삭제
        Button deleteidenter = root.findViewById(R.id.deleteid);
        deleteidenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),
                        deleteid.class);
                startActivity(intent);

            }
        });






//비밀번호변경
        Button pwchangeenter = root.findViewById(R.id.pwchangeenter);
        pwchangeenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),
                        changepw.class);
                startActivity(intent);

            }
        });

        //로그아웃
        Button logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("auto");

                editor.commit();
//                CheckBox checkBox = root.findViewById(R.id.autologin) ;
//
//                if (checkBox.isChecked()) {
//                    // TODO : CheckBox is checked.
//
//                    editor.remove("auto");
//                    editor.putInt("auto", 0);
//                    editor.commit();
//
//                } else {
//                    // TODO : CheckBox is unchecked.
//
//                    editor.remove("auto");
//                    editor.putInt("auto", 0);
//                    editor.commit();
//                }


                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),
                        login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
//종료
        Button exit = root.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
              //  android.os.Process.killProcess(android.os.Process.myPid());
                ActivityCompat.finishAffinity(getActivity());
                System.exit(0);
            }
        });


        return root;
    }
}