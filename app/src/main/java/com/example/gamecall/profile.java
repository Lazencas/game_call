package com.example.gamecall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class profile extends AppCompatActivity {
    private static final String TAG = "태그요";
    int REQUEST_IMAGE_CODE = 1001;
    int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002;
    Bitmap bitimg;
    TextView eemm;
    String email;
    ImageView img;
    File localFile;
    FirebaseDatabase database;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        database = FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle("프로필설정");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //프로필나가기버튼
        Button profileexit = (Button) findViewById(R.id.profileexit);
        profileexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (ContextCompat.checkSelfPermission(profile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(profile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(profile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

  eemm = (TextView)findViewById(R.id.nowemail);
        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
      email = sharedPref.getString("email", "");
eemm.setText(email);

        img = (ImageView)findViewById(R.id.profileIMG);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, REQUEST_IMAGE_CODE);
            }
        });



        try {
            localFile = File.createTempFile("images", "jpg");
            StorageReference riversRef = mStorageRef.child("users").child(email).child("profile.jpg");

            riversRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            // ...
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                              img.setImageBitmap(bitmap);


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

//하위처리
        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
          //      Chat chat = dataSnapshot.getValue(Chat.class);
                String commentKey = dataSnapshot.getKey();
               // String sendEmail = chat.getEmail();
               // String sendText = chat.getText();
              //  Log.d(TAG, "이메일: "+sendEmail);
               // Log.d(TAG, "텍스트: "+sendText);

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.


                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                // Comment movedComment = dataSnapshot.getValue(Comment.class);
                //  String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(profile.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };


        //하위처리
        //변경버튼
        Button modi = (Button)findViewById(R.id.modiBTN);
        modi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String namee = ((EditText)findViewById(R.id.usernicname)).getText().toString();
               String gamee = ((EditText)findViewById(R.id.usergame)).getText().toString();

                DatabaseReference ref = database.getReference("profile");
                ref.addChildEventListener(childEventListener);

                DatabaseReference myRef = database.getReference("profile");

                Hashtable<String, String> profiles
                        = new Hashtable<String, String>();
                profiles.put("name", namee);
                profiles.put("game", gamee);

                myRef.setValue(profiles);


                finish();

            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CODE){
            Uri image = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                img.setImageBitmap(bitmap);

                bitimg = bitmap;
                String image1 = bitmapstring(bitimg);
                Log.d(TAG, "비트맵저장되는거맞나?: "+image1);
                SharedPreferences sharedPref = getSharedPreferences("shared",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("profileimg", image1);
                editor.commit();



            } catch (IOException e) {
                e.printStackTrace();
            }
        //    Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            StorageReference riversRef = mStorageRef.child("users").child(email).child("profile.jpg");


            riversRef.putFile(image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                          //  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.d(TAG, taskSnapshot.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });


        }


    }//온액티비티리설트

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }//리퀘스트퍼미션리설트

    public String bitmapstring(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] b =baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }//비트맵스트링

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == android.R.id.home) {
            finish();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}