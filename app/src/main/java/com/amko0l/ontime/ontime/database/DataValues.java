package com.amko0l.ontime.ontime.database;

/**
 * Created by Lakshmisagar on 4/19/2017.
 */

public class DataValues {
    static String className;
    String src_lat;
    static String src_long;
    static String dest_lat;
    static String dest_long;
    int mode;
    long time;
    static String hour;
    static String minutes;
    static boolean coffee;

    public DataValues(String classname,String hour, String min, boolean preference){
        this.className = classname;
        this.hour = hour;
        this.minutes = min;
        this.coffee = preference;
    }

    public DataValues(){

    }

    public static String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public static String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public static String getMin() {
        return minutes;
    }

    public void setMin(String min) {
        this.minutes = min;
    }

    public static boolean getPreference() {
        return coffee;
    }

    public void setPreference(boolean pref) {
        this.coffee = pref;
    }




    public String getSrc_lat() {
        return src_lat;
    }

    public void setSrc_lat(String src_lat) {
        this.src_lat = src_lat;
    }

    public static String getSrc_long() {
        return src_long;
    }

    public void setSrc_long(String src_long) {
        this.src_long = src_long;
    }

    public static String getDest_lat() {
        return dest_lat;
    }

    public void setDest_lat(String dest_lat) {
        this.dest_lat = dest_lat;
    }

    public static String getDest_long() {
        return dest_long;
    }

    public void setDest_long(String dest_long) {
        this.dest_long = dest_long;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
