package com.freedev.soufienov.allnews;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
aAdapter = new ArticleAdapter(articleList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(aAdapter);
        category=getIntent().getStringExtra("category").toLowerCase();
        country=getIntent().getStringExtra("loc").toLowerCase();
category=(category=="all")? "general":category;
        getSupportActionBar().setTitle(category);

progressBar.setVisibility(View.VISIBLE);
        SendRequest();
    }


    private  void  SendRequest(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url =(country=="all")? "https://newsapi.org/v2/top-headlines?" +
                "country="+country+"&category=" +category+
                "&apiKey=59514c0e996a4982b784153d6a7762f3":"https://newsapi.org/v2/top-headlines?" +
                "category=" +category+
                "&apiKey=59514c0e996a4982b784153d6a7762f3";

// Request a string response from the provided URL.
        JsonObjectRequest  stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response)  {
                        try {
                            JSONArray articles= (JSONArray) response.get("articles");
                            for (int i = 0 ; i < articles.length(); i++) {
                                JSONObject art= articles.getJSONObject(i);
                                Article article= new Article(art);
                                articleList.add(article);
                                progressBar.setVisibility(View.GONE);

                            }
                            aAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);

                        }
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("That didn't work!","vbds");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void openPage(View view) {
        LinearLayout linearLayout= (LinearLayout)view;
       TextView link= (TextView) linearLayout.getChildAt(2);
       String url= link.getText().toString();
        TextView title= (TextView) linearLayout.getChildAt(0);
        String tit= title.getText().toString();
        Intent browserIntent = new Intent(AllNewsActivity.this,WebviewActivity.class);
        browserIntent.putExtra("link",url);
        browserIntent.putExtra("title",tit);
        startActivity(browserIntent);
    }
}
