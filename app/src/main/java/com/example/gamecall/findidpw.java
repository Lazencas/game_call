package com.example.gamecall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class findidpw extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private View mainLayout;

    ProgressBar prog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findidpw);
        prog = (ProgressBar)findViewById(R.id.progpw);
       mainLayout = findViewById(R.id.main_layout);


        mAuth = FirebaseAuth.getInstance();



        Button findpweBTN = (Button)findViewById(R.id.findpwBTN);
        findpweBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog.setVisibility(View.VISIBLE);

               send();
            }
        });



        Button findcancel = (Button)findViewById(R.id.findcancelBTN);
        findcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    public void send(){

        String email = ((EditText)findViewById(R.id.findpwID)).getText().toString();



        if(email.length() > 0){
            FirebaseAuth auth = FirebaseAuth.getInstance();
         //   String emailAddress = "user@example.com";

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            prog.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                               Toast.makeText(findidpw.this, "이메일로 비밀번호 재설정링크를 보냈습니다! 이메일을 확인하세요.",
                                       Toast.LENGTH_SHORT).show();
                              //  Snackbar.make(mainLayout,  "이메일로 비밀번호 재설정링크를 보냈습니다! 이메일을 확인하세요.",Snackbar.LENGTH_INDEFINITE).show();

                                EditText ID = (EditText)findViewById(R.id.findpwID);
                                ID.setText("");
                                finish();
                                //Log.d(TAG, "Email sent.");
                            }else {
                                Toast.makeText(findidpw.this, "등록되어있지 않은 이메일입니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            prog.setVisibility(View.GONE);
            Toast.makeText(findidpw.this, "이메일을 입력하세요!",
                    Toast.LENGTH_SHORT).show();

        }


    }


}
