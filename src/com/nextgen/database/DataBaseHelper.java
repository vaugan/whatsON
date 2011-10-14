package com.nextgen.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.nextgen.bemore.R;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DataBaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "DataBaseHelper";

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.nextgen.bemore/databases/";

    private static String DB_NAME = "EventData";
    private static String EVENTS_TABLE = "events";
    private static String BUY_TABLE = "buy";
    private static String BUY_REC_TABLE = "buy_recommendations";
    private static String VIEW_REC_TABLE = "view_recommendations";
    //keys for events table
    public static final String KEY_ROWID = "_id";
    public static final String KEY_EVENT_NAME = "name";
    public static final String KEY_EVENT_CHANNEL = "channel";
    public static final String KEY_EVENT_RATING = "rating";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_YOUTUBE_VIDEO_ID = "video_url";
    public static final String KEY_IMAGE_POSTER = "image_poster";
    public static final String KEY_IMAGE_BANNER = "image_banner";
    
    public static final String KEY_SHORT_DESC = "short_desc";   
    public static final String KEY_BUY_REC_ID = "buy_rec_id";   
    public static final String KEY_VIEW_REC_ID = "ev_rec_id";   
    //Keys for buy recommendations table
    public static final String KEY_BUY_REC_1 = "buy_rec_1";   
    public static final String KEY_BUY_REC_2 = "buy_rec_2";   
    public static final String KEY_BUY_REC_3 = "buy_rec_3";   
    public static final String KEY_BUY_REC_4 = "buy_rec_4";   
    public static final String KEY_BUY_REC_5 = "buy_rec_5";   
    public static final String KEY_BUY_REC_6 = "buy_rec_6";   
    public static final String KEY_BUY_REC_7 = "buy_rec_7";   
    public static final String KEY_BUY_REC_8 = "buy_rec_8";   
    public static final String KEY_BUY_REC_9 = "buy_rec_9";   
    public static final String KEY_BUY_REC_10 = "buy_rec_10";   
    //keys for buy table
    public static final String KEY_BUY_TITLE = "title";   
    public static final String KEY_BUY_DESC = "desc";   
    public static final String KEY_BUY_PRICE = "price";   
    public static final String KEY_BUY_IMAGE = "image_url";
    public static final String KEY_BUY_WEB_URL = "web_url";
    
    
    //keys for the view recommendations table
    public static final String KEY_VIEW_REC_1 = "view_rec_1";   
    public static final String KEY_VIEW_REC_2 = "view_rec_2";   
    public static final String KEY_VIEW_REC_3 = "view_rec_3";   
    public static final String KEY_VIEW_REC_4 = "view_rec_4";   
    public static final String KEY_VIEW_REC_5 = "view_rec_5";   
    public static final String KEY_VIEW_REC_6 = "view_rec_6";   
    public static final String KEY_VIEW_REC_7 = "view_rec_7";   
    public static final String KEY_VIEW_REC_8 = "view_rec_8";   
    public static final String KEY_VIEW_REC_9 = "view_rec_9";   
    public static final String KEY_VIEW_REC_10 = "view_rec_10";   

    private SQLiteDatabase myDataBase; 

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }   

  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

//        if(dbExist){
//        //do nothing - database already exist
//        }else
        {

            //By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {

            if(myDataBase != null)
                myDataBase.close();

            super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
    public Cursor fetchAllEvents() {

        return myDataBase.query(EVENTS_TABLE, new String[] {KEY_ROWID, KEY_EVENT_NAME,
                KEY_GENRE, KEY_DATE, KEY_TIME,KEY_SHORT_DESC, KEY_YOUTUBE_VIDEO_ID, KEY_IMAGE_POSTER, KEY_IMAGE_BANNER, KEY_EVENT_CHANNEL, KEY_EVENT_RATING}, null, null, null, null, null);
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
 public Cursor fetchEventsByCategory(String Category) {

     return myDataBase.query(EVENTS_TABLE, new String[] {KEY_ROWID, KEY_EVENT_NAME,
             KEY_GENRE, KEY_DATE, KEY_TIME,KEY_SHORT_DESC, KEY_YOUTUBE_VIDEO_ID, KEY_IMAGE_POSTER, KEY_IMAGE_BANNER, KEY_EVENT_CHANNEL, KEY_EVENT_RATING}, KEY_CATEGORY + " like '" + Category+"'", null, null, null, null);
 }

    /**
     * Return a Cursor positioned at the event that matches the given rowId
     * 
     * @param rowId id of event to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if event could not be found/retrieved
     */
    public Cursor fetchEvent(long rowId) throws SQLException {

        Cursor mCursor =
            myDataBase.query(true, EVENTS_TABLE, new String[] {KEY_ROWID,
                    KEY_DATE, KEY_EVENT_NAME, KEY_SHORT_DESC, KEY_YOUTUBE_VIDEO_ID, KEY_IMAGE_POSTER, KEY_EVENT_CHANNEL, KEY_EVENT_RATING}, KEY_ROWID + "='"+Long.toString(rowId)+"'", null,
                    null, null, null, null);
        
        mCursor.moveToFirst();
        return mCursor;

    }

    /**
     * Return a Cursor positioned at the event that matches the given rowId
     * 
     * @param rowId id of event to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if event could not be found/retrieved
     */
    public Cursor fetchBuyRecommendation(long rowId) throws SQLException {
        Integer buyRecRowId=0;
        Integer buyRowId1=0;
        Integer buyRowId2=0;
        Integer buyRowId3=0;
        Integer buyRowId4=0;
        Integer buyRowId5=0;
        Integer buyRowId6=0;
        Integer buyRowId7=0;
        Integer buyRowId8=0;
        Integer buyRowId9=0;
        Integer buyRowId10=0;
        
        
        /*get cursor to the event in the event table*/
        Cursor mCursor =
            myDataBase.query(true, EVENTS_TABLE, new String[] {KEY_ROWID,
                    KEY_BUY_REC_ID}, KEY_ROWID + "='"+Long.toString(rowId)+"'", null,
                    null, null, null, null);
        
        mCursor.moveToFirst();
        

            //make sure the cursor is not empty, then get the buy rec id. Then get cursor to the buy recommendations for the event.
            if (mCursor.getCount() > 0) {
                buyRecRowId= mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_ID));
                
                mCursor =
                    myDataBase.query(true, BUY_REC_TABLE, new String[] {KEY_ROWID,
                            KEY_BUY_REC_1, KEY_BUY_REC_2, KEY_BUY_REC_3, KEY_BUY_REC_4, KEY_BUY_REC_5,
                            KEY_BUY_REC_6, KEY_BUY_REC_7, KEY_BUY_REC_8, KEY_BUY_REC_9, KEY_BUY_REC_10,
                            }, KEY_ROWID + "='"+Integer.toString(buyRecRowId)+"'", null,
                            null, null, null, null);              
            }

            mCursor.moveToFirst();
            
            //make sure the cursor to the buy recommendations is not empty, then get the 1st buy recommendation
            if (mCursor.getCount() > 0) {
                buyRowId1 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_1));
                buyRowId2 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_2));
                buyRowId3 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_3));
                buyRowId4 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_4));
                buyRowId5 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_5));
                buyRowId6 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_6));
                buyRowId7 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_7));
                buyRowId8 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_8));
                buyRowId9 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_9));
                buyRowId1 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_BUY_REC_10));
 
                mCursor =
                    myDataBase.query(true, BUY_TABLE, new String[] {KEY_ROWID,
                            KEY_BUY_TITLE, KEY_BUY_DESC, KEY_BUY_PRICE,KEY_BUY_IMAGE, KEY_BUY_WEB_URL}, 
                            KEY_ROWID + "='"+buyRowId1+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId2+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId3+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId4+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId5+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId6+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId7+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId8+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId9+"'" + " or "+
                            KEY_ROWID + "='"+buyRowId10+"'",
                            null,null, null, null, null);              

