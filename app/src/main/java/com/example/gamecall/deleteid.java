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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class deleteid extends AppCompatActivity {
    String TAG ="check";
    EditText nowpw;
   private FirebaseAuth mAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteid);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        Intent intent = getIntent();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String usname = user.getDisplayName();
            String usemail = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            String usuid = user.getUid();
            // Log.e(tag, usname);
         //   Toast.makeText(deleteid.this, usuid,
          //          Toast.LENGTH_LONG).show();
        } else {
            // No user is signed in
           // Toast.makeText(deleteid.this, "제발 이거는 안떳으면 좋겠다.",
          //          Toast.LENGTH_LONG).show();

         //   Log.e(TAG, "이거되눈고맞누? 뜨면 안되는건데!");
        }




        Button deleteideBTN = (Button) findViewById(R.id.deleteidBTN);
        deleteideBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              revoke();


            }
        });



        Button deleteidcancel = (Button) findViewById(R.id.deleteidcancel);
        deleteidcancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
       finish();
        }
    });

}

public void revoke() {
    String nowpw = ((EditText) findViewById(R.id.rnowpw)).getText().toString();
    SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
    String rnowpw = sharedPref.getString("pw", "");


    if (nowpw.equals(rnowpw)) {
        //아이디삭제
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

      //  FirebaseAuth.getInstance().signOut();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "User account deleted.");
                            Toast.makeText(deleteid.this, "계정삭제가 성공했습니다!",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(deleteid.this,
                                    login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                        }
                    }
                });

    }else {
        Toast.makeText(deleteid.this, "현재 비밀번호가 올바르지 않습니다.",
                Toast.LENGTH_LONG).show();
    }

}


}
