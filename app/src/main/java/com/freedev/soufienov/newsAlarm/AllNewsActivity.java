package com.freedev.soufienov.newsAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllNewsActivity extends AppCompatActivity {
    private List<Article> articleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArticleAdapter aAdapter;
    private String category;
    AlarmManager alarmManager;
    final CharSequence colors[] = new CharSequence[] {"All","UK","USA","Argentina","Australia","Austria","Belgium","Brazil",
            "Bulgaria","Canada","China","Colombia","Cuba","Czech Rp","Egypt","France","Germany","Greece",
            "Hungary","India","Indonesia","Israel","Italy","Japan","Mexico","Netherlands","Nigeria","Portugal","Poland","Romania","Russia","Saudi","Korea","Switzerland","Taiwan",
            "Thailand","Turkey","Emirates","Ukraine","Venezuela"};
    String[]countries= new String[]{"all","gb","us","ar","au","at","be","br",
            "bg","ca","cn","co","cu","cz","eg","fr","de","gr",
            "hu","in","id","il","it","jp","mx","nl","ng","pt","pl",
            "ro","ru","sa","kr","ch","tw",
            "th","tr","ae","ua","ve"};
    private String country;
String[] languages={"ar","de","en","es","fr","he","it","nl","no","pt","ru","se","ud","zh"};
    private SearchView searchView;
    private TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_news);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar= findViewById(R.id.progressBar2);
        searchView= findViewById(R.id.searchView);
        searchView.setQueryHint("Search here");



        searchView.setActivated(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
               Search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
aAdapter = new ArticleAdapter(articleList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(aAdapter);
        category=getIntent().getStringExtra("category").toLowerCase();
        country=getIntent().getStringExtra("loc").toLowerCase();
category=(category=="all")? "general":category;
        getSupportActionBar().hide();

        SendRequest();
    }


    private  void  SendRequest(){
       progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url =(!country.equals("all"))? "https://newsapi.org/v2/top-headlines?" +
                "country="+country+"&category=" +category+
                "&apiKey=59514c0e996a4982b784153d6a7762f3":"https://newsapi.org/v2/top-headlines?" +
                "category=" +category+
                "&apiKey=59514c0e996a4982b784153d6a7762f3";

        JsonObjectRequest  stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response)  {
                        try {articleList.clear();
                            JSONArray articles= (JSONArray) response.get("articles");
                            for (int i = 0 ; i < articles.length(); i++) {
                                JSONObject art= articles.getJSONObject(i);
                                Article article= new Article(art);

                                articleList.add(article);


                            } progressBar.setVisibility(View.GONE);
                            aAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);

                        }
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }
    private  void  Search(String q){
      progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url =(!country.equals("all"))? "https://newsapi.org/v2/top-headlines?q="+q +
                "&country="+country+"&category=" +category+
                "&apiKey=59514c0e996a4982b784153d6a7762f3":"https://newsapi.org/v2/top-headlines?q="+q +
                "&category=" +category+
                "&apiKey=59514c0e996a4982b784153d6a7762f3";

        JsonObjectRequest  stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response)  {
                        try {articleList.clear();
                            JSONArray articles= (JSONArray) response.get("articles");
                            for (int i = 0 ; i < articles.length(); i++) {
                                JSONObject art= articles.getJSONObject(i);
                                Article article= new Article(art);

                                articleList.add(article);


                            } progressBar.setVisibility(View.GONE);
                            aAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);

                        }
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }
    public void openPage(View view) {
        if(isInternetAvailable()){
        LinearLayout linearLayout= (LinearLayout)view;
       TextView link= (TextView) linearLayout.getChildAt(2);
       String url= link.getText().toString();
        TextView title= (TextView) linearLayout.getChildAt(0);
        String tit= title.getText().toString();
            Intent browserIntent = new Intent(AllNewsActivity.this,Alarm.class);
            browserIntent.putExtra("title",tit);
            PendingIntent pi=PendingIntent.getBroadcast(getApplicationContext(),0,browserIntent,0);
alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pi);
        /*
        browserIntent.putExtra("link",url);

        startActivity(browserIntent);*/}
        else showAlert();
    }

    public void popCountries(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a country");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selection=colors[which].toString();
                FloatingActionButton flb= findViewById(R.id.flb);
                int ident=getApplicationContext().getResources().getIdentifier(selection.toLowerCase(),"drawable",getPackageName());
                flb.setImageResource(ident);
                country= countries[which];
                SendRequest();
            }
        });
        builder.show();
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
}
