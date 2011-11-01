package com.nextgen.bemore;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;

import com.nextgen.database.DataBaseHelper;
import com.nextgen.facebook.BaseRequestListener;
import com.nextgen.facebook.FriendsGetMovies;
import com.nextgen.facebook.Utility;

import android.support.v4.app.*;
import android.text.method.ScrollingMovementMethod;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


    /**
     * This is the secondary fragment, displaying the details of a particular
     * item.
     */

    public class EventDetailsFragment extends Fragment implements android.view.View.OnClickListener{
        /**
         * Create a new instance of DetailsFragment, initialized to
         * show the text at 'index'.
         */
        private Long mRowId;
        private DataBaseHelper mEventDbHelper;
        private static final String TAG = "DetailsFragment";
        String mYouTubeVideoId = null;
     
        
        public static EventDetailsFragment newInstance(Long id) {
            EventDetailsFragment f = new EventDetailsFragment();

            
            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putLong("id", id);
            f.setArguments(args);

            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Cursor event = null;
           
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

//            ScrollView scroller = new ScrollView(getActivity());
//            TextView text = new TextView(getActivity());
//            int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                    4, getActivity().getResources().getDisplayMetrics());
//            text.setPadding(padding, padding, padding, padding);
//            scroller.addView(text);
//            text.setText(EventData.DIALOGUE[getArguments().getInt("index", 0)]);
//            return scroller;

            View v = inflater.inflate(R.layout.event_details, container, false);

            //Get cursor to db using id
            mEventDbHelper = MainActivity.getDatabaseHelper();

            mRowId = getArguments().getLong("id", 0);
            
            if (mRowId != null) {
                 event = mEventDbHelper.fetchEvent(mRowId);
//                startManagingCursor(event);
                
                //make sure the cursor is not empty
                if (event.getCount() > 0) {
                View tv = v.findViewById(R.id.details_event_date);
                ((TextView)tv).setText(event.getString(
                        event.getColumnIndexOrThrow(DataBaseHelper.KEY_DATE)));

                tv = v.findViewById(R.id.details_event_name);
                ((TextView)tv).setText(event.getString(
                        event.getColumnIndexOrThrow(DataBaseHelper.KEY_EVENT_NAME)));

                tv = v.findViewById(R.id.details_event_channel);
                ((TextView)tv).setText(event.getString(
                        event.getColumnIndexOrThrow(DataBaseHelper.KEY_EVENT_CHANNEL)));

                RatingBar rb = (RatingBar)v.findViewById(R.id.ratingBar1);
                rb.setRating(event.getInt(
                        event.getColumnIndexOrThrow(DataBaseHelper.KEY_EVENT_RATING)));

                tv = v.findViewById(R.id.details_event_short_desc);
                ((TextView)tv).setText(event.getString(
                        event.getColumnIndexOrThrow(DataBaseHelper.KEY_SHORT_DESC)));    
                ((TextView)tv).setMovementMethod(new ScrollingMovementMethod());
                
                ImageView jpgView = (ImageView)v.findViewById(R.id.details_event_poster);
                String imageName = event.getString(event.getColumnIndexOrThrow(DataBaseHelper.KEY_IMAGE_POSTER));
                String myJpgPath = Environment.getExternalStorageDirectory()+"/WhatsON_Images/"+imageName;
                BitmapDrawable d = new BitmapDrawable(getResources(), myJpgPath);
                jpgView.setImageDrawable(d);            

                ImageView trailer = (ImageView)v.findViewById(R.id.details_event_trailer);
                trailer.setOnClickListener(this);
                
                ImageView like = (ImageView)v.findViewById(R.id.imageView1);
                like.setOnClickListener(this);

                TextView tvSetReminder= (TextView)v.findViewById(R.id.details_set_reminder);
                tvSetReminder.setOnClickListener(this);
                TextView tvSetRecording= (TextView)v.findViewById(R.id.details_set_recording);
                tvSetRecording.setOnClickListener(this);
                TextView tvRentNow= (TextView)v.findViewById(R.id.details_rent_now);
                tvRentNow.setOnClickListener(this);

                 mYouTubeVideoId = event.getString(
                        event.getColumnIndexOrThrow(DataBaseHelper.KEY_YOUTUBE_VIDEO_ID));
                }
                else
                {
                    Log.w(TAG, "event Cursor is empty!!!!");
                }
            }
            
            event.close();
            return v;            
        }

        
        public void onClick(View arg0) {
            final Dialog dialog = new Dialog(this.getActivity(), R.style.myBackgroundStyle);

            OnClickListener listener = new OnClickListener() {

                public void onClick(View v) {
                    dialog.dismiss();
                    }
                };
                
            Log.w(TAG, "View="+arg0.getId());
            if (arg0.getId() == R.id.details_event_trailer) 
            {
                   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+mYouTubeVideoId)); 
                   intent.putExtra("VIDEO_ID", mYouTubeVideoId); 
                   startActivity(intent); 
            } 
           
            else if (arg0.getId() == R.id.details_set_reminder)
            {
                //set up dialog
                dialog.setContentView(R.layout.reminder_dialog);
//                dialog.setTitle("Set reminder on your decoder or android device");
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!

                TextView text = (TextView) dialog.findViewById(R.id.reminder_text_descr);
                ImageView jpgView = (ImageView)dialog.findViewById(R.id.reminder_poster);
 
                if (mRowId != null) {
                   Cursor event = mEventDbHelper.fetchEvent(mRowId);
                   
                   //make sure the cursor is not empty
                   if (event.getCount() > 0) {
                       text.setText(event.getString(event.getColumnIndexOrThrow(DataBaseHelper.KEY_EVENT_NAME)));

                       String imageName = event.getString(event.getColumnIndexOrThrow(DataBaseHelper.KEY_IMAGE_POSTER));
                       String myJpgPath = Environment.getExternalStorageDirectory()+"/WhatsON_Images/"+imageName;
                       BitmapDrawable d = new BitmapDrawable(getResources(), myJpgPath);
                       jpgView.setImageDrawable(d);            
                       
                   }
                   event.close();
               }
                //set up text
                

 
                //set up image view
//                ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
//                img.setImageResource(R.drawable.nista_logo);
// 
                //set up button
                Button button = (Button) dialog.findViewById(R.id.reminder_positive_btn);
                Button button_cancel = (Button) dialog.findViewById(R.id.reminder_negative_btn);
                
                button.setOnClickListener(listener);
                button_cancel.setOnClickListener(listener);
                //now that the dialog is set up, it's time to show it    
                dialog.show();                
            }
            else if (arg0.getId() == R.id.details_set_recording)
            {
                //set up dialog
                dialog.setContentView(R.layout.set_recording_dialog);
//                dialog.setTitle("Set reminder on your decoder or android device");
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!

                TextView text = (TextView) dialog.findViewById(R.id.set_recording_event_name);
                ImageView jpgView = (ImageView)dialog.findViewById(R.id.set_recording_poster);
 
                if (mRowId != null) {
                   Cursor event = mEventDbHelper.fetchEvent(mRowId);
                   
                   //make sure the cursor is not empty
                   if (event.getCount() > 0) {
                       text.setText(event.getString(event.getColumnIndexOrThrow(DataBaseHelper.KEY_EVENT_NAME)));

                       String imageName = event.getString(event.getColumnIndexOrThrow(DataBaseHelper.KEY_IMAGE_POSTER));
                       String myJpgPath = Environment.getExternalStorageDirectory()+"/WhatsON_Images/"+imageName;
                       BitmapDrawable d = new BitmapDrawable(getResources(), myJpgPath);
                       jpgView.setImageDrawable(d);            
                       
                   }
                   event.close();
               }
                //set up text
                

 
                //set up image view
//                ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
//                img.setImageResource(R.drawable.nista_logo);
// 
                //set up button
                Button button = (Button) dialog.findViewById(R.id.set_recording_btn);
                Button button_cancel = (Button) dialog.findViewById(R.id.set_recording_cancel_btn);
                
                button.setOnClickListener(listener);
                button_cancel.setOnClickListener(listener);
                //now that the dialog is set up, it's time to show it    
                dialog.show();      
            }
            else if (arg0.getId() == R.id.details_rent_now)
            {
                //set up dialog
                dialog.setContentView(R.layout.rent_now_dialog);
//                dialog.setTitle("Set reminder on your decoder or android device");
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!

                TextView text = (TextView) dialog.findViewById(R.id.rent_now_name);
                ImageView jpgView = (ImageView)dialog.findViewById(R.id.rent_now_poster);
 
                if (mRowId != null) {
                   Cursor event = mEventDbHelper.fetchEvent(mRowId);
                   
                   //make sure the cursor is not empty
                   if (event.getCount() > 0) {
                       text.setText(event.getString(event.getColumnIndexOrThrow(DataBaseHelper.KEY_EVENT_NAME)));

                       String imageName = event.getString(event.getColumnIndexOrThrow(DataBaseHelper.KEY_IMAGE_POSTER));
                       String myJpgPath = Environment.getExternalStorageDirectory()+"/WhatsON_Images/"+imageName;
                       BitmapDrawable d = new BitmapDrawable(getResources(), myJpgPath);
                       jpgView.setImageDrawable(d);            
                       
                   }
                   event.close();
               }
                //set up text
                

 
                //set up image view
//                ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
//                img.setImageResource(R.drawable.nista_logo);
// 
                //set up button
                Button button = (Button) dialog.findViewById(R.id.rent_now_btn);
                Button button_cancel = (Button) dialog.findViewById(R.id.rent_now_cancel_btn);

                
                button.setOnClickListener(listener);
                button_cancel.setOnClickListener(listener);
                //now that the dialog is set up, it's time to show it    
                dialog.show();      
            }            
            
        }
        

    }