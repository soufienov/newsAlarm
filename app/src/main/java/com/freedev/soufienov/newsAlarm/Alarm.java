package com.freedev.soufienov.newsAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by user on 19/03/2018.
 */

public class Alarm extends BroadcastReceiver {

    private Object wd;

    @Override
    public void onReceive(Context context, Intent intent) {

  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
  /*intent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
          WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
          WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
          WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);*/
  intent.setClass(context,NewsReader.class);
  context.startActivity(intent);
       // Log.e("tt","pp");
    }
}
