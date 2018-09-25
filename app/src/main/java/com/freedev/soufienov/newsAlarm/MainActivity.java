package com.freedev.soufienov.newsAlarm;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    DatabaseHelper databaseHelper;
    private ArrayList<AlarmModel> alarmModelList = new ArrayList<>();
    private ListView listView;
    private AlarmModelAdapter aAdapter;
    ActionBar actionbar;
    TextView textview;
    LinearLayout.LayoutParams layoutparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
          setContentView(R.layout.home);
        ActivityCompat.requestPermissions
                (MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        100);
       databaseHelper=new DatabaseHelper(this);
          aAdapter = new AlarmModelAdapter(this,R.layout.alarm_model,alarmModelList);

        listView = findViewById(R.id.alarmsRecycler);
        listView.setAdapter(aAdapter);
        listView.setFastScrollEnabled(true);
        getAlarmsList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id){

               editAlarm(alarmModelList.get(position));
            }
        });
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
    public void editAlarm(AlarmModel alarmModel){
        Intent intent=new Intent(this,NewsAlarm.class);
        intent.putExtra("id",alarmModel.getId()+"");
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
