package com.freedev.soufienov.allnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    String countryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button politics= (Button) findViewById(R.id.button);
        MobileAds.initialize(this, "ca-app-pub-7106139341895351~3511384828");
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
         countryCode = tm.getSimCountryIso();

    }

    public void getNews(View view) {
        Button btn =(Button)view;
        String cat= btn.getText().toString();
       Intent intent= new Intent(MainActivity.this,AllNewsActivity.class);
       intent.putExtra("category",cat);
       intent.putExtra("loc",countryCode);
       startActivity(intent);
    }
}
