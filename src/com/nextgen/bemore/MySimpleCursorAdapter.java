package com.nextgen.bemore;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleCursorAdapter extends SimpleCursorAdapter {

    
    @SuppressWarnings("deprecation")
    public MySimpleCursorAdapter(Context context, int layout, Cursor c,
            String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

    @Override
    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    @Override
    public void setViewImage(ImageView v, String id) {

//        String myJpgPath = Environment.getExternalStorageDirectory()+"/WhatsON_Images/"+imageName;
        String path = Environment.getExternalStorageDirectory()+"/WhatsON_Images/"+id;
        Bitmap b = BitmapFactory.decodeFile(path);
        v.setImageBitmap(b);

    }

}