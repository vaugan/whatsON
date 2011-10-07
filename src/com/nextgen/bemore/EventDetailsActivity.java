package com.nextgen.bemore;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.*;


    /**
     * This is a secondary activity, to show what the user has selected
     * when the screen is not large enough to show it all in one activity.
     */

    public class EventDetailsActivity extends FragmentActivity  {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                // If the screen is now in landscape mode, we can show the
                // dialog in-line with the list so we don't need this activity.
                finish();
                return;
            }

            if (savedInstanceState == null) {
                // During initial setup, plug in the details fragment.
                EventDetailsFragment details = new EventDetailsFragment();
                details.setArguments(getIntent().getExtras());
              getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
            }
        }
    }