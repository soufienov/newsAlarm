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


    @Override
    public void onReceive(Context context, Intent intent) {

  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
String alarm_repeat=intent.getStringExtra("alarm_repeat");
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        String day=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        if (alarm_repeat.contains(day)||alarm_repeat.contains("Every day"))
        { intent.setClass(context,NewsReader.class);
  context.startActivity(intent);}
  else Log.e("lol","not today");
    }

}
