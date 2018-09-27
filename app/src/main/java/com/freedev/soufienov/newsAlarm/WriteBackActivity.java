package com.freedev.soufienov.newsAlarm;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by user on 27/09/2018.
 */

public class WriteBackActivity extends AppCompatActivity {
Intent intent;
TextView quote;
    private PowerManager powerManager;
    private KeyguardManager keyguardManager;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        powerManager   = (PowerManager) this.getSystemService(Context.POWER_SERVICE);

        setContentView(R.layout.write_back_layout);
    quote=findViewById(R.id.quoteView);
    intent=getIntent();
    quote.setText(intent.getStringExtra("quote"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        if (!powerManager.isInteractive() || keyguardManager.inKeyguardRestrictedInputMode()) {
            Log.e("locked","locked");
            PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                    PowerManager.FULL_WAKE_LOCK , "TAG");
            screenLock.acquire();
    }}
    public void HandleWriteBack(View view){

        EditText editText=findViewById(R.id.editText);
        String text=editText.getText().toString();
        intent.putExtra("text",text);
            setResult(2);
            finish();

    }
}
