package com.nextgen.bemore;

import java.io.FileInputStream;

import com.nextgen.coverflow.CoverFlow;

import android.support.v4.app.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;


    /**
     * This is the secondary fragment, displaying the details of a particular
     * item.
     */

    public class RecommendedFragment extends Fragment {
        /**
         * Create a new instance of DetailsFragment, initialized to
         * show the text at 'index'.
         */
        public static RecommendedFragment newInstance(int index) {
            RecommendedFragment f = new RecommendedFragment();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putInt("index", index);
            f.setArguments(args);

            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            if (container == null) {
                // We have different layouts, and in one of them this
                // fragment's containing frame doesn't exist.  The fragment
                // may still be created from its saved state, but there is
                // no reason to try to create its view hierarchy because it
                // won't be displayed.  Note this is not needed -- we could
                // just run the code below, where we would create and return
                // the view hierarchy; it would just never be used.
                return null;
            }

            CoverFlow coverFlow;
            coverFlow = new CoverFlow(this.getActivity().getApplicationContext());
            coverFlow.setAdapter(new ImageAdapter(this.getActivity().getApplicationContext()));

            ImageAdapter coverImageAdapter =  new ImageAdapter(this.getActivity().getApplicationContext());
            
            //coverImageAdapter.createReflectedImages();
            
            coverFlow.setAdapter(coverImageAdapter);
            
            coverFlow.setSpacing(-25);
            coverFlow.setSelection(4, true);
            coverFlow.setAnimationDuration(1000);
            
            return coverFlow;
        }
    
    
    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        private FileInputStream fis;
           
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
        
        public ImageAdapter(Context c) {
         mContext = c;
         mImages = new ImageView[mImageIds.length];
        }
     public boolean createReflectedImages() {
             //The gap we want between the reflection and the original image
             final int reflectionGap = 10;
             
             
             int index = 0;
             for (int imageId : mImageIds) {
           Bitmap originalImage = BitmapFactory.decodeResource(getResources(), 
             imageId);
              int width = originalImage.getWidth();
              int height = originalImage.getHeight();
              
        
              //This will not scale but will flip on the Y axis
              Matrix matrix = new Matrix();
              matrix.preScale(1, -1);
              
              //Create a Bitmap with the flip matrix applied to it.
              //We only want the bottom half of the image
              Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);
              
                  
              //Create a new bitmap with same width but taller to fit reflection
              Bitmap bitmapWithReflection = Bitmap.createBitmap(width 
                , (height + height/2), Config.ARGB_8888);
            
             //Create a new Canvas with the bitmap that's big enough for
             //the image plus gap plus reflection
             Canvas canvas = new Canvas(bitmapWithReflection);
             //Draw in the original image
             canvas.drawBitmap(originalImage, 0, 0, null);
             //Draw in the gap
             Paint deafaultPaint = new Paint();
             canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
             //Draw in the reflection
             canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
             
             //Create a shader that is a linear gradient that covers the reflection
             Paint paint = new Paint(); 
             LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, 
               bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, 
               TileMode.CLAMP); 
             //Set the paint to use this shader (linear gradient)
             paint.setShader(shader); 
             //Set the Transfer mode to be porter duff and destination in
             paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
             //Draw a rectangle using the paint with our linear gradient
             canvas.drawRect(0, height, width, 
               bitmapWithReflection.getHeight() + reflectionGap, paint); 
             
             ImageView imageView = new ImageView(mContext);
             imageView.setImageBitmap(bitmapWithReflection);
             imageView.setLayoutParams(new CoverFlow.LayoutParams(120, 180));
             imageView.setScaleType(ScaleType.MATRIX);
             mImages[index++] = imageView;
             
             }
          return true;
     }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

         //Use this code if you want to load from resources
            ImageView i = new ImageView(mContext);
            i.setImageResource(mImageIds[position]);
            i.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
            i.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 
            
            //Make sure we set anti-aliasing otherwise we get jaggies
            BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
            drawable.setAntiAlias(true);
            return i;
         
         //return mImages[position];
        }
      /** Returns the size (0.0f to 1.0f) of the views 
         * depending on the 'offset' to the center. */ 
         public float getScale(boolean focused, int offset) { 
           /* Formula: 1 / (2 ^ offset) */ 
             return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
         } 

    }
   }