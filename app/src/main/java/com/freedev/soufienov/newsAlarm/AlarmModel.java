package com.freedev.soufienov.newsAlarm;

/**
 * Created by user on 21/03/2018.
 */

public class AlarmModel {
    public static final String TABLE_NAME = "alarms";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_REPEAT = "repeat";

    private int id;

    
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

    private String repeat;
    private String time;
    private String name;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_REPEAT + " TEXT,"
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
