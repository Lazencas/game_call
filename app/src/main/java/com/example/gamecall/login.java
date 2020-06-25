package com.example.gamecall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login extends AppCompatActivity {
    ProgressBar prog;
    private FirebaseAuth mAuth;
    String aemail="";
    String email;
    EditText idemail;
    EditText idvalue, pwvalue;
    InputMethodManager imm;
    int autoauto =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

       // getSupportActionBar().setTitle("ACTIONBAR");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));








        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
         int autologin = sharedPref.getInt("auto", 0);
        if(autologin ==1){
            Intent intent = new Intent(login.this,
                    TabActivity.class);
            Toast.makeText(login.this, "자동로그인되었습니다!",
                    Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
             String tempo  = sharedPref.getString("tempoid", "");
             if(tempo.length()>4){
                 EditText tempoid = (EditText)findViewById(R.id.idvalue);
                 tempoid.setText(tempo);

                 SharedPreferences.Editor editor = sharedPref.edit();
                 editor.remove("tempoid");

                 editor.commit();
             }


         imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
 prog = (ProgressBar)findViewById(R.id.prog);

    //    Intent intent = getIntent();
     //   aemail = intent.getExtras().getString("eemail");
       //    idemail =(EditText)findViewById(R.id.idvalue);
        //   idemail.setText(aemail);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
      idvalue = (EditText)findViewById(R.id.idvalue);
      pwvalue = (EditText)findViewById(R.id.loginpwvalue);

        Button loginbtn = (Button)findViewById(R.id.loginenterBTN);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //키보드제어
               // imm.hideSoftInputFromWindow(idvalue.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pwvalue.getWindowToken(), 0);

                prog.setVisibility(View.VISIBLE);
                login();
            }
        });

        Button loginexit = (Button)findViewById(R.id.loginexit);
        loginexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });


        TextView signupp = (TextView) findViewById(R.id.signupbtn);
        signupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,
                        newaccount.class);
                startActivity(intent);
            }
        });


        TextView findidpw = (TextView) findViewById(R.id.findidpwBTN);
        findidpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,
                        findidpw.class);
                startActivity(intent);
            }
        });




    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }


    //액션바 숨기기
    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
    }
    //액션버튼을 클릭했을때의 동작

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == android.R.id.home) {
            ActivityCompat.finishAffinity(login.this);
            System.exit(0);
            Toast.makeText(this, "앱을 종료합니다.", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }






    @Override public void onBackPressed() {

        moveTaskToBack(true);
        finishAndRemoveTask();
        android.os.Process.killProcess(android.os.Process.myPid());


    }



public void login(){
    final String email = ((EditText)findViewById(R.id.idvalue)).getText().toString();
    final String password = ((EditText)findViewById(R.id.loginpwvalue)).getText().toString();


    if(email.length() > 0 && password.length() >0){



            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            prog.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                              // Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);

                                final EditText idtext = (EditText)findViewById(R.id.idvalue);
                                final EditText pwtext = (EditText)findViewById(R.id.loginpwvalue);
                                String name = idtext.getText().toString();

                                SharedPreferences sharedPref = getSharedPreferences("shared",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("email", email);
                                editor.putString("pw", password);
                                editor.commit();

                                CheckBox checkBox = (CheckBox) findViewById(R.id.autologin) ;

                                        if (checkBox.isChecked()) {
                                            // TODO : CheckBox is checked.
                                            autoauto =1;
                                            editor.remove("auto");
                                            editor.putInt("auto", autoauto);
                                            editor.commit();

                                        } else {
                                            // TODO : CheckBox is unchecked.
                                            autoauto=0;
                                            editor.remove("auto");
                                            editor.putInt("auto", autoauto);
                                            editor.commit();
                                        }




                                Intent intent = new Intent(login.this,
                                        TabActivity.class);

                                intent.putExtra("name", name);

                                //idtext.setText("");
                                pwtext.setText("");
                                startActivity(intent);
                                Toast.makeText(login.this, "로그인 성공!",
                                        Toast.LENGTH_LONG).show();

                            } else {


                                prog.setVisibility(View.GONE);
                                        Toast.makeText(login.this, "이메일이나 비밀번호가 일치하지 않습니다.",
                                Toast.LENGTH_LONG).show();

                            }

                            // ...
                        }
                    });

    }else {
        prog.setVisibility(View.GONE);
        Toast.makeText(login.this, "이메일과 비밀번호를 입력하세요!",
                Toast.LENGTH_SHORT).show();
    }




}



}
