package com.example.gamecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
   public ImageView userphoto;
    private ArrayList<Dictionary> mList;
    private Context mContext;


    public class CustomViewHolder extends  RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView userphoto;
        protected TextView username;
        protected TextView usergame;


        public CustomViewHolder(View view) {
            super(view);

            userphoto=view.findViewById(R.id.id_listitem);
            this.username = (TextView) view.findViewById(R.id.english_listitem);
            this.usergame = (TextView) view.findViewById(R.id.korean_listitem);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }


        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem item) {



                switch (item.getItemId()) {
                    case 1001:

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.edit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                        final EditText editTextID = (EditText) view.findViewById(R.id.userphoto);
                        final EditText editTextEnglish = (EditText) view.findViewById(R.id.username);
                        final EditText editTextKorean = (EditText) view.findViewById(R.id.usergame);

                        editTextID.setText(mList.get(getAdapterPosition()).getId());
                        editTextEnglish.setText(mList.get(getAdapterPosition()).getEnglish());
                        editTextKorean.setText(mList.get(getAdapterPosition()).getKorean());

                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String strID = editTextID.getText().toString();
                                String strEnglish = editTextEnglish.getText().toString();
                                String strKorean = editTextKorean.getText().toString();

                                Dictionary dict = new Dictionary(strID, strEnglish, strKorean );

                                mList.set(getAdapterPosition(), dict);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();


                            }
                        });

                        dialog.show();

                        break;

                    case 1002:

                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());


                        break;

                }
                return true;
            }

        };




    }









    public CustomAdapter(Context context, ArrayList<Dictionary> list) {
        mList = list;
        mContext = context;

    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.userphoto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.username.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.usergame.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.userphoto.setGravity(Gravity.CENTER);
        viewholder.username.setGravity(Gravity.CENTER);
        viewholder.usergame.setGravity(Gravity.CENTER);



        viewholder.userphoto.setText(mList.get(position).getId());
        viewholder.username.setText(mList.get(position).getEnglish());
        viewholder.usergame.setText(mList.get(position).getKorean());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}

