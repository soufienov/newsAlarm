package com.freedev.soufienov.newsAlarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 21/03/2018.
 * New alarms are set from here
 * Editing alarms is done from this class
 */

public class NewsAlarm extends Activity{
    Button info,lang,save;
    String info_category="General";
    AlarmManager alarmManager;
    TimePicker timePicker;
    AlarmModel alarmModel;
    DatabaseHelper databaseHelper;
    Intent intent;
CharSequence[] infos={"General","Sports","Entertainment","Business","Sciences","Technology"};
CharSequence[] langs={"English","French","Spanish","Germain","Chinese","Japanese"};
    String[]countries= new String[]{"gb","us","ar","au","at","be","br",
            "bg","ca","cn","co","cu","cz","eg","fr","de","gr",
            "hu","in","id","il","it","jp","mx","nl","ng","pt","pl",
            "ro","ru","sa","kr","ch","tw",
            "th","tr","ae","ua","ve"};
    Map<String, String> map = new HashMap<String, String>();

    private String language="English",id,every_day="",monday="",tuesday="",wednesday="",thursday="",friday="",saturday="",sunday="";
    private String repeat,alarm_country;
    private long Time_Toget_Data=30*1000;
    private String countryCode;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);
        map.put("English","gb,us,au,in,ng");
        map.put("Spanish","co,mx,ve,pt,br");
        map.put("French","fr,br,ca");
        map.put("German","de,at");
        map.put("Chinese","ch");
        map.put("Japanese","jp");
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        countryCode = tm.getSimCountryIso();
        databaseHelper=new DatabaseHelper(this);
        info= findViewById(R.id.info);
        lang= findViewById(R.id.lang);
        save= findViewById(R.id.save);
        intent=getIntent();
        timePicker= findViewById(R.id.timePicker4);
        timePicker.setIs24HourView(true);

        try
        {id=intent.getStringExtra("id");
           // Log.e("editing",id);
          alarmModel= databaseHelper.getAlarmModel(Long.parseLong(id));
          info_category=alarmModel.getCategory();
          info.setText(info_category);
          language=alarmModel.getLanguage();
          lang.setText(language);
          String time=alarmModel.getTime();
          timePicker.setHour(Integer.parseInt(time.substring(0,2)));
          timePicker.setMinute(Integer.parseInt(time.substring(3,5)));
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String timeString=""+String.format("%02d", timePicker.getHour())+":"+String.format("%02d", timePicker.getMinute());
                    alarmModel.setCategory(info_category);
                    alarmModel.setLanguage(language);
                    setAlarmCountry();
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

                    browserIntent.putExtra("category",info_category);
                    browserIntent.putExtra("alarm_country",alarm_country);
                    browserIntent.putExtra("language",language);
                    PendingIntent pi= PendingIntent.getBroadcast(getApplicationContext(),id,browserIntent,0);
                    setAlarmManager(pi);

                    setResult(1);finish();

                }
            });
        }
        catch (Exception e){}


       }

    private void setAlarmCountry() {
        alarm_country=map.get(language);
        if(alarm_country.contains(countryCode)){alarm_country=countryCode;}
        else alarm_country=alarm_country.substring(0,2);
    }

    public void popDays(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose days");
        builder.setPositiveButton("OK",null);
        builder.setNegativeButton("Cancel",null);
        builder.setView(R.layout.days_layout);
        builder.show();
    }
    public void popInfo(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a category");
        builder.setPositiveButton("OK",null);
        builder.setNegativeButton("Cancel",null);
        builder.setItems(infos, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 info_category=infos[which].toString();
                info.setText(info_category);
            }
        });        builder.show();
    }
    public void popLang(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a category");
        builder.setPositiveButton("OK",null);
        builder.setNegativeButton("Cancel",null);
        builder.setItems(langs, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                language=langs[which].toString();
                lang.setText(language);
            }
        });        builder.show();
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.every_day:
                if (checked){every_day="Every day";}
                // Put some meat on the sandwich
                else
                    every_day="";
                    break;
            case R.id.monday:
                if (checked){monday="Monday,";}
                // Cheese me
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
      alarmModel=new AlarmModel(0,info_category,timeString,repeat,language,"first");
     int id=(int) databaseHelper.insertAlarm(alarmModel);
     alarmModel.setId(id);
        setAlarmCountry();

        browserIntent.putExtra("alarm_id",id);
browserIntent.putExtra("alarm_repeat",repeat);
browserIntent.putExtra("category",info_category);
browserIntent.putExtra("alarm_country",alarm_country);
        browserIntent.putExtra("language",language);

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
