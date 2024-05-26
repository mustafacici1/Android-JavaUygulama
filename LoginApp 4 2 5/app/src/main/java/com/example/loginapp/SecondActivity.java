package com.example.loginapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.loginapp.ProfilActivity;
import com.example.loginapp.PuanActivity;
import com.example.loginapp.R;
import com.example.loginapp.SettingsActivity;
import com.example.loginapp.YemekActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        // Üstten aşağı çekme çubuğunu gizle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Kartları tanımla
        CardView cardProfil = findViewById(R.id.carpHome);
        CardView cardPuanKazan = findViewById(R.id.cardChat);
        CardView cardAyarlar = findViewById(R.id.cardSettings);
        CardView cardYemekhane = findViewById(R.id.cardWidget);
        CardView cardOtobusler = findViewById(R.id.cardProfile);
        CardView cardCikis = findViewById(R.id.cardLogout);

        // Kartlara tıklama olaylarını ekle
        cardProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Profil aktivitesine geçiş
                Intent intent = new Intent(SecondActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        cardPuanKazan.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                if (BilgiTimer.remainingTimeInSeconds == 0) {
                    Intent intent = new Intent(SecondActivity.this, PuanActivity.class);
                    startActivity(intent);
                } else if (BilgiTimer.remainingTimeInSeconds > 0) {
                    Intent intent = new Intent(SecondActivity.this, KalanSureActivity.class);
                    startActivity(intent);
                }
            }


        });


        cardAyarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ayarlar aktivitesine geçiş
                Intent intent = new Intent(SecondActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        cardYemekhane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, YemekActivity.class);
                startActivity(intent);
            }
        });


        cardOtobusler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Otobüsler kartına tıklandığında yapılacak işlemler
                // Tarayıcıda belirli bir URL'yi açmak için Intent oluştur
                String url = "https://www.kutahya.bel.tr/ulasim.asp"; // Gitmek istediğiniz URL buraya yazılmalı
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        cardCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitConfirmationDialog();
            }
        });


    }
    private void showExitConfirmationDialog () {
        new AlertDialog.Builder(SecondActivity.this)
                .setTitle("Çıkış")
                .setMessage("Uygulamadan çıkmak istediğinize emin misiniz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Evet'e tıklandığında uygulamadan çıkış yap
                        finishAffinity(); // Tüm aktiviteleri kapat
                    }
                })
                .setNegativeButton("Hayır", null) // Hayır'a tıklandığında işlem yapma
                .show();

}
}