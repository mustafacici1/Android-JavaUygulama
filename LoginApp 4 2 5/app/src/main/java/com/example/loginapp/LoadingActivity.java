package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private Class<?> targetClass; // Hedef aktivite sınıfını saklamak için değişken
    private boolean backPressed = false; // Geri tuşuna basılıp basılmadığını kontrol etmek için

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        // Intent'ten gelen hedef aktivite sınıfını al
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("target_class")) {
            targetClass = (Class<?>) intent.getSerializableExtra("target_class");
        }

        // Geri tuşuna basıldığında loading ekranından çık
        findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed = true;
                finish();
            }
        });

        // 3 saniye bekleyip sonra yeni aktiviteyi başlat
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!backPressed && targetClass != null) {
                    startActivity(new Intent(LoadingActivity.this, targetClass));
                    finish(); // LoadingActivity'yi kapat
                }
            }
        }, 750);
    }

    @Override
    public void onBackPressed() {
        backPressed = true;
        super.onBackPressed();
}
}