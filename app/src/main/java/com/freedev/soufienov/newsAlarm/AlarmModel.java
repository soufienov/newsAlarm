package com.freedev.soufienov.newsAlarm;

import android.util.Log;

/**
 * Created by user on 21/03/2018.
 */

public class AlarmModel {
    public static final String TABLE_NAME = "alarms";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_REPEAT = "repeat";
    public static final  String COLUMN_ACTIVE ="active";
    public static final String COLUMN_SNOOZED = "snoozed";
    public  static  final  String COLUMN_SNOOZTIME="snoozTime";

    private int id;
    private int snoozTime;
    private String repeat;
    private String time;
    private String name;
    private boolean active=true,snoozed=false;
    public int getSnoozTime() {
        Log.e("tm",snoozTime+""+isActive()+isSnoozed()+"");
        return snoozTime;
    }

    public void setSnoozTime(int snoozTime) {
        this.snoozTime = snoozTime;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSnoozed() {
        return snoozed;
    }

    public void setSnoozed(boolean snoozed) {
        this.snoozed = snoozed;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }





    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_REPEAT + " TEXT,"
                    +COLUMN_ACTIVE+"BOOLEAN,"
                    +COLUMN_SNOOZED+"BOOLEAN,"
                    +COLUMN_SNOOZTIME+"INTEGER,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_TIME + " TEXT"
                    + ")";

    public AlarmModel() {
    }

    public AlarmModel(int id, String time,String repeat,String name) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.repeat = repeat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setId(int id) {
        this.id = id;
    }

}
