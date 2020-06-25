package com.example.gamecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class option extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    String TAG ="한글이다!";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);


        Intent intent = getIntent();
        email=getIntent().getStringExtra("email");
     //   Toast.makeText(option.this, email,
     //           Toast.LENGTH_SHORT).show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is signed in
            String usname = user.getDisplayName();
            String usemail = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            String usuid = user.getUid();
            // Log.e(tag, usname);
          //  Toast.makeText(option.this, usuid,
           //         Toast.LENGTH_LONG).show();
        } else {
            // No user is signed in
        //    Toast.makeText(option.this, "제발 이거는 안떳으면 좋겠다.",
          //          Toast.LENGTH_LONG).show();

       //     Log.e(TAG, "이거되눈고맞누? 뜨면 안되는건데!");
        }

        Button profileenter = (Button)findViewById(R.id.profileenterBTN);
        profileenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(option.this,
                        profile.class);
                startActivity(intent);

            }
        });

        //알림설정창넘어가기

        Button noti = (Button)findViewById(R.id.notiBTN);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(option.this,
                        notifyoption.class);
                startActivity(intent);

            }
        });


//        Button optioncancel = (Button) findViewById(R.id.canceloptionBTN);
//        optioncancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               finish();
//            }
//        });

//계정삭제
        Button deleteidenter = (Button) findViewById(R.id.deleteid);
        deleteidenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(option.this,
                        deleteid.class);
                startActivity(intent);

            }
        });

//비밀번호변경창넘어가기
        Button pwchangeenter = (Button) findViewById(R.id.pwchangeenter);
        pwchangeenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(option.this,
                        changepw.class);
                intent.putExtra("email", email);
                startActivity(intent);

            }
        });

//로그아웃
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("auto");

                editor.commit();
                CheckBox checkBox = (CheckBox) findViewById(R.id.autologin) ;

                if (checkBox.isChecked()) {
                    // TODO : CheckBox is checked.

                    editor.remove("auto");
                    editor.putInt("auto", 0);
                    editor.commit();

                } else {
                    // TODO : CheckBox is unchecked.

                    editor.remove("auto");
                    editor.putInt("auto", 0);
                    editor.commit();
                }

                FirebaseAuth.getInstance().signOut();

               Intent intent = new Intent(option.this,
                        login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

        Button exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  FirebaseAuth.getInstance().signOut();
                moveTaskToBack(true);
                ActivityCompat.finishAffinity(option.this);
                System.exit(0);
//                moveTaskToBack(false);
//                finishAndRemoveTask();
//                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });


    }
}