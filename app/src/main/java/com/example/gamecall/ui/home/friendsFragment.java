package com.example.gamecall.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamecall.Chat;
import com.example.gamecall.CustomAdapter;
import com.example.gamecall.Dictionary;
import com.example.gamecall.ExampleService;
import com.example.gamecall.User;
import com.example.gamecall.MyAdapter;
import com.example.gamecall.R;
import com.example.gamecall.addfriend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class friendsFragment extends Fragment {
    private static final String TAG = "friendsFragment";
    FirebaseDatabase database;
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
    String loadname, loadgame;
    TextView nicname, game;
    int notify;
    String email;
    public  ArrayList<Chat> friendList;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
      //  homeViewModel =
      //          ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.activity_main, container, false);
        setHasOptionsMenu(true);

      /*  final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        // getSupportActionBar().setTitle("ACTIONBAR");


        //????????????????????????
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
            // Toast.makeText(MainActivity.this, "?????? ????????? ???????????? ?????????.",
            //         Toast.LENGTH_LONG).show();

            //  Log.e(TAG, "?????????????????????? ?????? ???????????????!");
        }

        SharedPreferences sharedPref =  this.getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        String profileimg = sharedPref.getString("profileimg","");
        profile = stringbitmap(profileimg);
        mainimg = root.findViewById(R.id.mainimage);
        mainimg.setImageBitmap(profile);


        nicname = (TextView)root.findViewById(R.id.mainname);
        game = (TextView)root.findViewById(R.id.game);
        loadname=sharedPref.getString("nicname", "????????????");
        loadgame= sharedPref.getString("game","????????????");
        nicname.setText(loadname);
        game.setText(loadgame);
        email = sharedPref.getString("email", "");

        notify = sharedPref.getInt("mesnotify", 0);

//        if(notify==1){
//
//            getActivity().startService(new Intent(getActivity(), ExampleService.class));
//        }






//??????



//???????????????????????????
        RecyclerView mRecyclerView = root.findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        mArrayList = new ArrayList<>();


        // kAdapter = new CustomAdapter( mArrayList);
        kAdapter = new CustomAdapter(getActivity(), mArrayList, email);
        mRecyclerView.setAdapter(kAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        DatabaseReference myRef = database.getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, "?????????????????????????????? "+dataSnapshot.getValue().toString());

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    Dictionary dic  = dataSnapshot1.getValue(Dictionary.class);
//                    Log.d(TAG, "?????????????????????????????? "+dic.getName());
//                    Log.d(TAG, "?????????????????????????????? "+dic.getGame());
//                    Log.d(TAG, "?????????????????????????????? "+dic.getEmail());
                    mArrayList.add(dic);

                }
                kAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



/*
        //???????????????
        Button newfriendBTN = root.findViewById(R.id.newfriendBTN);
        newfriendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.edit_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                final EditText userphoto = (EditText) view.findViewById(R.id.userphoto);
                final EditText username = (EditText) view.findViewById(R.id.username);
                final EditText usergame = (EditText) view.findViewById(R.id.usergame);

                ButtonSubmit.setText("??????");


                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String strphoto = userphoto.getText().toString();
                        String strname = username.getText().toString();
                        String strgame = usergame.getText().toString();

                        Dictionary dict = new Dictionary(strphoto, strname, strgame );

                        mArrayList.add(0, dict); //??? ?????? ??????
                        //mArrayList.add(dict); //????????? ?????? ??????
                        kAdapter.notifyDataSetChanged(); //????????? ???????????? ????????? ??????


                        dialog.dismiss();
                        Toast.makeText(getActivity(), strname+"?????? ???????????? ???????????????.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();


            }
        });
*/



/*
//??????????????? ????????????
        TextView mainname = root.findViewById(R.id.mainname);
        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        mainname.setText(name+"???");
        */
/*
//?????????????????????
        Button option = root.findViewById(R.id.optionBTN);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        com.example.gamecall.option.class);
                intent.putExtra("email", name);
                startActivity(intent);
            }
        });
*/



        return root;
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

    }//??????????????????

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu,menu);
    }


//??????????????? ?????????
@Override public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
    if(curId==R.id.action_add){
        Intent intent = new Intent(getActivity(), addfriend.class);
        startActivity(intent);
        return true;
    }
    return super.onOptionsItemSelected(item);
}







}