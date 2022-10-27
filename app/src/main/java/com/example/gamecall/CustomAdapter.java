package com.example.gamecall;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
   public ImageView userphoto;
    private ArrayList<Dictionary> mList;
    private Context mContext;
    String email;
    String myemail="";


    public class CustomViewHolder extends  RecyclerView.ViewHolder {
        public ImageView userphoto;
        public TextView username;
        public TextView usergame;
        public TextView eemail;



        public CustomViewHolder(View view) {
            super(view);

            userphoto = view.findViewById(R.id.fphoto);
            this.username = (TextView) view.findViewById(R.id.fname);
            this.usergame = (TextView) view.findViewById(R.id.fgame);



        }
    }


    public CustomAdapter(Context context , ArrayList<Dictionary> list, String email) {
        mList = list;
       mContext = context;
        this.myemail = email;

    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {

       if(mList.get(position).getEmail().equals(myemail)) {}else {

           viewholder.userphoto.setImageBitmap(mList.get(position).getFphoto());
           viewholder.username.setText(mList.get(position).getName());
           viewholder.usergame.setText(mList.get(position).getGame());

//           final String nextphoto = mList.get(position).getImage();
           final String nextemail = mList.get(position).getEmail();
           final String nextname = mList.get(position).getName();
           final String nextgame = mList.get(position).getGame();
           final String nextuid = mList.get(position).getUid();
           viewholder.userphoto.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(v.getContext(), otherprofile.class);
                  intent.putExtra("nowuid", nextuid);
                   intent.putExtra("nowemail",nextemail);
                   intent.putExtra("nowname",nextname);
                   intent.putExtra("nowgame",nextgame);
                   v.getContext().startActivity(intent);
               }
           });

       }

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }


}
