package com.freedev.soufienov.newsAlarm;

/**
 * Created by user on 21/03/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;



public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "alarms_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(AlarmModel.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + AlarmModel.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertAlarm(String name,String repeat,String time,String category,String language) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(AlarmModel.COLUMN_NAME, name);
        values.put(AlarmModel.COLUMN_REPEAT, repeat);
        values.put(AlarmModel.COLUMN_TIME, time);
        values.put(AlarmModel.COLUMN_CAT, category);
        values.put(AlarmModel.COLUMN_LANG, language);

        // insert row
        long id = db.insert(AlarmModel.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public long insertAlarm(AlarmModel alarmModel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(AlarmModel.COLUMN_NAME, alarmModel.getName());
        values.put(AlarmModel.COLUMN_REPEAT, alarmModel.getRepeat());
        values.put(AlarmModel.COLUMN_TIME, alarmModel.getTime());
        values.put(AlarmModel.COLUMN_CAT, alarmModel.getCategory());
        values.put(AlarmModel.COLUMN_LANG, alarmModel.getLanguage());

        // insert row
        long id = db.insert(AlarmModel.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public AlarmModel getAlarmModel(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(AlarmModel.TABLE_NAME,
                new String[]{AlarmModel.COLUMN_ID, AlarmModel.COLUMN_CAT, AlarmModel.COLUMN_TIME,AlarmModel.COLUMN_LANG,AlarmModel.COLUMN_NAME,AlarmModel.COLUMN_REPEAT},
                AlarmModel.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        AlarmModel alarmModel = new AlarmModel(
                cursor.getInt(cursor.getColumnIndex(AlarmModel.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_CAT)),
                cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_REPEAT)),
                cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_LANG)),
                cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_NAME))
               );

        // close the db connection
        cursor.close();

        return alarmModel;
    }

    public ArrayList<AlarmModel> getAllAlarms() {
        ArrayList<AlarmModel> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + AlarmModel.TABLE_NAME + " ORDER BY " +
                AlarmModel.COLUMN_TIME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AlarmModel alarmModel = new AlarmModel();
                alarmModel.setId(cursor.getInt(cursor.getColumnIndex(AlarmModel.COLUMN_ID)));
                alarmModel.setName(cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_NAME)));
                alarmModel.setTime(cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_TIME)));
                alarmModel.setLanguage(cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_LANG)));
                alarmModel.setRepeat(cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_REPEAT)));
                alarmModel.setCategory(cursor.getString(cursor.getColumnIndex(AlarmModel.COLUMN_CAT)));

                notes.add(alarmModel);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getAlarmsCount() {
        String countQuery = "SELECT  * FROM " + AlarmModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateAlarm(AlarmModel alarmModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AlarmModel.COLUMN_NAME, alarmModel.getName());
        values.put(AlarmModel.COLUMN_REPEAT, alarmModel.getRepeat());
        values.put(AlarmModel.COLUMN_TIME, alarmModel.getTime());
        values.put(AlarmModel.COLUMN_CAT, alarmModel.getCategory());
        values.put(AlarmModel.COLUMN_LANG, alarmModel.getLanguage());

        // updating row
        return db.update(AlarmModel.TABLE_NAME, values, AlarmModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(alarmModel.getId())});
    }

    public void deleteAlarm(AlarmModel alarmModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AlarmModel.TABLE_NAME, AlarmModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(alarmModel.getId())});
        db.close();
    }
}