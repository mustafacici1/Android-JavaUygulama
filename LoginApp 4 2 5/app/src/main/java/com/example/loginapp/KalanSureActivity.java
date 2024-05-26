package com.example.loginapp;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class KalanSureActivity extends AppCompatActivity {

    private long remainingTimeInSeconds = BilgiTimer.remainingTimeInSeconds; // Saniye bilgisini al veya gerekirse ayarla
    private TextView textView;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalansure);
        textView = findViewById(R.id.textView3);

        // Timer'ı başlat
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer(); // Sürekli çalışan metodu çağır
            }
        }, 0, 1000); // Başlangıçta hemen başla, her 1 saniyede bir çalıştır
    }

    private void updateTimer() {
        remainingTimeInSeconds--; // Her saniyede saniyeyi azalt
        if (remainingTimeInSeconds < 0) {
            remainingTimeInSeconds = 0; // Negatif değerleri sıfıra ayarla, isteğe bağlı
        }
        long hours = remainingTimeInSeconds / 3600;
        long minutes = (remainingTimeInSeconds % 3600) / 60;
        long seconds = remainingTimeInSeconds % 60;

        // UI'yı güncelleme işlemi
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String timeLeftFormatted = String.format("%02d Saat %02d Dakika %02d Saniye", hours, minutes, seconds);
                textView.setText(timeLeftFormatted);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel(); // Timer'ı durdur
 }
}
}