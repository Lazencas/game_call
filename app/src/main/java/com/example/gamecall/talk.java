package com.example.gamecall;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Comment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class talk extends AppCompatActivity {
    private static final String TAG = "talk";
    int isIMG=0;
    Button imgcancel;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private RecyclerView recyclerView;
    InputMethodManager imm;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText inputtext;
    String imagewill;
    String email;
    Bitmap immgg;
    Context context;
    ConstraintLayout imgchoice;
    LinearLayout call;
    Button gall, camera;
    FirebaseDatabase database;
     ArrayList<Chat> chatArrayList;
    String sendtext;
    int first =0;
    int notify =0;
    private File tempFile;
     ImageView imgv, preimg;
    private static final int PICK_FROM_GALLERY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk);

        tedPermission();


        getSupportActionBar().setTitle("대화");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();



        gall = (Button)findViewById(R.id.gall);
        camera = (Button)findViewById(R.id.camera);

         imgchoice =(ConstraintLayout)findViewById(R.id.imagechoose);
          call = (LinearLayout)findViewById(R.id.callimage) ;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imgcancel =(Button)findViewById(R.id.imgcancel);
       // imgv = (ImageView)findViewById(R.id.chatimg) ;

        preimg = (ImageView)findViewById(R.id.preimg);



        database = FirebaseDatabase.getInstance();

        chatArrayList = new ArrayList<>();

        inputtext = (EditText)findViewById(R.id.talktext);
        inputtext.requestFocus();



        email=getIntent().getStringExtra("email");
       //   Toast.makeText(talk.this, email,
        //           Toast.LENGTH_LONG).show();


        email = sharedPref.getString("email","");
        notify = sharedPref.getInt("mesnotify", 0);

//+버튼클릭시 나오는창
        Button buttonGallery = (Button)findViewById(R.id.sendimg);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                call.setVisibility(View.VISIBLE);
                //갤러리선택했을때
                gall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call.setVisibility(View.GONE);
                        imgchoice.setVisibility(View.VISIBLE);
                        Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(in, PICK_FROM_ALBUM);
                    }
                });

                //카메라선택했을때
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        call.setVisibility(View.GONE);
                        imgchoice.setVisibility(View.VISIBLE);
                        dispatchTakePictureIntent();
                       // takePhoto();

                    }
                });




            }
        });









        //하위처리
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list


                Chat chat = dataSnapshot.getValue(Chat.class);
               //immgg = stringbitmap(imagewill);
                String commentKey = dataSnapshot.getKey();
                String sendEmail = chat.getEmail();
                String sendText = chat.getText();
                String sendImage = chat.getImage();

//                Log.d(TAG, "이메일: "+sendEmail);
//                Log.d(TAG, "텍스트: "+sendText);
              //  Log.d(TAG, "이미지: "+sendImage);
                chatArrayList.add(chat);
                mAdapter.notifyDataSetChanged();
                if(notify==1) {

                    if (email.equals(sendEmail)) {


                    } else {

                        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("lasttext", sendText);
                        editor.commit();

                   if(isAppRunning(talk.this))   {

                   }else {
                       startService();
                   }



                    }
                }

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
                Toast.makeText(talk.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        DatabaseReference ref = database.getReference("message");
        ref.addChildEventListener(childEventListener);

        //문자보내기버튼
        Button sendBtn = (Button)findViewById(R.id.sendtext);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgcancel.setVisibility(View.INVISIBLE);
                preimg.setImageBitmap(null);
                 sendtext = inputtext.getText().toString();
                //   Toast.makeText(talk.this, email,
                //           Toast.LENGTH_LONG).show();
                Calendar c =Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String datetime = dateformat.format(c.getTime());




                DatabaseReference myRef = database.getReference("message").child(datetime);

                Hashtable<String, String> numbers
                        = new Hashtable<String, String>();
                numbers.put("email", email);
                numbers.put("text", sendtext);

                if(isIMG==1){
                    numbers.put("image", imagewill);
                    isIMG=0;
                   // Log.d(TAG, "이미지보내지나확인: "+imagewill);
                }

                myRef.setValue(numbers);
                imgchoice.setVisibility(View.GONE);
                inputtext.setText("");



            }
        });


//        //대화에서 뒤로가기
//        Button exittalk = (Button)findViewById(R.id.exittalk);
//        exittalk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


        recyclerView = (RecyclerView) findViewById(R.id.talkdia);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(chatArrayList, email);
        recyclerView.setAdapter(mAdapter);







    }
    //메뉴
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
    //메뉴집엊넣기

//비트스트링

    public String bitmapstring(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] b =baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }//비트맵스트링

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


    public void startService() {

        Intent intent = new Intent(this, ExampleService.class);

        startService(intent);
    }


    public void stopService() {

        Intent intent = new Intent(this, ExampleService.class);
        stopService(intent);
    }




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

    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {

            imgchoice.setVisibility(View.GONE);
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {
            Uri image = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                preimg.setImageBitmap(bitmap);

                 imagewill = bitmapstring(bitmap);
                isIMG=1;

                imgcancel.setVisibility(View.VISIBLE);
                imgcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preimg.setImageBitmap(null);
                        imgcancel.setVisibility(View.GONE);
                        imgchoice.setVisibility(View.GONE);

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if(requestCode==PICK_FROM_CAMERA){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView)findViewById(R.id.preimg)).setImageBitmap(imageBitmap);
            imagewill = bitmapstring(imageBitmap);
            isIMG=1;

        }



    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
            Button cancel = (Button)findViewById(R.id.imgcancel);
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preimg.setImageBitmap(null);
                    imgchoice.setVisibility(View.GONE);

                }
            });




        }




    }




    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }


    private boolean isAppRunning(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i++){
            if(procInfos.get(i).processName.equals(context.getPackageName())){
                return true;
            }
        }

        return false;
    }





}