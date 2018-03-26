package com.freedev.soufienov.newsAlarm;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 19/03/2018.
 * this class reads news and shows wakeup screen
 * alarm is stoped or snoozed from here
 */

public class NewsReader extends AppCompatActivity {
    private android.speech.tts.TextToSpeech t1;
    private List<String> articleList = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wakedup_screen);
articleList=getIntent().getStringArrayListExtra("articles");
        Log.e("ttest",articleList.get(0));

        try{   t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    Locale lc=new Locale(getIntent().getStringExtra("language"));
                    t1.setLanguage(lc);
                    String title=getIntent().getStringExtra("title");
                    unlockScreen();
                    if(articleList.isEmpty())
                    t1.speak(title, TextToSpeech.QUEUE_FLUSH, null);
                    else
                        t1.speak(articleList.get(0), TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });

          }
        catch (Exception e){
            Log.e("ex",e.toString());}
    }
    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }
    public void snoozAlarm(View view){}
    public void stopAlarm(View view){t1.shutdown();getApplication().onTerminate();}

}
