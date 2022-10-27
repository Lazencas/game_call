package com.example.gamecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter kAdapter;
 ImageView mainimg ;
 Bitmap profile;
    private int count = -1;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    MyAdapter mAdapter;
String name;
    private RecyclerView.LayoutManager layoutManager;
String email;
  public  ArrayList<Chat> friendList;



    String TAG = "한글태그는처음보지?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        //유저정보가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String usname = user.getDisplayName();
            String usemail = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();


            String usuid = user.getUid();
           // Log.e(tag, usname);
           // Toast.makeText(MainActivity.this, usuid,
          //          Toast.LENGTH_LONG).show();
        } else {
            // No user is signed in
           // Toast.makeText(MainActivity.this, "제발 이거는 안떳으면 좋겠다.",
           //         Toast.LENGTH_LONG).show();

          //  Log.e(TAG, "이거되눈고맞누? 뜨면 안되는건데!");
        }

        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        String profileimg = sharedPref.getString("profileimg","");
       profile = stringbitmap(profileimg);
        mainimg = (ImageView)findViewById(R.id.mainimage);
        mainimg.setImageBitmap(profile);


//친구목록리사이클러
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        mArrayList = new ArrayList<>();

       // kAdapter = new CustomAdapter( mArrayList);
        kAdapter = new CustomAdapter(this, mArrayList, email);
        mRecyclerView.setAdapter(kAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


/*
        //친구추가창
        Button newfriendBTN = (Button) findViewById(R.id.addfri);
        newfriendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.edit_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                final EditText userphoto = (EditText) view.findViewById(R.id.userphoto);
                final EditText username = (EditText) view.findViewById(R.id.username);
                final EditText usergame = (EditText) view.findViewById(R.id.usergame);

                ButtonSubmit.setText("추가");


                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String strphoto = userphoto.getText().toString();
                        String strname = username.getText().toString();
                        String strgame = usergame.getText().toString();

                        Dictionary dict = new Dictionary(strphoto, strname, strgame );

                        mArrayList.add(0, dict); //첫 줄에 삽입
                        //mArrayList.add(dict); //마지막 줄에 삽입
                        kAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영


                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, strname+"님이 친구추가 되었습니다.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();


            }
        });

*/



//넘겨받아서 이름표시
        TextView mainname = (TextView)findViewById(R.id.mainname);
     Intent intent = getIntent();
      name = intent.getExtras().getString("name");
       mainname.setText(name+"님");
       /*
//옵션창넘어가기
        Button option = (Button) findViewById(R.id.optionBTN);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        option.class);
                intent.putExtra("email", name);
                startActivity(intent);
            }
        });

        */

        //테스트대화창넘어가기
        Button talk = (Button) findViewById(R.id.talkBTN);
        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        talk.class);
                intent.putExtra("email", name);
                startActivity(intent);
            }
        });






    }
    public Bitmap stringbitmap(String encodedString){
        try{
            byte[] encodedByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);
            return bitmap;
        }catch (Exception e){
            e.getMessage();
            return null;
        }

    }//스트링비트맵



public void delete() {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    user.delete()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User account deleted.");
                    }
                }
            });


}


}
