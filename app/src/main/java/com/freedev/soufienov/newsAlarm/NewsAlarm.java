package com.freedev.soufienov.newsAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by user on 21/03/2018.
 * New alarms are set from here
 * Editing alarms is done from this class
 */

public class NewsAlarm extends AppCompatActivity {
    Button save,days;
    AlarmManager alarmManager;
    TimePicker timePicker;
    AlarmModel alarmModel;
    DatabaseHelper databaseHelper;
    Intent intent;
RadioButton norepeat,everyDayRepeat,weekRepeat,customRepeat;
RadioGroup radioGroup;
    private String language="English",id,every_day="Every day",monday="",tuesday="",wednesday="",thursday="",friday="",saturday="",sunday="";
    private String repeat;
    private long Time_Toget_Data=30*1000;
    private String countryCode;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        countryCode = tm.getSimCountryIso();
        databaseHelper=new DatabaseHelper(this);
        save= findViewById(R.id.save);
        days= findViewById(R.id.days);
        intent=getIntent();
        timePicker= findViewById(R.id.timePicker4);
        timePicker.setIs24HourView(true);
        days.setText(every_day);
        try
        {id=intent.getStringExtra("id");
           // Log.e("editing",id);
          alarmModel= databaseHelper.getAlarmModel(Long.parseLong(id));
          String time=alarmModel.getTime();
          timePicker.setHour(Integer.parseInt(time.substring(0,2)));
          timePicker.setMinute(Integer.parseInt(time.substring(3,5)));
          days.setText(alarmModel.getRepeat());
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String timeString=""+String.format("%02d", timePicker.getHour())+":"+String.format("%02d", timePicker.getMinute());
                    alarmModel.setTime(timeString);
                    if(!every_day.isEmpty()) repeat=every_day;
                    else repeat=monday+tuesday+wednesday+thursday+friday+saturday+sunday;
                    if (repeat.endsWith(",")) repeat=repeat.substring(0,repeat.length()-1);
                    alarmModel.setRepeat(repeat);
                    int id= databaseHelper.updateAlarm(alarmModel);
                    //ecraser l'intent
                    String tit= "wake up you are late, if u don't i will continue to say rubbish please wakeup now!!";
                    Intent browserIntent = new Intent(NewsAlarm.this,Alarm.class);
                    browserIntent.putExtra("title",tit);
                    browserIntent.putExtra("alarm_id",id);
                    browserIntent.putExtra("alarm_repeat",repeat);

                    PendingIntent pi= PendingIntent.getBroadcast(getApplicationContext(),id,browserIntent,0);
                    setAlarmManager(pi);

                    setResult(1);finish();

                }
            });
        }
        catch (Exception e){}


       }

  

    public void popDays(View view) {

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
   }

    public void noRepeat(){}
    public void everyDayRepeat(){}
    public void weekRepeat(){}
    public void customRepeat(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose days");
        builder.setPositiveButton("OK",null);
        builder.setNegativeButton("Cancel",null);
        builder.setView(R.layout.days_layout);
        builder.show();

    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
               case R.id.monday:
                if (checked){monday="Monday,";}
                else
                   monday="";
                    break;
            case R.id.tuesday:
                if (checked){tuesday="Tuesday,";}
                // Cheese me
                else
                    tuesday="";
                break; case R.id.wednesday:
                if (checked){wednesday="Wednesday,";}
                // Cheese me
                else
                    wednesday="";
                break; case R.id.thursday:
                if (checked){thursday="Thursday,";}
                // Cheese me
                else
                    thursday="";
                break; case R.id.friday:
                if (checked){friday="Friday,";}
                // Cheese me
                else
                    friday="";
                break;
                // TODO: Veggie sandwich
            case R.id.saturday:
                if (checked){saturday="Saturday,";}
                // Cheese me
                else
                    saturday="";
                break;
                case R.id.sunday:
                if (checked){sunday="Sunday";}
                // Cheese me
                else
                    sunday="";
                break;
        }
        String days_text=monday+" "+tuesday+" "+wednesday+" "+thursday+" "+friday+" "+saturday+" "+sunday;
        days_text=days_text.replaceAll("  "," ");
        if(days_text.length()>0)
        {days.setText(days_text); every_day="";}
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setAlarmClock(View v){
        String tit= "wake up you are late, if u don't i will continue to say rubbish please wakeup now!!";
        Intent browserIntent = new Intent(NewsAlarm.this,Alarm.class);
        browserIntent.putExtra("title",tit);
if(!every_day.isEmpty()) repeat=every_day;
else repeat=monday+tuesday+wednesday+thursday+friday+saturday+sunday;
if (repeat.endsWith(",")) repeat=repeat.substring(0,repeat.length()-1);
        String timeString=""+String.format("%02d", timePicker.getHour())+":"+String.format("%02d", timePicker.getMinute());
      alarmModel=new AlarmModel(0,timeString,repeat,"first");
     int id=(int) databaseHelper.insertAlarm(alarmModel);
     alarmModel.setId(id);

        browserIntent.putExtra("alarm_id",id);
browserIntent.putExtra("alarm_repeat",repeat);

        PendingIntent pi= PendingIntent.getBroadcast(getApplicationContext(),id,browserIntent,0);
        setAlarmManager(pi);this.setResult(1);
finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarmManager(PendingIntent pi){
         int hours =timePicker.getHour();
        int mins =timePicker.getMinute();
        Log.e("h:",""+hours);
        Log.e("m:",""+mins);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-Time_Toget_Data,
                AlarmManager.INTERVAL_DAY, pi);
    }
    }
