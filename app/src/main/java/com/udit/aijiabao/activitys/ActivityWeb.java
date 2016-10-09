package com.udit.aijiabao.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.udit.aijiabao.R;

public class ActivityWeb extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://cl.jxglrj.com/app/index.php?i=5&c=entry&m=ewei_shopv2&do=mobile");
    }
}
