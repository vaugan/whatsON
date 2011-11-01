package com.nextgen.facebook;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.support.v4.app.*;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import com.nextgen.bemore.MainActivity;
import com.nextgen.bemore.R;
import com.nextgen.coverflow.CoverFlow;
import com.nextgen.database.DataBaseHelper;


public class FriendsMoviesImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;
        private static final String TAG  = "FriendsMoviesImageAdapter";
        private static final int MAX_FRIENDS_MOVIES_DISPLAYED=20;
        private static final int IO_BUFFER_SIZE = 4 * 1024;
        private static LayoutInflater inflater=null;

        Bitmap bitmap = null;
        private final ImageDownloader imageDownloader = new ImageDownloader();
        
        private Integer[] mImageIds = {
                R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3,
                R.drawable.image4,
                R.drawable.image5,
                R.drawable.image6,
                R.drawable.image7,
                R.drawable.image8,
                R.drawable.image9
        };

        private ImageView[] mImages;
        
        public FriendsMoviesImageAdapter(Context c) {
         mContext = c;
         
         inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      
         mImages = new ImageView[MAX_FRIENDS_MOVIES_DISPLAYED];
        }
        
//     public boolean createReflectedImages() {
//             //The gap we want between the reflection and the original image
//             final int reflectionGap = 10;
//             
//             
//             int index = 0;
//             for (int imageId : mImageIds) {
//             Bitmap originalImage = BitmapFactory.decodeResource(getResources(), imageId);
//              int width = originalImage.getWidth();
//              int height = originalImage.getHeight();
//              
//        
//              //This will not scale but will flip on the Y axis
//              Matrix matrix = new Matrix();
//              matrix.preScale(1, -1);
//              
//              //Create a Bitmap with the flip matrix applied to it.
//              //We only want the bottom half of the image
//              Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);
//              
//                  
//              //Create a new bitmap with same width but taller to fit reflection
//              Bitmap bitmapWithReflection = Bitmap.createBitmap(width 
//                , (height + height/2), Config.ARGB_8888);
//            
//             //Create a new Canvas with the bitmap that's big enough for
//             //the image plus gap plus reflection
//             Canvas canvas = new Canvas(bitmapWithReflection);
//             //Draw in the original image
//             canvas.drawBitmap(originalImage, 0, 0, null);
//             //Draw in the gap
//             Paint deafaultPaint = new Paint();
//             canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
//             //Draw in the reflection
//             canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
//             
//             //Create a shader that is a linear gradient that covers the reflection
//             Paint paint = new Paint(); 
//             LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, 
//               bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, 
//               TileMode.CLAMP); 
//             //Set the paint to use this shader (linear gradient)
//             paint.setShader(shader); 
//             //Set the Transfer mode to be porter duff and destination in
//             paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
//             //Draw a rectangle using the paint with our linear gradient
//             canvas.drawRect(0, height, width, 
//               bitmapWithReflection.getHeight() + reflectionGap, paint); 
//             
//             ImageView imageView = new ImageView(mContext);
//             imageView.setImageBitmap(bitmapWithReflection);
//             imageView.setLayoutParams(new CoverFlow.LayoutParams(120, 180));
//             imageView.setScaleType(ScaleType.MATRIX);
//             mImages[index++] = imageView;
//             
//             }
//          return true;
//     }

        public int getCount() {
            return MAX_FRIENDS_MOVIES_DISPLAYED;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.fb_list_item, null);
//
//            TextView text=(TextView)vi.findViewById(R.id.text);
//            ImageView image=(ImageView)vi.findViewById(R.id.image);
//            text.setText("item "+position);
//            imageLoader.DisplayImage(data[position], activity, image);
//            return vi;
            
            ImageView ivLikeIcon=(ImageView)vi.findViewById(R.id.fb_list_like_icon);
            TextView tvMovieName=(TextView)vi.findViewById(R.id.fb_list_item_movie_name);
            TextView tvMovieLikes=(TextView)vi.findViewById(R.id.fb_list_item_movie_likes);
            
            ImageView i = (ImageView)vi.findViewById(R.id.fb_list_item_image);//new ImageView(mContext);
                   
            URL newUrl;
            
//            if (bitmap == null)
//            {
//                Resources res = mContext.getResources();
//                bitmap = BitmapFactory.decodeResource(res,R.drawable.fb_default);
//            }
            
            if (FriendsGetMovies.myTop20Movies  != null)
            {
                tvMovieName.setText(FriendsGetMovies.myTop20Movies [position].name);
                tvMovieLikes.setText(Integer.toString(FriendsGetMovies.myTop20Movies [position].likes));
                ivLikeIcon.setImageResource(R.drawable.like);

                //newUrl = new URL(FriendsGetMovies.myMoviesList [position].pictureUrl);
                if (FriendsGetMovies.myTop20Movies [position].pictureUrl != null)
                {
                    imageDownloader.download(FriendsGetMovies.myTop20Movies [position].pictureUrl, (ImageView) i);
                }
            }
//            int width = bitmap.getWidth();
//            int height = bitmap.getHeight();
//            int newWidth = 86;
//            int newHeight = 127;
//            
//            // calculate the scale - in this case = 0.4f
//            float scaleWidth = ((float) newWidth) / width;
//            float scaleHeight = ((float) newHeight) / height;
//            
//            // createa matrix for the manipulation
//            Matrix matrix = new Matrix();
//            // resize the bit map
//            matrix.postScale(scaleWidth, scaleHeight);
//            // recreate the new Bitmap
//            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, 
//                              width, height, matrix, true); 
//            BitmapDrawable d = new BitmapDrawable(resizedBitmap);
//            i.setImageDrawable(d);
            
            return vi;

        }
      /** Returns the size (0.0f to 1.0f) of the views 
         * depending on the 'offset' to the center. */ 
         public float getScale(boolean focused, int offset) { 
           /* Formula: 1 / (2 ^ offset) */ 
             return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
         } 

//         Bitmap downloadFile(String fileUrl){
//               URL myFileUrl =null;          
//               Bitmap bmImg = null;
//               try {
//                    myFileUrl= new URL(fileUrl);
//               } catch (MalformedURLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//               }
//               try {
//                    HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
//                    conn.setDoInput(true);
//                    conn.connect();
//                    InputStream is = conn.getInputStream();
//                    
//                    bmImg = BitmapFactory.decodeStream(is);
//               } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//               }
//               return bmImg;
//          }
      
//         class RetreiveImageTask extends AsyncTask<String, Void, Bitmap> {
//
//             private Exception exception;
//
//             protected Bitmap doInBackground(String... url) {
//                 return downloadFile(url[0]);
//             }
//
//             protected void onPostExecute(Bitmap bmp) {
//                 bitmap = bmp;
//             }
//
//          }
         


}

