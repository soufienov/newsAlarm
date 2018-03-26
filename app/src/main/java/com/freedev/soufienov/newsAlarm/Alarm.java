package com.freedev.soufienov.newsAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 19/03/2018.
 * receives notification from alarm manager and lunches the news reader
 */

public class Alarm extends BroadcastReceiver {

    String category,language;
    private List<String> articleList = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
String alarm_repeat=intent.getStringExtra("alarm_repeat");
        Calendar calendar=Calendar.getInstance();
        Date date = calendar.getTime();
        String day=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        if (alarm_repeat.contains(day)||alarm_repeat.contains("Every day"))
        {Log.e("lol"," today");
            category=intent.getStringExtra("category");
            language=intent.getStringExtra("alarm_country");
           // SendRequest(context);
            intent.putExtra("articles",articleList.toArray());
            intent.setClass(context,NewsReader.class);
  context.startActivity(intent);}
  else Log.e("lol","not today");
    }
    private  void  SendRequest(Context ctx){

        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url ="https://newsapi.org/v2/top-headlines?"+"category=" +category+"&country=" +language+
                "&apiKey=59514c0e996a4982b784153d6a7762f3";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response)  {
                        try {articleList.clear();
                            JSONArray articles= (JSONArray) response.get("articles");
                            for (int i = 0 ; i < articles.length(); i++) {
                                JSONObject art= articles.getJSONObject(i);
                                Article article= new Article(art);
                                Log.e("log"," today");
                                articleList.add(article.title);


                            }
                        } catch (JSONException e) {     Log.e("pig"," onday");

                            e.printStackTrace();

                        }
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }
}
