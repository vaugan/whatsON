package com.nextgen.facebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.FacebookError;
import com.nextgen.bemore.FacebookFragment;

public class FriendsGetMovies {
    private static final String TAG = "FriendsGetMovies";
    protected static JSONArray jsonFriendsArray;
    protected static JSONArray jsonMoviesArray;
    protected static JSONArray jsonMovieDetailsArray;
    protected static JSONArray jsonMoviesDedupedArray;
//    protected MovieDetails[] myMoviesList = new MovieDetails[20];
    public static MovieDetails[] myMoviesList;
    public static MovieDetails[] myTop20Movies;
    static int movieCounter=0;
    public static int totalFriends=0;
    static int totalMovies=0;
    static int detailsMoviesCtr=0;
    public static int remainingFriends=0;
    public static final int MAX_MOVIES  = 200;
    public static final int NUM_OF_FRIENDS = 20;

    /*
     * callback after friends are fetched via me/friends or fql query.
     */
    
    public void RequestFriendList()
    {
        Bundle params = new Bundle();
        params.putString("fields", "name, id, picture, location");
        Log.w("TAG_-------------------------------------", "START");
        Utility.mAsyncRunner.request("me/friends", params, new FriendsRequestListener());
        Log.w("TAG_-------------------------------------", "END");
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

		@Override
		public void inProgress(boolean value) {
			
			if (value) {
				FacebookFragment.f.notifyFacebookApp(value);
			}
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
                }

                //Request next friend's movie list
                int position = totalFriends-remainingFriends;
                if (position< NUM_OF_FRIENDS && position >=0)
                {
                    long friendId = jsonFriendsArray.getJSONObject(totalFriends-remainingFriends).getLong("id");
                    Bundle params = new Bundle();
                    params.putString("fields", "name, id, category");
                    Utility.mAsyncRunner.request(friendId+"/movies", params, new MoviesRequestRequestListener());
                }
                else
                {
                    Log.w(TAG, "All friends processed :-):::"+position);
                    
                    addMoviesToArray();
                    FillTop20Movies();
                    RequesMovieDetails();
                    
                    Log.w(TAG, "Movies sorted! ");
                    
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
		@Override
		public void inProgress(boolean value) {
			if (value) {
				FacebookFragment.f.notifyFacebookApp(value);
			}
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
                long id = jsonTempObject.getLong("id");
                String category = jsonTempObject.getString("category");
                
                //populate these details into myMoviesList
                if (jsonTempObject.length() > 0)
                {
                    for(int i=0;i<myTop20Movies.length;i++)
                    {
                        if ((myTop20Movies[i].id == id) && (category.equals("Movie")))
                        {
                            myTop20Movies[i].pictureUrl = jsonTempObject.getString("picture");
                            myTop20Movies[i].link = jsonTempObject.getString("link");
                            myTop20Movies[i].category=category;
                            Log.w(TAG, "myTop20Movies["+i+"].pictureUrl"+myTop20Movies[i].pictureUrl);
                            Log.w(TAG, "myTop20Movies["+i+"].link"+myTop20Movies[i].link);
                            Log.w(TAG, "myTop20Movies["+i+"].category="+myTop20Movies[i].category+" Likes="+myTop20Movies[i].likes);
                            break;
                        }
                    }
                      
                }
            } catch (JSONException e) {
                Log.w(TAG, "JSONException!!!  "+e);
            }

            //Request next movie details
            if (++detailsMoviesCtr <20)
            {
               Bundle params = new Bundle();
               params.putString("fields", "name, picture, link, category");
               Utility.mAsyncRunner.request(Long.toString(myTop20Movies[detailsMoviesCtr].id), params, new MovieDetailsRequestListener());
            }
            else
            {
                Log.w(TAG, "All Top20 movies' details retrieved OK");
            }

        }
        
        private void runOnUiThread(Runnable runnable) {
            // TODO Auto-generated method stub
            
        }
        public void onFacebookError(FacebookError error) {
//          dialog.dismiss();
            Log.w(TAG, "Facebook Error: "+error.getMessage());
      }

		@Override
		public void inProgress(boolean value) {
            Log.w(TAG, " value: "+ value + "  ,remainingFriends = "+ remainingFriends +
            		" totalFriends-remainingFriends="+(totalFriends-remainingFriends) + " detailsMoviesCtr= "+detailsMoviesCtr);
			if (!value && totalFriends-remainingFriends == NUM_OF_FRIENDS && detailsMoviesCtr == (NUM_OF_FRIENDS-1)) {
				FacebookFragment.f.notifyFacebookApp(value);
			}
			
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
    
    private void addMoviesToArray()
    {
        boolean found=false;
        long movieId=0;
        String movieName;
        int mCtr=0;
        
        //create and initialise array to worst case size, which is same as json array size.
        totalMovies = jsonMoviesArray.length()>MAX_MOVIES?MAX_MOVIES:jsonMoviesArray.length();
        
        Log.w(TAG, "jsonMoviesArray.length()="+jsonMoviesArray.length());

        myMoviesList = new MovieDetails[totalMovies];
        for(int i = 0; i < myMoviesList.length; i++) {
            myMoviesList[i] = new MovieDetails();
         }
        
        /*From the full Json list of movies, which includes duplicates, we populate the myMoviesList. 
         * Remove duplicates, but for each duplicate, increase the number of likes for that movie.*/
        for (int i=0;i<jsonMoviesArray.length();i++)
        {
            found=false;

            try {
                movieId = jsonMoviesArray.getJSONObject(i).getLong("id");
                movieName = jsonMoviesArray.getJSONObject(i).getString("name");
//                Log.w(TAG, "jsonMoviesArray["+i+"] =  "+movieId+" "+ movieName);

                for(int j=0;j<myMoviesList.length;j++)
                {
                    mCtr = i;
                        
                    if(myMoviesList[j].id == movieId)
                    {
                        //increment likes
                        myMoviesList[j].likes++;
                        found=true;
                        Log.w(TAG, "mCtr="+mCtr+" Existing movie["+movieName+"], increasing likes to[ "+myMoviesList[j].likes);
                        break;
                    }
                }
                if (found == false)
                {
                     if (mCtr < totalMovies)
                     {
                         Log.w(TAG, "mCtr="+mCtr+" Adding new movie: "+movieName);
                       myMoviesList[mCtr].id = movieId;
                       myMoviesList[mCtr].name = movieName;
                       myMoviesList[mCtr].likes = 1;
                     }
                     else
                     {
                         Log.w(TAG, "mCtr="+mCtr+" is larger than the max movie list size, so ignoring movie...");
                     }
                }

            } catch (JSONException e) {
                Log.w(TAG, "JSONException!!!  "+e);
                return;
            }
        }

        //print the movie list with the updated number of likes
        for(int i=0;i<myMoviesList.length;i++)
        {
            Log.w(TAG, "myMoviesList["+i+"]="+" "+myMoviesList[i].id+" likes="+myMoviesList[i].likes+" "+myMoviesList[i].name);
            Log.w(TAG, "myMoviesList["+i+"].pictureUrl="+myMoviesList[i].pictureUrl);
        }
    }
    
    private void RequesMovieDetails()
    {


            Bundle params = new Bundle();
              params.putString("fields", "name, picture, link, category");
              Utility.mAsyncRunner.request(Long.toString(myTop20Movies[0].id), params, new MovieDetailsRequestListener());
  
    }

    private void FillTop20Movies() {
        
        myTop20Movies = new MovieDetails[20];
        
                for (int j=0; j<myMoviesList.length; j++)
                {
                    for (int i=j+1; i<myMoviesList.length; i++)
                    if (myMoviesList[j].likes<myMoviesList[i].likes){
                        MovieDetails temp=myMoviesList[i];
                                        myMoviesList[i]=myMoviesList[j];
                                        myMoviesList[j]=temp;
                    }
                }
                
                Log.w(TAG, "myMoviesList Sorted by descending likes::");
                for (int i=0; i<myMoviesList.length; i++) 
                {
                    Log.w(TAG, "myMoviesList["+i+"] name = "+myMoviesList[i].name+" Likes= "+myMoviesList[i].likes);
                }

                Log.w(TAG, "FillTop20Movies:");
                for (int i=0;i<20;i++)
                {
                        myTop20Movies[i] = myMoviesList[i];
                        Log.w(TAG, "myTop20Movies["+i+"] name = "+myTop20Movies[i].name+" Likes= "+myTop20Movies[i].likes);
                }

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

    public static String getMovieUrl(int position)
    {
        return myTop20Movies[position].link;
    }
    
    class MovieDetails{
    long id;
    int likes;
    String name;
    String pictureUrl;
    String link;
    String descr;
    String category;
    
    
    }
    
    
}
