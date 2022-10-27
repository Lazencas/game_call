package com.example.gamecall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class notifyoption extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifyoption);

        getSupportActionBar().setTitle("알림설정");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
       int notify = sharedPref.getInt("mesnotify", 0);

       Switch messageswitch =(Switch) findViewById(R.id.messagenitiBTN);
        messageswitch.setOnCheckedChangeListener(new visibilitySwitchListener());
        if(notify==1){
            messageswitch.setChecked(true);
        }






    }


    class visibilitySwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {

       startServicea();

            }
            else {

           stopServicea();

            }
        }

    }

    public void startServicea() {
//        Toast.makeText(notifyoption.this, "알림켜짐!",
//                Toast.LENGTH_LONG).show();



        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("mesnotify");
        editor.putInt("mesnotify", 1);

        editor.commit();

        Intent intent = new Intent(this, ExampleService.class);

        startService(intent);

    }


    public void stopServicea() {
//        Toast.makeText(notifyoption.this, "알림꺼짐!",
//                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ExampleService.class);

        stopService(intent);

        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("mesnotify");
        editor.putInt("mesnotify", 0);
        editor.commit();


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



}