package com.easytech.smartstaffapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class ReportingActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);

        webView = (WebView) findViewById(R.id.reporting_view);
        webView.loadUrl(Constants.reportString);
    }
}
