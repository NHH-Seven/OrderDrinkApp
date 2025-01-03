package com.sinhvien.orderdrinkapp.Activities;




import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sinhvien.orderdrinkapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        webView = findViewById(R.id.webView);

        // Nhận URL từ Intent
        String url = getIntent().getStringExtra("url");

        // Cấu hình WebView
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
