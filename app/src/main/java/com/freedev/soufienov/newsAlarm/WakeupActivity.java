package com.freedev.soufienov.newsAlarm;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
/**
 * Created by sofiene on 31/07/2018.
 */

public  class  WakeupActivity extends AppCompatActivity implements
        RecognitionListener {
    String phrase="";
    TextView quote;
    int alarm_id,hours,mins;
    AlarmModel alarmModel;
    DatabaseHelper databaseHelper;
    private long Time_Toget_Data=30*1000;
    KeyguardManager keyguardManager;
    PowerManager powerManager;
    private SpeechRecognizer speech = null;
    String[] stopWords={"a","about","above","after","again","against","all","am","an","and","any","are","aren't","as","at","be","because","been","before","being","below","between",
            "both","but","by","can't","cannot","could","couldn't","did","didn't","do","does","doesn't","doing","don't","down","during","each","few","for","from","further","had","hadn't",
            "has","hasn't","have","haven't","having","he","he'd","he'll","he's","her","here","here's","hers","herself","him","himself","his","how","how's","i","i'd","i'll","i'm","i've",
            "if","in","into","is","isn't","it","it's","its","itself","let's","me","more","most","mustn't","my","myself","no","nor","not","of","off","on","once","only","or","other",
            "ought","our","ours","ourselves","out","over","own","same","shan't","she","she'd","she'll","she's","should","shouldn't","so","some","such","than","that","that's","the",
            "their","theirs","them","themselves","then","there","there's","these","they","they'd","they'll","they're","they've","this","those","through","to","too","under","until","up",
            "very","was","wasn't","we","we'd","we'll","we're","we've","were","weren't","what","what's","when","when's","where","where's","which","while","who","who's","whom","why","why's",
            "with","won't","would","wouldn't","you","you'd","you'll","you're","you've","your","yours","yourself","yourselves"};
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

private MediaPlayer mediaPlayer;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        powerManager   = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        Random rand = new Random();
        phrase=quotes[rand.nextInt(quotes.length)];
        setContentView(R.layout.wakedup_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        databaseHelper=new DatabaseHelper(this);
        quote=findViewById(R.id.quote);
        quote.setText(phrase);
         alarm_id=getIntent().getIntExtra("alarm_id",-1);
        alarmModel=databaseHelper.getAlarmModel(alarm_id);
mediaPlayer=new MediaPlayer();mediaPlayer.setLooping(true);
        try {
            mediaPlayer.setDataSource(getResources().openRawResourceFd(R.raw.canary));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void getSpeechInput(View view) {
        Intent intent=new Intent();
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
mediaPlayer.pause();
            if (!powerManager.isInteractive() || keyguardManager.inKeyguardRestrictedInputMode()) {
            Log.e("locked","locked");
                PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                        PowerManager.FULL_WAKE_LOCK , "TAG");
                screenLock.acquire();
                speech.startListening(intent);

            }

        else
            {


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        } }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    Log.e("result","ok");
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.e("txt",result.get(0));
                    if(distance(phrase,result.get(0))!=1){Log.e("wrong","try again");mediaPlayer.start();}
                    else {
                        mediaPlayer.stop();
                        setResult(1);
                        finish();
                    }
                    }
                    else Log.e("result","not ok");
                break;
        }
    }
    public double compareStrings(String stringA, String stringB) {
        return StringUtils.getJaroWinklerDistance(stringA, stringB);
    }
    public int distance(String text,String speech){
        text=text.replaceAll("[’‘,;.!?]", "");
        text=text.toLowerCase();
        speech=speech.toLowerCase();
        Log.e("rep",text);
        String[] text_array=text.split(" ");
        List<String> quote_bag=removeStopWords(text_array);
       return calculateDistance(quote_bag,speech);

    }
    public void snoozAlarm(View view){
        mediaPlayer.stop();
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

        int sn=alarmModel.getSnooz();
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

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        if(distance(phrase,text)!=1){mediaPlayer.start();}
        else {
            mediaPlayer.stop();
            setResult(1);
            finish();
        }

    }
    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
    public List<String> removeStopWords(String[] entry){
        List<String> resultString=new ArrayList<>();
        List<String> words= Arrays.asList(stopWords);
        int i=0;
        for ( i=0; i<entry.length;i++){
            if (!words.contains(entry[i])){
                resultString.add(entry[i]);

            }

        }
        return  resultString;
    }
    public int calculateDistance(List<String> quote,String speech){
int mainpos=-1;
        Log.e("quote",""+quote.toString());
        String[] speech_array=speech.split(" ");
int i=0;
for (i=0;i<quote.size();i++){
    List<String>   speech_list=Arrays.asList(speech_array);
    Log.e("speechList",""+speech_list.toString());
    int pos=speech_list.indexOf(quote.get(i));

    if(pos>mainpos){
        speech_array [pos]="**";
    mainpos=pos;
    Log.e("mainpos",""+mainpos);
}
else  {
        for(int j=0;j<speech_array.length;j++){
            if(compareStrings(quote.get(i),speech_array[j])<0.5)
                return 0;
            else {mainpos=j; speech_array [j]="**";}
        }

    }

}

        return 1;
    }
}
