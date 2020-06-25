package com.example.gamecall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class Chat {
    String email;
    String text;
    String image;
    Bitmap ivbit;
   // ImageView image;


    public void setIvbit() {
        this.ivbit = stringbitmap(image);
    }

    public Bitmap getIvbit() {
        this.ivbit = stringbitmap(image);
        return ivbit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
