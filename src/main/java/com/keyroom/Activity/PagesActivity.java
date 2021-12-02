package com.keyroom.Activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.keyroom.R;

public class PagesActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView txtPagesName;
    WebView webview;
    String url;
    String pageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        txtPagesName = findViewById(R.id.txtPagesName);
        webview = findViewById(R.id.webview);
        pageName = getIntent().getStringExtra("pageName");
        url = getIntent().getStringExtra("url");
        txtPagesName.setText(pageName);


        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);

        WebSettings webSettings=webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webview.canGoBack()) {
            this.webview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
