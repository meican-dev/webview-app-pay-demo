package com.miaonster.webviewwechatpay;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i("onLoadResource", url);
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String s = request.getUrl().toString();

                Log.i("shouldOverrideUrlLoading", s);

                try {
                    if (s.startsWith("weixin://wap/pay?")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);
                        return true;
                    }
                    if (s.startsWith("alipays://platformapi/startApp?")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                    alert.setTitle("Cannot open Wechat / Alipay");
                    alert.show();
                }

                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.loadUrl("https://meican.com");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
