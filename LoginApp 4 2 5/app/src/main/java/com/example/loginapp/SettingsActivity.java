package com.example.loginapp; // Paket adınızı değiştirin

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.R;

public class SettingsActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // activity_main.xml dosyanızın adını değiştirin
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // JavaScript'i etkinleştirme (isteğe bağlı)

        // Web sayfasını yükleme
        webView.loadUrl("https://www.dpu.edu.tr/index/duyurular");
}
}