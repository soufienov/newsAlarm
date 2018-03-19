package com.freedev.soufienov.newsAlarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    String countryCode;
    String[] countries;
    ActionBar actionbar;
    TextView textview;
    RelativeLayout.LayoutParams layoutparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        countries= new String[]{"ar","au","at","be","br",
        "bg","ca","cn","co","cu","cz","eg","fr","de","gr",
        "hu","in","id","il","it","jp","mx","nl","ng","pl","pt",
        "ro","ru","sa","kr","ch","tw",
        "th","tr","ae","ua","gb","us","ve"};
        setContentView(R.layout.activity_main);
     ActionBarTitleGravity();
        MobileAds.initialize(this, "ca-app-pub-7106139341895351~8411780987");
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
            if(isInternetAvailable()){startActivity(intent);}
        else showAlert();

    }
    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public  void showAlert(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("No Internet")
                .setMessage("Please check your intenet connection and try again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })


                .show();
    }
    private void ActionBarTitleGravity() {
        // TODO Auto-generated method stub

        actionbar = getSupportActionBar();

        textview = new TextView(getApplicationContext());

        layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        textview.setLayoutParams(layoutparams);

        textview.setText("NOW|HERE");

        textview.setTextColor(Color.WHITE);

        textview.setGravity(Gravity.CENTER);

        textview.setTextSize(20);

        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionbar.setCustomView(textview);

    }
}
