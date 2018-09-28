package com.freedev.soufienov.newsAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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
    BottomSheetFragment bottomSheetFragment;
    TimePicker timePicker;
    AlarmModel alarmModel;
    DatabaseHelper databaseHelper;
    Spinner snoozSpinner;
    Intent intent;
    EditText label;
RadioButton norepeat,everyDayRepeat,weekRepeat,customRepeat;
    private String language="English",id,every_day="Every day",monday="",tuesday="",wednesday="",thursday="",friday="",saturday="",sunday="";
    private String repeat;
    private long Time_Toget_Data=30*1000;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);
        getViews();
        databaseHelper=new DatabaseHelper(this);

        intent=getIntent();

        timePicker.setIs24HourView(true);
        days.setText(every_day);

        try
        {id=intent.getStringExtra("id");
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

                    alarmModel.setSnoozTime((snoozSpinner.getSelectedItemPosition()));
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
        catch (Exception e){        alarmModel=new AlarmModel(0,null,null,"first");
        }
        snoozSpinner.setSelection(alarmModel.getSnoozTime());


       }

  private void getViews(){
      save= findViewById(R.id.save);
      days= findViewById(R.id.days);
      timePicker= findViewById(R.id.timePicker4);
      snoozSpinner=findViewById(R.id.spinnerSnooz);
      label=findViewById(R.id.labeltxt);
  }

    public void popDays(View view) {

       bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
   }

    public void noRepeat(View view){
        norepeat=(RadioButton)view;
        if(norepeat.isChecked()){

            every_day="";monday="";tuesday="";wednesday="";thursday="";friday="";saturday="";sunday="";
        }
        alarmModel.setRepeat(getRepeatString());
        days.setText(getRepeatString());

    }
    public void everyDayRepeat(View view){
        everyDayRepeat=(RadioButton)view;
        if(everyDayRepeat.isChecked()){

            every_day="Every day";monday="";tuesday="";wednesday="";thursday="";friday="";saturday="";sunday="";
        }
        else {every_day="";}
        alarmModel.setRepeat(getRepeatString());
        days.setText(getRepeatString());

    }
    public void weekRepeat(View view){ weekRepeat=(RadioButton)view;
        if(weekRepeat.isChecked()){
            every_day="";  monday="Mon,";tuesday="Tue,";wednesday="Wed,";thursday="Thu,";friday="Fri,";
        }
        else {every_day="";monday="";tuesday="";wednesday="";thursday="";friday="";saturday="";sunday="";}
        alarmModel.setRepeat(getRepeatString());
        days.setText(getRepeatString());

    }
    public void customRepeat(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose days");
        builder.setPositiveButton("OK",null);
        builder.setNegativeButton("Cancel",null);
        builder.setView(R.layout.days_layout);

          builder.show();
        every_day="";monday="";tuesday="";wednesday="";thursday="";friday="";saturday="";sunday="";
        days.setText("No repeat");

builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
    @Override
    public void onCancel(DialogInterface dialog) {
        monday="";tuesday="";wednesday="";thursday="";friday="";saturday="";sunday="";    }
});
builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
    @Override
    public void onDismiss(DialogInterface dialog) {
        bottomSheetFragment.dismiss();

    }
});
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
               case R.id.monday:
                if (checked){monday="Mon,";}
                else
                   monday="";
                    break;
            case R.id.tuesday:
                if (checked){tuesday="Tue,";}
                // Cheese me
                else
                    tuesday="";
                break; case R.id.wednesday:
                if (checked){wednesday="Wed,";}
                // Cheese me
                else
                    wednesday="";
                break; case R.id.thursday:
                if (checked){thursday="Thu,";}
                // Cheese me
                else
                    thursday="";
                break; case R.id.friday:
                if (checked){friday="Fri,";}
                // Cheese me
                else
                    friday="";
                break;
                // TODO: Veggie sandwich
            case R.id.saturday:
                if (checked){saturday="Sat,";}
                // Cheese me
                else
                    saturday="";
                break;
                case R.id.sunday:
                if (checked){sunday="Sun";}
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

        String timeString=""+String.format("%02d", timePicker.getHour())+":"+String.format("%02d", timePicker.getMinute());
      alarmModel.setRepeat(getRepeatString());
      days.setText(getRepeatString());
      alarmModel.setTime(timeString);
      alarmModel.setName(label.getText().toString());
     int id=(int) databaseHelper.insertAlarm(alarmModel);
     alarmModel.setId(id);
        TextView tv= (TextView) snoozSpinner.getSelectedView();
alarmModel.setSnoozTime(Integer.parseInt(tv.getText().toString()));
        browserIntent.putExtra("alarm_id",id);
browserIntent.putExtra("alarm_repeat",repeat);

        PendingIntent pi= PendingIntent.getBroadcast(getApplicationContext(),id,browserIntent,0);
        setAlarmManager(pi);this.setResult(1);
finish();
    }
private String getRepeatString(){if(!every_day.isEmpty()) repeat=every_day;
else repeat=monday+tuesday+wednesday+thursday+friday+saturday+sunday;
    if (repeat.endsWith(",")) repeat=repeat.substring(0,repeat.length()-1);

    return  repeat;}
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
