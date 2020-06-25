package com.example.gamecall;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changepw extends AppCompatActivity {
    String TAG ="check";
    EditText nowpw;
String email;

    private FirebaseAuth mAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepw);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));

        email=getIntent().getStringExtra("email");

      //  Toast.makeText(changepw.this, email,
       //         Toast.LENGTH_SHORT).show();



        Button changepw = (Button) findViewById(R.id.changepwBTN);
        changepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               change();

            }
        });



        Button changepwcancel = (Button) findViewById(R.id.changecancelBTN);
        changepwcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

public void change(){
   String nowpw = ((EditText)findViewById(R.id.nowpw)).getText().toString();
   String newpw = ((EditText)findViewById(R.id.newpw)).getText().toString();
    String newpwcheck = ((EditText)findViewById(R.id.newpwcheck)).getText().toString();
    SharedPreferences sharedPref = getSharedPreferences("shared",Context.MODE_PRIVATE);
    String rnowpw = sharedPref.getString("pw","");
    Log.d(TAG, "비밀번호잘오냐? : "+rnowpw);


        if(newpw.length()>0 && newpwcheck.length()>0 ){

            if(newpw.equals(newpwcheck)){

                if(nowpw.equals(rnowpw)) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String newPassword = newpw;


                    //여기는 비밀번호변경하는거

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                        Toast.makeText(changepw.this, "비밀번호변경 성공!",
                                                Toast.LENGTH_SHORT).show();
                                        EditText nowpw = (EditText) findViewById(R.id.nowpw);
                                        EditText newpw = (EditText) findViewById(R.id.newpw);
                                        EditText newpwcheck = (EditText) findViewById(R.id.newpwcheck);
                                        nowpw.setText("");
                                        newpw.setText("");
                                        newpwcheck.setText("");
                                        finish();
                                    } else {
                                        Toast.makeText(changepw.this, "비밀번호가 6자리 이상인지 확인하세요.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(changepw.this, "현재 비밀번호가 올바르지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(changepw.this, "비밀번호가 서로 맞지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }



        }else{
            Toast.makeText(changepw.this, "비밀번호를 모두 입력하세요.",
                    Toast.LENGTH_SHORT).show();
        }






}




}