//               mCursor =
//                    myDataBase.query(true, BUY_TABLE, new String[] {KEY_ROWID,
//                            KEY_BUY_TITLE, KEY_BUY_DESC, KEY_BUY_PRICE,KEY_BUY_IMAGE}, KEY_ROWID + "='"+Integer.toString(buyRowId)+"'", null,
//                            null, null, null, null);              
            }            

            mCursor.moveToFirst();
     
       //return cursor to the buy table corresponding to the 1st recommendation for this event
        return mCursor;

    }

    /**
     * Return a Cursor positioned at the event that matches the given rowId
     * 
     * @param rowId id of event to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if event could not be found/retrieved
     */
    public Cursor fetchViewRecommendation(long rowId) throws SQLException {
        Integer viewRecRowId=0;
        Integer eventRowId1=0;
        Integer eventRowId2=0;
        Integer eventRowId3=0;
        Integer eventRowId4=0;
        Integer eventRowId5=0;
        Integer eventRowId6=0;
        Integer eventRowId7=0;
        Integer eventRowId8=0;
        Integer eventRowId9=0;
        Integer eventRowId10=0;
        
        /*get cursor to the event in the event table along with the view rec column*/
        Cursor mCursor =
            myDataBase.query(true, EVENTS_TABLE, new String[] {KEY_ROWID,
                    KEY_VIEW_REC_ID}, KEY_ROWID + "='"+Long.toString(rowId)+"'", null,
                    null, null, null, null);
        
        mCursor.moveToFirst();
        

            //make sure the cursor is not empty, then get the view rec id. Then get cursor to the view recommendations for the event.
            if (mCursor.getCount() > 0) {
                viewRecRowId = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_ID));
                
                mCursor =
                    myDataBase.query(true, VIEW_REC_TABLE, new String[] {KEY_ROWID,
                            KEY_VIEW_REC_1, KEY_VIEW_REC_2, KEY_VIEW_REC_3, KEY_VIEW_REC_4, KEY_VIEW_REC_5,
                            KEY_VIEW_REC_6, KEY_VIEW_REC_6, KEY_VIEW_REC_7, KEY_VIEW_REC_8, KEY_VIEW_REC_9,KEY_VIEW_REC_10,
                            }, KEY_ROWID + "='"+Integer.toString(viewRecRowId)+"'", 
                            null,null, null, null, null);              
            }

            mCursor.moveToFirst();
            
            //make sure the cursor to the view recommendations is not empty, then get the 1st view recommendation
            if (mCursor.getCount() > 0) {
                eventRowId1 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_1));
                eventRowId2 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_2));
                eventRowId3 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_3));
                eventRowId4 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_4));
                eventRowId5 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_5));
                eventRowId6 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_6));
                eventRowId7 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_7));
                eventRowId8 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_8));
                eventRowId9 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_9));
                eventRowId10 = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHelper.KEY_VIEW_REC_10));
                
               mCursor =
                    myDataBase.query(true, EVENTS_TABLE, new String[] {KEY_ROWID,
                            KEY_DATE, KEY_EVENT_NAME, KEY_SHORT_DESC, KEY_IMAGE_POSTER}, 
                            KEY_ROWID + "='"+eventRowId1+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId2+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId3+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId4+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId5+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId6+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId7+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId8+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId9+"'" + " or "+
                            KEY_ROWID + "='"+eventRowId10+"'",
                            null,null, null, null, null);              
            }            

            mCursor.moveToFirst();
     
       //return cursor to the event table corresponding to the 1st recommendation for this event
        return mCursor;

    }

}

