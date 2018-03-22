package com.freedev.soufienov.newsAlarm;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

/**
 * Created by user on 19/03/2018.
 */

public class NewsReader extends AppCompatActivity {
    private android.speech.tts.TextToSpeech t1;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);

        try{   t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String title=getIntent().getStringExtra("title");
                    unlockScreen();
                    t1.speak(title, TextToSpeech.QUEUE_FLUSH, null);
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


}
