package com.freedev.soufienov.allnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    String countryCode;
    String[] countries;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        countries= new String[]{"ar","au","at","be","br",
        "bg","ca","cn","co","cu","cz","eg","fr","de","gr",
        "hu","in","id","il","it","jp","mx","nl","ng","pl","pt",
        "ro","ru","sa","kr","ch","tw",
        "th","tr","ae","ua","gb","us","ve"};
        setContentView(R.layout.activity_main);
        Button politics= findViewById(R.id.button);
        MobileAds.initialize(this, "ca-app-pub-7106139341895351~3511384828");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
         countryCode = tm.getSimCountryIso();
if(!Arrays.asList(countries).contains(countryCode)){countryCode="all";}
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
