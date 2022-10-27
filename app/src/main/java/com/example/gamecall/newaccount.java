package com.example.gamecall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class newaccount extends AppCompatActivity {
    private TextView textView = null;
    private TextView message = null;
    EditText emailText = null;
    InputMethodManager imm;
    ProgressBar prog;
    FirebaseDatabase database;
    //이메일인증여부
public int kire = 1;

    String user = "dkfkskgks@gmail.com"; // 보내는 계정의 id
    String password = "sepwqnbpqwtehfvj"; // 보내는 계정의 pw
    GMailSender gMailSender = new GMailSender(user, password);

    private FirebaseAuth mAuth;
    private  static final String TAG="newaccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newaccount);
        database = FirebaseDatabase.getInstance();
        prog = (ProgressBar)findViewById(R.id.prognew);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());





        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

       textView = (TextView) findViewById(R.id.emailvalue); //받는 사람의 이메일
      //  TextView email = (TextView)findViewById(R.id.emailvalue);

        emailText = (EditText) findViewById(R.id.emailvalue);

        Button send = (Button) this.findViewById(R.id.sendemailBTN);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // TODO Auto-generated method stub
//메일보내기
                 EditText e1 = (EditText)findViewById(R.id.pwvaluecheck);
                imm.hideSoftInputFromWindow(e1.getWindowToken(), 0);
             //   SendMail mailServer = new SendMail();
                sendSecurityCode(getApplicationContext(), emailText.getText().toString());


                }

        });







        Button signupcancel = (Button)findViewById(R.id.signupcancelBTN);
        signupcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final Button signup = (Button)findViewById(R.id.signupBTN);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 관련 동작
                prog.setVisibility(View.VISIBLE);
                newaccount();


            }
        });

       final Button authcheck = (Button)findViewById(R.id.authcheckBTN);
        authcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //인증번호체크

                EditText authnum = (EditText)findViewById(R.id.emailauth);
                String auth = authnum.getText().toString();


                if(gMailSender.getEmailCode().equals(auth)){
                    //인증코드 같을때 로직
                    Toast.makeText(newaccount.this, "이메일인증에 성공하였습니다! 가입을 계속 진행하세요.",
                            Toast.LENGTH_LONG).show();
                    authcheck.setText("인증됨");
                    kire=1;
                  //  signup.setEnabled(true);

                }else{
                    //다를때로직
                    Toast.makeText(newaccount.this, "이메일인증에 실패하였습니다. 인증코드를 다시확인하거나 메일을 다시보내보세요!",
                            Toast.LENGTH_LONG).show();


                }


            }
        });




    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //  updateUI(currentUser);
    }



    private void newaccount() {
     final String email = ((EditText)findViewById(R.id.emailvalue)).getText().toString();
        String password = ((EditText)findViewById(R.id.pwvalue)).getText().toString();
        String passwordcheck = ((EditText)findViewById(R.id.pwvaluecheck)).getText().toString();

        if(email.length() > 0 && password.length() >0 && passwordcheck.length() > 0){


            if(password.equals(passwordcheck)) {

                if (kire == 1) {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    prog.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //성공했을때
                                        user.updateEmail(email)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
//                                                            Log.d(TAG, "User email address updated.");
                                                        }
                                                    }
                                                });


                                        SharedPreferences sharedPref = getSharedPreferences("shared",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("tempoid", email);
                                        editor.commit();


                                        String emailsend = ((EditText)findViewById(R.id.emailvalue)).getText().toString();
                                        DatabaseReference myRef = database.getReference("users").child(user.getUid());
//                                        DatabaseReference myRef = database.getReference("users");

                                        Hashtable<String, String> numbers
                                                = new Hashtable<String, String>();
                                        numbers.put("email", user.getEmail());
                                        myRef.setValue(numbers);

                                       Intent intent = new Intent(newaccount.this, login.class);

                                        intent.putExtra("eemail", emailsend);

                                        Toast.makeText(newaccount.this, "회원가입 성공!",
                                                Toast.LENGTH_SHORT).show();

                                        startActivity(intent);




                                    } else {
                                        //  if(task.getException()!=null){

                                        // Toast.makeText(newaccount.this, task.getException().toString(),
                                        //           Toast.LENGTH_LONG).show();
                                        //   Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                        if (task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.")) {
                                            prog.setVisibility(View.GONE);
                                            Toast.makeText(newaccount.this, "이메일 형식이 올바르지 않습니다.",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                        if (task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")) {
                                            prog.setVisibility(View.GONE);
                                            Toast.makeText(newaccount.this, "이미 가입된 이메일입니다.",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                        if (task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]")) {
                                            prog.setVisibility(View.GONE);
                                            Toast.makeText(newaccount.this, "비밀번호 자리수를 확인하십시오.",
                                                    Toast.LENGTH_LONG).show();
                                        }


                                        // }

                                        // If sign in fails, display a message to the user.
                                        //    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        //         Toast.makeText(newaccount.this, "회원가입 실패! 이메일형식이나 인터넷환경을 확인하세요!",
                                        //               Toast.LENGTH_SHORT).show();
                                        //실패했을때


                                    }

                                    // ...
                                }
                            });
                }
                  else{
                    prog.setVisibility(View.GONE);
                    Toast.makeText(newaccount.this, "이메일 인증완료 후에 가입을 진행하십시오.", Toast.LENGTH_SHORT).show();
                }


                } else {
                prog.setVisibility(View.GONE);
                    Toast.makeText(newaccount.this, "비밀번호가 서로 다릅니다!", Toast.LENGTH_SHORT).show();
                }




        }else {
            prog.setVisibility(View.GONE);
            Toast.makeText(newaccount.this, "이메일과 비밀번호를 입력하세요!",
                               Toast.LENGTH_SHORT).show();
        }




    }

    public void sendSecurityCode(Context context, String sendTo) {
        try {


            gMailSender.sendMail("[GAME CALL]회원가입 인증 메일입니다.", "안녕하세요! GAME CALL 운영진입니다." +
                    "요청하신 GAME CALL 회원가입의 인증코드는 다음과 같습니다. \n  "+gMailSender.getEmailCode()+"\n본인이 요청한 것이 아니라면 고객센터로 문의주십시오. 감사합니다! \n -GAME CALL-", sendTo);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다. 입력하신 이메일로 접속하여 인증코드를 확인하십시오!", Toast.LENGTH_LONG).show();
        } catch (SendFailedException e) {

            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_LONG).show();

        } catch (MessagingException e) {

            Toast.makeText(context, "인터넷 연결을 확인하거나 이메일을 확인하십시오", Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }



}
