package com.nextgen.bemore;
import java.io.FileInputStream;

import com.nextgen.bemore.EventRecommendationFragment.ImageAdapter;
import com.nextgen.coverflow.CoverFlow;
import com.nextgen.database.DataBaseHelper;
import com.nextgen.facebook.FriendsGetMovies;
import com.nextgen.facebook.FriendsMoviesImageAdapter;
import com.nextgen.facebook.ImageDownloader;
import com.viewpagerindicator.R;

import android.support.v4.app.*;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.database.Cursor;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;


    /**
     * This is the secondary fragment, displaying the details of a particular
     * item.
     */

    public class FacebookFragment extends ListFragment implements OnItemClickListener , AsyncFacebookNotifier{

        private static final String TAG = "FacebookFragment";
        private static TextView pageTitle = null;
        public static FacebookFragment f;
        int mCount=0;
      
        /**
         * Create a new instance of DetailsFragment, initialized to
         * show the text at 'index'.
         */
        public static FacebookFragment newInstance(Long id) {
            f = new FacebookFragment();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putLong("id", id);
            f.setArguments(args);

            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            
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
            View v = inflater.inflate(R.layout.facebook_likes_layout, container, false);
            FriendsMoviesImageAdapter facebookFriendsImageAdapter =  new FriendsMoviesImageAdapter(this.getActivity().getApplicationContext());
            setListAdapter(facebookFriendsImageAdapter);

//              CoverFlow facebookFriendsCoverFlow;
////            coverFlow = new CoverFlow(this.getActivity().getApplicationContext());
//              facebookFriendsCoverFlow = (CoverFlow) v.findViewById(R.id.fb_friend_likes_coverflow);
//              FriendsMoviesImageAdapter facebookFriendsImageAdapter =  new FriendsMoviesImageAdapter(this.getActivity().getApplicationContext());
//            facebookFriendsCoverFlow.setAdapter(facebookFriendsImageAdapter);
//            facebookFriendsCoverFlow.setSpacing(-25);
//            facebookFriendsCoverFlow.setSelection(10, true);
//            facebookFriendsCoverFlow.setAnimationDuration(1000);
////            facebookFriendsCoverFlow.setOnItemClickListener(this);

			pageTitle =  (TextView) v.findViewById(R.id.fb_page_title);
              return v;       
                     
        }

        public void onItemClick(AdapterView parent, View v, int position, long id) {
            
            //TODO: have to extract the url of the selected buy item and launch the webactivity

            
      
        }

        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            String itemUrl;
            
            final Dialog dialog = new Dialog(this.getActivity(), R.style.myBackgroundStyle);

            OnClickListener listener = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();         
                }
                };

            //set up dialog
            dialog.setContentView(R.layout.rent_now_dialog);
//            dialog.setTitle("Set reminder on your decoder or android device");
            dialog.setCancelable(true);
            //there are a lot of settings, for dialog, check them all out!

            TextView text = (TextView) dialog.findViewById(R.id.rent_now_name);
            ImageView jpgView = (ImageView)dialog.findViewById(R.id.rent_now_poster);
            
               text.setText(FriendsGetMovies.getMovieName(position));
               jpgView.setImageResource(R.drawable.dstv_boxoffice_ad);
              


            //set up text
            


            //set up image view
//            ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
//            img.setImageResource(R.drawable.nista_logo);
//
            //set up button
            Button button = (Button) dialog.findViewById(R.id.rent_now_btn);
            Button button_cancel = (Button) dialog.findViewById(R.id.rent_now_cancel_btn);

            
            button.setOnClickListener(listener);
            button_cancel.setOnClickListener(listener);
            //now that the dialog is set up, it's time to show it    
            dialog.show();      
//            itemUrl = FriendsGetMovies.getMovieUrl(position);
//            if (itemUrl != null)
//            {
//                  Intent i = new Intent(this.getActivity().getApplicationContext(), WebviewActivity.class);
//                  i.putExtra("buy_item_url",itemUrl);
//                  startActivity(i);    
//            }
        }

        private static boolean boolVal = false;
        public void notifyFacebookApp(boolean updating) {
        	
        	if (updating) {
        		boolVal = true;
        		getActivity().runOnUiThread(new Runnable() {
    				public void run() {
    					
    	    				Log.w(TAG, "FriendsGetMovies.remainingFriends="+FriendsGetMovies.remainingFriends);
        	    			String str = getString(R.string.updateMessage);
    						pageTitle.setText(str + " " + (FriendsGetMovies.NUM_OF_FRIENDS - (FriendsGetMovies.totalFriends - FriendsGetMovies.remainingFriends)));
    				}
    			});
    		} 
    		else {
    			
    			Log.w(TAG, "ending by calling run");
    			Log.w(TAG, "getActivity() = "+ getActivity().toString());
    			if (boolVal)
	    			getActivity().runOnUiThread(new Runnable() {
	    				public void run() {
	    					pageTitle.setText("Your facebook friends like this");
	    				}
	    			});
    			boolVal = false;
    		}
        }

//        public void onClick(View v) {
//            switch (v.getId()) {
//            case R.id.buy_title:
//            case R.id.buy_desc:
//            case R.id.buy_price:
//                Intent i = new Intent(this.getActivity().getApplicationContext(), WebviewActivity.class);
//                startActivity(i);             
//                break;
//            }
//        }
    }