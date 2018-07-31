package com.freedev.soufienov.newsAlarm;

import android.app.AlarmManager;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    DatabaseHelper databaseHelper;
    private List<AlarmModel> alarmModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AlarmModelAdapter aAdapter;
    String countryCode;
    ActionBar actionbar;
    TextView textview;
    LinearLayout.LayoutParams layoutparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
          setContentView(R.layout.home);

 /*   ActionBarTitleGravity();
        MobileAds.initialize(this, "ca-app-pub-7106139341895351~8411780987");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        databaseHelper=new DatabaseHelper(this);
          aAdapter = new AlarmModelAdapter(alarmModelList);

        recyclerView= findViewById(R.id.alarmsRecycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(aAdapter);
        getAlarmsList();
       AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
 TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
         countryCode = tm.getSimCountryIso();
    }

    
    public void setAlarm(View v){
        try {
            Intent i=new Intent(this,NewsAlarm.class);
            startActivityForResult(i,0);
        }
        catch (Exception e){Log.e("sit",e.getMessage());
        }


    }

    private void ActionBarTitleGravity() {
        // TODO Auto-generated method stub

        actionbar = getSupportActionBar();

        textview = new TextView(getApplicationContext());

        layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        textview.setLayoutParams(layoutparams);

        textview.setText("NOW|HERE");

        textview.setTextColor(Color.WHITE);

        textview.setGravity(Gravity.CENTER);

        textview.setTextSize(20);

        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionbar.setCustomView(textview);

    }

    public void getAlarmsList() {
alarmModelList.clear();

alarmModelList.addAll(databaseHelper.getAllAlarms());
           aAdapter.notifyDataSetChanged();



    }
    public void editAlarm(View view){
        Intent intent=new Intent(this,NewsAlarm.class);
         LinearLayout linearLayout=(LinearLayout) view;
        TextView textView=(TextView)linearLayout.getChildAt(2);
        intent.putExtra("id",textView.getText());
startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAlarmsList();
        Toast.makeText(this,"Alarm created",Toast.LENGTH_SHORT);
        if (requestCode==1 && resultCode==1){
            Toast.makeText(this,"Alarm created",Toast.LENGTH_SHORT);}
        if (requestCode==2 && resultCode==1){
            Toast.makeText(this,"Alarm edited",Toast.LENGTH_SHORT);}
    }
}
