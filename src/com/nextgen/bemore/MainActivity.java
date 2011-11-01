/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nextgen.bemore;

//import com.nextgen.bemore.apis.R;
import java.io.IOException;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.nextgen.database.DataBaseHelper;
import com.nextgen.facebook.FriendsGetMovies;
import com.nextgen.facebook.Utility;
import com.viewpagerindicator.R;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;


import android.app.Activity;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Demonstration of using fragments to implement different activity layouts.
 * This sample provides a different layout (and activity flow) when run in
 * landscape.
 */
public class MainActivity extends FragmentActivity  {

    public static final String APP_ID = "299979046695005";
   EventListFragmentPagerAdapter mAdapter;
    ViewPager mPager;
    private static DataBaseHelper myDbHelper;         
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;    
    public static FriendsGetMovies fgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.main_layout);
        
      //Open Database
      openDatabase();

        mAdapter = new EventListFragmentAdapterTitleProvider(getSupportFragmentManager());
        
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        
        TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FacebookFragment ff = FacebookFragment.newInstance((long) 1);
        ft.replace(R.id.facebook_fragment, ff);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit(); 
        
        fgm = new FriendsGetMovies();
        //Create the Facebook Object using the app id.
        Utility.mFacebook = new Facebook(APP_ID);
        //Instantiate the asynrunner object for asynchronous api calls.
        Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

        /*
         * Get existing access_token if any
         */
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            Utility.mFacebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            Utility.mFacebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!Utility.mFacebook.isSessionValid()) {
            Utility.mFacebook.authorize(this, new String[] { "user_likes", "offline_access", "friends_likes", "friends_interests"}, new DialogListener() {
                    public void onComplete(Bundle values) {
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("access_token", Utility.mFacebook.getAccessToken());
                        editor.putLong("access_expires", Utility.mFacebook.getAccessExpires());
                        editor.commit();
                    }
    
                public void onFacebookError(FacebookError error) {}
    
                public void onError(DialogError e) {}
    
                public void onCancel() {}
            });        
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    protected void onDestroy(Bundle savedInstanceState) {
        myDbHelper.close();
    }
    private void openDatabase() {
        myDbHelper = new DataBaseHelper(this.getApplicationContext());
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
    }        

    public static DataBaseHelper getDatabaseHelper()
    {
        return myDbHelper;
    }
    
   
}
