package com.example.gamecall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gamecall.talk;



import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Chat> mDataset;
    String myemail = "";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public ImageView img;
     //   public TextView img;
        //protected  imageView;
        //public ImageView imageView;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.chats);
            img = v.findViewById(R.id.chatimg);

        }
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if(mDataset.get(position).email.equals(myemail)){
             return 1;
        }else {
            return 0;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Chat> myDataset, String email) {
        mDataset = myDataset;
        this.myemail = email;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        if(viewType == 1){
             v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.realmy_text_view, parent, false);
        }

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.textView.setText(mDataset.get(position).getText());
      // holder.img.setText(mDataset.get(position).getImage());
        holder.img.setImageBitmap(mDataset.get(position).getIvbit());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


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


}




