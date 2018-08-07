package com.freedev.soufienov.newsAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

/**
 * Created by sofiene on 31/07/2018.
 */

public  class  WakeupActivity extends AppCompatActivity {
    String phrase="";
    TextView quote;
    int alarm_id,hours,mins;
    AlarmModel alarmModel;
    DatabaseHelper databaseHelper;
    private long Time_Toget_Data=30*1000;

    String[] quotes={"The Way Get Started Is To Quit Talking And Begin Doing.","The Pessimist Sees Difficulty In Every Opportunity. The Optimist Sees Opportunity In Every Difficulty",
    "Don’t Let Yesterday Take Up Too Much Of Today","You Learn More From Failure Than From Success. Don’t Let It Stop You. Failure Builds Character.",
    "It’s Not Whether You Get Knocked Down, It’s Whether You Get Up.","If You Are Working On Something That You Really Care About, You Don’t Have To Be Pushed. The Vision Pulls You.",
    "Failure Will Never Overtake Me If My Determination To Succeed Is Strong Enough.","Entrepreneurs Are Great At Dealing With Uncertainty And Also Very Good At Minimizing Risk. That’s The Classic Entrepreneur.",
    "We May Encounter Many Defeats But We Must Not Be Defeated.","Knowing Is Not Enough We Must Apply. Wishing Is Not Enough We Must Do.",
    "Imagine Your Life Is Perfect In Every Respect; What Would It Look Like?","We Generate Fears While We Sit. We Overcome Them By Action.",
    "Whether You Think You Can Or Think You Can’t, You’re Right.","Security Is Mostly A Superstition. Life Is Either A Daring Adventure Or Nothing.",
    "The Man Who Has Confidence In Himself Gains The Confidence Of Others.","The Only Limit To Our Realization Of Tomorrow Will Be Our Doubts Of Today.",
    "Creativity Is Intelligence Having Fun.","What You Lack In Talent Can Be Made Up With Desire, Hustle And Giving 110% All The Time.",
    "Do What You Can With All You Have, Wherever You Are.","Develop An ‘Attitude Of Gratitude’. Say Thank You To Everyone You Meet For Everything They Do For You.",
    "You Are Never Too Old To Set Another Goal Or To Dream A New Dream.","To See What Is Right And Not Do It Is A Lack Of Courage.",
    "Reading Is To The Mind, As Exercise Is To The Body.","Fake It Until You Make It! Act As If You Had All The Confidence You Require Until It Becomes Your Reality."};
    private AlarmManager alarmManager;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        Random rand = new Random();
        phrase=quotes[rand.nextInt(quotes.length)];
        setContentView(R.layout.wakedup_screen);
        databaseHelper=new DatabaseHelper(this);
        quote=findViewById(R.id.quote);
        quote.setText(phrase);
         alarm_id=getIntent().getIntExtra("alarm_id",-1);
        Log.e("wake",alarm_id+"");
        alarmModel=databaseHelper.getAlarmModel(alarm_id);

    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.e("txt",result.get(0));

                    if(distance(phrase,result.get(0))>1){Log.e("wrong","try again");}
                }
                break;
        }
    }

    public int distance(String text,String speech){
        text=text.replaceAll("[’‘,;.!?]", "");
        Log.e("rep",text);
        int diff=0,i=0;
        String[] text_array=text.split(" ");
        String[] speech_array=speech.split(" ");
        int text_lenght=text_array.length;
        int speech_lenght=speech_array.length;
        int LENGTH=text_lenght-speech_lenght;
        if (LENGTH<-1||LENGTH>1) return 2;
        else if(text_lenght>speech_lenght) LENGTH=speech_lenght;
                else LENGTH=text_lenght;
            while (i<LENGTH){
                if (text_array[i].compareToIgnoreCase(speech_array[i])!=0) diff++;
            i++;}
        return diff;
    }
    public void snoozAlarm(View view){
alarmModel=addSnoozTime(alarmModel);
alarmModel.setId(0);
alarmModel.setSnoozed(true);
databaseHelper.insertAlarm(alarmModel);
        String tit= "wake up you are late, if u don't i will continue to say rubbish please wakeup now!!";
        Intent browserIntent = new Intent(this,Alarm.class);
        browserIntent.putExtra("title",tit);
        browserIntent.putExtra("alarm_id",alarmModel.getId());
        browserIntent.putExtra("alarm_repeat",alarmModel.getRepeat());

        PendingIntent pi= PendingIntent.getBroadcast(getApplicationContext(),alarmModel.getId(),browserIntent,0);
        setAlarmManager(pi);
        setResult(1);finish();
    }
    public AlarmModel addSnoozTime(AlarmModel alarmModel){
        String time=alarmModel.getTime();
         hours=Integer.parseInt(time.substring(0,2));
         mins=Integer.parseInt(time.substring(3,5));
        Log.e("h1:",""+hours);
        Log.e("m1:",""+mins);

        int sn=alarmModel.getSnoozTime();
        Log.e("sn:",""+sn);

        int sm=sn+mins;
        if(sm>=60){hours++;mins=sm-60;}
        else mins=sm;
        String timeString=""+String.format("%02d", hours)+":"+String.format("%02d", mins);
        alarmModel.setTime(timeString);
        Log.e("h2:",""+hours);
        Log.e("m2:",""+mins);

        return alarmModel;
    }
    private void setAlarmManager(PendingIntent pi){

        Log.e("h:",""+hours);
        Log.e("m:",""+mins);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, mins);
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-Time_Toget_Data,
                AlarmManager.INTERVAL_DAY, pi);
    }
}
