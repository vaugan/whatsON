package com.nextgen.bemore;

import com.nextgen.database.DataBaseHelper;

import android.support.v4.app.*;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


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
            mEventDbHelper = new DataBaseHelper(this.getActivity());
            mEventDbHelper.openDataBase();

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
//                tv.setOnClickListener(this);

                tv = v.findViewById(R.id.details_event_short_desc);
                ((TextView)tv).setText(event.getString(
                        event.getColumnIndexOrThrow(DataBaseHelper.KEY_SHORT_DESC)));    
                
                ImageView jpgView = (ImageView)v.findViewById(R.id.details_event_poster);
                String imageName = event.getString(event.getColumnIndexOrThrow(DataBaseHelper.KEY_IMAGE_POSTER));
                String myJpgPath = Environment.getExternalStorageDirectory()+"/WhatsON_Images/"+imageName;
                BitmapDrawable d = new BitmapDrawable(getResources(), myJpgPath);
                jpgView.setImageDrawable(d);            

                ImageView trailer = (ImageView)v.findViewById(R.id.details_event_trailer);
                trailer.setOnClickListener(this);
                
                 mYouTubeVideoId = event.getString(
                        event.getColumnIndexOrThrow(DataBaseHelper.KEY_YOUTUBE_VIDEO_ID));
                }
                else
                {
                    Log.w(TAG, "event Cursor is empty!!!!");
                }
            }
            
            event.close();
            mEventDbHelper.close();
            return v;            
        }

        public void onClick(View arg0) {
            if (mYouTubeVideoId != null )
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+mYouTubeVideoId)); 
                intent.putExtra("VIDEO_ID", mYouTubeVideoId); 
                startActivity(intent); 
            }
            else
            {
                //display some dialog indicating there's no trailer
            }
        }          
    }