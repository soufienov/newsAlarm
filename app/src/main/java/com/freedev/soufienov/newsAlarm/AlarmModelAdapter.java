package com.freedev.soufienov.newsAlarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22/03/2018.
 */

public class AlarmModelAdapter extends ArrayAdapter<AlarmModel> {
	ArrayList<AlarmModel> alarmModelList;
	Context context;
	LayoutInflater inflator;
	
	public AlarmModelAdapter(Context context, int resource,	ArrayList<AlarmModel> listOfAlarms) {
		super(context, resource, listOfAlarms);
		this.alarmModelList = listOfAlarms;
		this.context = context;
		inflator = LayoutInflater.from(context);
	}

	
	
	ViewHolder holder;
    public class ViewHolder  {
        public TextView name, time, repeat,id;

    }





    public int getItemCount() {
        return alarmModelList.size();
    }
		@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View myView = convertView;
		if(convertView == null){
			myView = inflator.inflate(R.layout.alarm_model, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) myView.findViewById(R.id.name);
			holder.time = (TextView) myView.findViewById(R.id.time);
			holder.id = (TextView) myView.findViewById(R.id.id);
			holder.repeat=(TextView)myView.findViewById(R.id.repeat);
			myView.setTag(holder);
		}else{
			holder = (ViewHolder)myView.getTag();
		}
		AlarmModel detail = alarmModelList.get(position);
		holder.name.setText(detail.getName());
		holder.time.setText(detail.getTime());
		holder.repeat.setText(detail.getRepeat());

		return myView;
	}
}
