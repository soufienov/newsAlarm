package com.freedev.soufienov.newsAlarm;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 22/03/2018.
 */

public class AlarmModelAdapter extends RecyclerView.Adapter<AlarmModelAdapter.MyViewHolder>{
    private List<AlarmModel> alarmModelList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, time, repeat,category,language,id;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            id = view.findViewById(R.id.id);
            category = view.findViewById(R.id.category);
            repeat = view.findViewById(R.id.repeat);
            language = view.findViewById(R.id.language);

        }
    }

    public AlarmModelAdapter(List<AlarmModel> alarmModelList) {
        this.alarmModelList = alarmModelList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_model, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(AlarmModelAdapter.MyViewHolder holder, int position) {
        Log.e("binding","uey");

        AlarmModel alarmModel = alarmModelList.get(position);
        holder.name.setText(alarmModel.getName());
        holder.category.setText(alarmModel.getCategory());
        holder.language.setText(alarmModel.getLanguage());
        holder.time.setText(alarmModel.getTime());
        holder.repeat.setText(alarmModel.getRepeat());
        holder.id.setText(""+alarmModel.getId());
    }

    @Override
    public int getItemCount() {
        return alarmModelList.size();
    }
}
