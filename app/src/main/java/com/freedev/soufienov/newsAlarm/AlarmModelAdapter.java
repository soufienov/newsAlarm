package com.freedev.soufienov.newsAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by user on 22/03/2018.
 */

public class AlarmModelAdapter extends ArrayAdapter<AlarmModel> {
	ArrayList<AlarmModel> alarmModelList;
	Context context;
	LayoutInflater inflator;
	DatabaseHelper databaseHelper;
	AlarmManager alarmManager;
	private long Time_Toget_Data=30*1000;

	public AlarmModelAdapter(Context context, int resource,	ArrayList<AlarmModel> listOfAlarms) {
		super(context, resource, listOfAlarms);
		this.alarmModelList = listOfAlarms;
		this.context = context;
		inflator = LayoutInflater.from(context);
	}

	
	
	ViewHolder holder;
    public class ViewHolder  {
        public TextView name, time, repeat,id;
        Button disable,remove;

    }





    public int getItemCount() {
        return alarmModelList.size();
    }
		@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View myView = convertView;
		if(convertView == null){
			myView = inflator.inflate(R.layout.alarm_model, parent, false);
			holder = new ViewHolder();
			holder.name = myView.findViewById(R.id.name);
			holder.time = myView.findViewById(R.id.time);
			holder.id = myView.findViewById(R.id.id);
			holder.repeat= myView.findViewById(R.id.repeat);
			holder.disable= myView.findViewById(R.id.disable);
			holder.remove= myView.findViewById(R.id.remove);

			myView.setTag(holder);
		}else{
			holder = (ViewHolder)myView.getTag();
		}
		AlarmModel detail = alarmModelList.get(position);
		holder.name.setText(detail.getName());
		holder.time.setText(detail.getTime());
		holder.repeat.setText(detail.getRepeat());
holder.id.setText(detail.getId()+"");
if(detail.isActive())
	holder.disable.setBackgroundResource(R.drawable.baseline_toggle_off_black_18dp);
			databaseHelper=new DatabaseHelper(getContext());

holder.remove.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
		builder1.setTitle("Confirmation");
		builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {


			@Override
			public void onClick(DialogInterface dialog, int which) {
				AlarmModel alert= alarmModelList.remove(position);
				notifyDataSetChanged();
				cancelalarm(alert);
				databaseHelper.deleteAlarm(alert);
			}
		});
		builder1.setMessage("Do you want to remove this alert");
		builder1.setNegativeButton("Cancel",null);

		builder1.show();
	}


});
holder.disable.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
		AlarmModel alert= alarmModelList.get(position);
		Button disable=(Button)v;
		if(alert.isActive()) {
			alert.setActive(false);
			disable.setBackgroundResource(R.drawable.baseline_toggle_off_black_18dp);
			cancelalarm(alert);
		}
		else
		{
			alert.setActive(true);
			disable.setBackgroundResource(R.drawable.baseline_toggle_on_black_18dp);
		}
		databaseHelper.updateAlarm(alert);
	}
});
		return myView;
	}
	public void cancelalarm(AlarmModel alarmModel){
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

		Intent browserIntent=new Intent(getContext(),Alarm.class);

		PendingIntent pi= PendingIntent.getBroadcast(context.getApplicationContext(),alarmModel.getId(),browserIntent,0);
		alarmManager.cancel(pi);
	}
	@RequiresApi(api = Build.VERSION_CODES.M)
	public void activateAlert(AlarmModel alarmModel){

		alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

		Intent browserIntent=new Intent(getContext(),Alarm.class);
		browserIntent.putExtra("alarm_id",alarmModel.getId());
		browserIntent.putExtra("alarm_repeat",alarmModel.getRepeat());
		PendingIntent pi= PendingIntent.getBroadcast(context.getApplicationContext(),alarmModel.getId(),browserIntent,0);
		setAlarmManager(pi,alarmModel);
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	private void setAlarmManager(PendingIntent pi,AlarmModel alarmModel){
		String time=alarmModel.getTime();

		int hours =Integer.parseInt(time.substring(0,2));
		int mins =Integer.parseInt(time.substring(3,5));
		Log.e("h:",""+hours);
		Log.e("m:",""+mins);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY,hours);
		calendar.set(Calendar.MINUTE, mins);
		alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-Time_Toget_Data,
				AlarmManager.INTERVAL_DAY, pi);
	}

}
