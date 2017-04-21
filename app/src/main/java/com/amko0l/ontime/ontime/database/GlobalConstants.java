package com.amko0l.ontime.ontime.database;

/**
 * Created by Lakshmisagar on 4/19/2017.
 */

public class GlobalConstants {
    static String TableName  = "Mctable";
    static String Col_ID = "EventId";
    static String Hour = "Hour";
    static String Minute = "Minute";
    static String DestLat = "DestLatitude";
    static String DestLng = "DestLongitude";
    static String Preference = "Preference";
    static String ClassName = "className";

    static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+
            TableName +"(" + Col_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Hour + " TEXT, " + DestLat + " TEXT ,"+ ClassName + " TEXT, " + DestLng + " TEXT," + Preference + " TEXT, "
            + Minute + " TEXT );";

    static String DELETE_TABLE =  "DROP TABLE IF EXISTS " +TableName;
    static String CHECK_TABLE = "SELECT name FROM sqlite_master WHERE type='table' AND name ="+TableName;
    static boolean isDBupdated = false;

}
