package com.freedev.soufienov.newsAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 19/03/2018.
 * receives notification from alarm manager and lunches the news reader
 */

public class Alarm extends BroadcastReceiver {
Intent myIntent;Context CTX;
    String news=" eee ";
    @Override
    public void onReceive(Context context, Intent intent) {
        CTX=context;
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
String alarm_repeat=intent.getStringExtra("alarm_repeat");
int alarm_id=intent.getIntExtra("alarm_id",-1);

       // Log.e("lol",intent.getExtras().toString());
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        String day=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());Log.e("lols"," today1");
        if (alarm_repeat.contains(day.substring(0,2))||alarm_repeat.contains("Every day"))
        {Log.e("lol"," today");
            myIntent=new Intent(CTX,WakeupActivity.class);
            myIntent.putExtra("alarm_id",alarm_id);
            CTX.startActivity(myIntent);
           }
    }
   
}
