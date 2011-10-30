package com.nextgen.facebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.FacebookError;

public class FriendsGetMovies {
    private static final String TAG = "FriendsGetMovies";
    protected static JSONArray jsonFriendsArray;
    protected static JSONArray jsonMoviesArray;
    protected static JSONArray jsonMovieDetailsArray;
    static  long friendsMoviesLikes[];
    static int movieCounter=0;
    static int totalFriends=0;
    static int remainingFriends=0;

    /*
     * callback after friends are fetched via me/friends or fql query.
     */
    
    public void RequestFriendList()
    {
        Bundle params = new Bundle();
        params.putString("fields", "name, id, picture, location");
        Utility.mAsyncRunner.request("me/friends", params, new FriendsRequestListener());
    }
    
    public class FriendsRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            long friendId=0;
            Log.w(TAG, "Got Facebook Response!!! :-) "+response);

            try {
           
                jsonFriendsArray = new JSONObject(response).getJSONArray("data");
                
                totalFriends = jsonFriendsArray.length();
                remainingFriends=totalFriends;
                
                //Request the list of movies for each friend
                for (int i=0;i<1/*totalFriends*/;i++)
                {
                 
                    friendId = jsonFriendsArray.getJSONObject(i).getLong("id");
                    Bundle params = new Bundle();
                    params.putString("fields", "name, id");
                    Utility.mAsyncRunner.request(friendId+"/movies", params, new MoviesRequestRequestListener());
                }  

            } catch (JSONException e) {
                Log.w(TAG, "JSONException!!!  "+e);
                return;
            }

            
        }
            
        public void onFacebookError(FacebookError error) {
//            dialog.dismiss();
            Log.w(TAG, "Facebook Error: "+error.getMessage());
        }
    }
    
    
    /*
     * callback after friends are fetched via me/friends or fql query.
     */
    public class MoviesRequestRequestListener extends BaseRequestListener{

        public void onComplete(final String response, final Object state) {
            long movieId=0; 
           remainingFriends--;
            Log.w(TAG, "Got Movies Response!!! Friends remaining = "+remainingFriends);
            
            runOnUiThread(new Runnable() {
                public void run() {

//                    friendsTv.setText(Integer.toString(remainingFriends));

               }
           });

            try {
                JSONArray jsonTempArray = new JSONObject(response).getJSONArray("data");

                //process this friend's movie list
                if (jsonTempArray.length() > 0)
                {
                    
                    jsonMoviesArray = concatArray(jsonMoviesArray,jsonTempArray);
                    
                    for (int i=0;i<jsonTempArray.length();i++)
                    {
                        movieId = jsonTempArray.getJSONObject(i).getLong("id");
                        String movieName = jsonTempArray.getJSONObject(i).getString("name");
//                        friendsMoviesLikes[movieCounter++] = movieId;
//                        friendsMoviesLikes[movieCounter][1] = movieCounter++;
                    }
                }

                //Request next friend's movie list
                int position = totalFriends-remainingFriends;
                if (position<50 && position >=0)
                {
                    long friendId = jsonFriendsArray.getJSONObject(totalFriends-remainingFriends).getLong("id");
                    Bundle params = new Bundle();
                    params.putString("fields", "name, id");
                    Utility.mAsyncRunner.request(friendId+"/movies", params, new MoviesRequestRequestListener());
                }
                else
                {
                    Log.w(TAG, "All friends processed :-):::"+position);
                    
                    /*Request the movie details*/
                    Bundle params = new Bundle();
                    params.putString("fields", "name, picture, link");
                    Utility.mAsyncRunner.request(Long.toString(movieId), params, new MovieDetailsRequestListener());
                }
            } catch (JSONException e) {
                Log.w(TAG, "JSONException!!!  "+e);
                return;
            }

        }
        private void runOnUiThread(Runnable runnable) {
            // TODO Auto-generated method stub
            
        }
        public void onFacebookError(FacebookError error) {
//          dialog.dismiss();
            Log.w(TAG, "Facebook Error: "+error.getMessage());
      }

  }


    /*
     * callback after friends are fetched via me/friends or fql query.
     */
    public class MovieDetailsRequestListener extends BaseRequestListener{

        public void onComplete(final String response, final Object state) {
            Log.w(TAG, "Got Movie Details Response!!!");
            

            try {
                JSONObject jsonTempObject = new JSONObject(response);

                //process this friend's movie list
                if (jsonTempObject.length() > 0)
                {
                    
//                    jsonMovieDetailsArray = concatArray(jsonMovieDetailsArray,jsonTempObject);
                    
                        String pictureUrl = jsonTempObject.getString("picture");
                        String movieName = jsonTempObject.getString("name");
                        String movieLink = jsonTempObject.getString("link");
                        
                        Log.w(TAG, "movieName="+movieName);
                        Log.w(TAG, "pictureUrl="+pictureUrl);
                        Log.w(TAG, "movieLink="+movieLink);
                }
            } catch (JSONException e) {
                Log.w(TAG, "JSONException!!!  "+e);
                return;
            }

        }
        private void runOnUiThread(Runnable runnable) {
            // TODO Auto-generated method stub
            
        }
        public void onFacebookError(FacebookError error) {
//          dialog.dismiss();
            Log.w(TAG, "Facebook Error: "+error.getMessage());
      }

  }
    
    private JSONArray concatArray(JSONArray arr1, JSONArray arr2)
    throws JSONException {
        JSONArray result = new JSONArray();
        if (arr1 != null) {
            for (int i = 0; i < arr1.length(); i++) {
                result.put(arr1.get(i));
            }
        }
        if (arr2 != null) {
            for (int i = 0; i < arr2.length(); i++) {
                result.put(arr2.get(i));
            }
        }
        return result;
    }
    
//    private JSONArray concatArray(JSONArray... arrs)
//    throws JSONException {
//        JSONArray result = new JSONArray();
//        for (JSONArray arr : arrs) {
//            for (int i = 0; i < arr.length(); i++) {
//                result.put(arr.get(i));
//            }
//        }
//        return result;
//    }
 
}
