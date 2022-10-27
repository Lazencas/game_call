package com.example.gamecall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

public class otherprofile extends AppCompatActivity {
    FirebaseDatabase database;
    ImageView otherphoto;
    File localFile;
    String uid;
    private StorageReference mStorageRef;
    TextView otheremail, othername, othergame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherprofile);
        getSupportActionBar().setTitle("프로필설정");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
Intent intent = getIntent();
//        String photo = intent.getStringExtra("nowphoto");
//        Bitmap bm = stringbitmap(photo);
String email = intent.getStringExtra("nowemail");
        String name = intent.getStringExtra("nowname");
        String game = intent.getStringExtra("nowgame");
        uid=intent.getStringExtra("nowuid");


        //xml파일이랑 연결
        otherphoto= (ImageView)findViewById(R.id.otherprofileIMG);

        otheremail=(TextView)findViewById(R.id.otheremail);
        otheremail.setText(email);
        othername=(TextView)findViewById(R.id.othernicname);
        othername.setText(name);
        othergame=(TextView)findViewById(R.id.othergame);
        othergame.setText(game);

        //파이어베이스에 접속해서 각 정보 가져오기.
        DatabaseReference ref = database.getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//대화하기버튼
        Button chatting = (Button)findViewById(R.id.modiBTN);
        chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), talk.class);
                intent.putExtra("destinationUID", uid);
                startActivity(intent);

            }
        });



        //스토리지
         localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");

            StorageReference riverRef = mStorageRef.child("users").child(email).child("profile.jpg");

            riverRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            // ...
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            otherphoto.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == android.R.id.home) {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
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

}
