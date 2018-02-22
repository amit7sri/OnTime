package com.amko0l.ontime.ontime.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static com.amko0l.ontime.ontime.database.GlobalConstants.CREATE_TABLE;
import static com.amko0l.ontime.ontime.database.GlobalConstants.ClassName;
import static com.amko0l.ontime.ontime.database.GlobalConstants.DELETE_TABLE;
import static com.amko0l.ontime.ontime.database.GlobalConstants.Days;
import static com.amko0l.ontime.ontime.database.GlobalConstants.DestLat;
import static com.amko0l.ontime.ontime.database.GlobalConstants.DestLng;
import static com.amko0l.ontime.ontime.database.GlobalConstants.Hour;
import static com.amko0l.ontime.ontime.database.GlobalConstants.Minute;
import static com.amko0l.ontime.ontime.database.GlobalConstants.Preference;
import static com.amko0l.ontime.ontime.database.GlobalConstants.TableName;


/**
 * Created by smanj on 4/19/2017.
 */

public class OnTimeDB extends SQLiteOpenHelper {

    private static String TAG = "Sneha"+OnTimeDB.class.getName().toString();
    static Context context;
    public OnTimeDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate()");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    public static void insertIntoDB(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(DestLat,DataValues.getDest_lat());
        values.put(Hour, DataValues.getHour());
        values.put(ClassName, DataValues.getClassName());
        values.put(DestLng, DataValues.getDest_long());
        values.put(Preference,DataValues.getPreference());
        values.put(Minute, DataValues.getMin());
        values.put(Days, DataValues.getList().toString());

        long newRowId = db.insert(TableName, null, values);
        Toast.makeText(context.getApplicationContext(),"Event "+DataValues.getClassName()+" Created", Toast.LENGTH_LONG).show();
        Log.d(TAG+ "INSERT", " Value : " + newRowId);

        //getAllDataToFile(db);
    }

    /*public static void getAllDataToFile(SQLiteDatabase db){
        String mQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = db.rawQuery(mQuery, null);
        Log.d(TAG,"COUNT DB"+cursor.getCount());

        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Download");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "trainingdata.txt");
            if(gpxfile.exists()) {
                gpxfile.delete();
                Log.d(TAG, "file deleted");
                gpxfile = new File(root, "trainingdata.txt");
            }
            FileWriter writer = new FileWriter(gpxfile);
            if (cursor.moveToFirst()) {
                int rowCount=0;
                while(!cursor.isAfterLast()){
                    String tempActivity = cursor.getString(cursor.getColumnIndex(ActivityLabel)).trim();
                    StringBuilder mActivityRow = new StringBuilder();
                    if(tempActivity.equals("Walking")) {
                        mActivityRow.append("1");
                    }else if(tempActivity.equals("Running")) {
                        mActivityRow.append("2");
                    }else if(tempActivity.equals("Eating")) {
                        mActivityRow.append("3");
                    }
                    rowCount++;
                    int j = 0;

                    for (int i = 0; i < 50; i++) {
                        String tempx = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_X + i));
                        String tempy = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_Y + i));
                        String tempz = (++j) + ":" + cursor.getDouble(cursor.getColumnIndex(Accel_Z + i));
                        mActivityRow.append(" " + tempx + " " + tempy + " " + tempz);
                    }
                    Log.d(TAG,mActivityRow.toString());
                    writer.write(mActivityRow.toString());
                    writer.write("\n");
                    //}
                    cursor.moveToNext();
                }
                //Toast.makeText(context.getApplicationContext(), rowCount, Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            writer.close();
        } catch (IOException e) {
            Log.d(TAG,"Error getAllDataToFile"+e.toString());
            e.printStackTrace();
        }
    }
*/
    public Cursor getEventInfo(SQLiteDatabase db){
        String mQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = db.rawQuery(mQuery, null);
        Log.d(TAG,"COUNT DB"+cursor.getCount());
        return cursor;
    }

    public static ArrayList<ArrayList<String>> getAllEventsTitle(SQLiteDatabase db){
        String mQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = db.rawQuery(mQuery, null);
        Log.d(TAG,"COUNT DB"+cursor.getCount());
        ArrayList<ArrayList<String>> allData = new ArrayList<ArrayList<String>>();
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> detail = new ArrayList<String>();
                String eventName = cursor.getString(cursor.getColumnIndex(ClassName)).trim();
                String eventHr = cursor.getString(cursor.getColumnIndex(Hour)).trim();
                String eventMin = cursor.getString(cursor.getColumnIndex(Minute)).trim();
                String days = cursor.getString(cursor.getColumnIndex(Days)).trim();
                detail.add(eventName);
                detail.add(eventHr);
                detail.add(eventMin);
                detail.add(days);

                allData.add(detail);
            } while (cursor.moveToNext());
        }
        return allData;
    }
    // LS ADDED
    public static void deleteEvent(SQLiteDatabase db, String title){

        String mQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = db.rawQuery(mQuery, null);
        Log.d(TAG,"COUNT DB"+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                String eventName = cursor.getString(cursor.getColumnIndex(ClassName)).trim();
                Log.d(TAG, "eventName" + eventName);
            } while (cursor.moveToNext());
        }

        db.delete(TableName, ClassName + "='" + title+"'", null) ;
    }

    /*public Cursor deleteEventInfo(SQLiteDatabase db, String classname){
        DataValues dataValues = new DataValues();
        String select = ClassName + "LIKE ?";
        String columns[] = {};
    }*/
}
