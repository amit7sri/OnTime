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

        long newRowId = db.insert(TableName, null, values);
        Toast.makeText(context.getApplicationContext(),"Event "+DataValues.getClassName()+" Created", Toast.LENGTH_LONG).show();
        Log.d(TAG+ "INSERT", " Value : " + newRowId);
    }

    public Cursor getEventInfo(SQLiteDatabase db){
        String mQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = db.rawQuery(mQuery, null);
        Log.d(TAG,"COUNT DB"+cursor.getCount());
        return cursor;
    }

    public static ArrayList<String> getAllEventsTitle(SQLiteDatabase db){
        String mQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = db.rawQuery(mQuery, null);
        Log.d(TAG,"COUNT DB"+cursor.getCount());
        ArrayList<String> allData = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String eventName = cursor.getString(cursor.getColumnIndex(ClassName)).trim();
                allData.add(eventName);
            } while (cursor.moveToNext());
        }
        return allData;
    }
    /*public Cursor deleteEventInfo(SQLiteDatabase db, String classname){
        DataValues dataValues = new DataValues();
        String select = ClassName + "LIKE ?";
        String columns[] = {};
    }*/
}
