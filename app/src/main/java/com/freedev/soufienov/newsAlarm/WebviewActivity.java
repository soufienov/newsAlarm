package com.freedev.soufienov.newsAlarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class WebviewActivity extends AppCompatActivity {

    private WebView mWebView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_webview);
        mWebView = findViewById(R.id.webview);
       WebSettings webSettings = mWebView.getSettings();
     webSettings.setJavaScriptEnabled(true);
        Intent current= getIntent();
        String title= current.getStringExtra("title");
        String url=current.getStringExtra("link");
        getSupportActionBar().setTitle(title);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7106139341895351/6460814186");
        mWebView.loadUrl(url);

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {  mInterstitialAd.show();
            }
        });



    }
}
