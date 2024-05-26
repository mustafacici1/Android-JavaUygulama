package com.example.loginapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class YemekActivity extends AppCompatActivity {

    private TextView bakiyeTextView, puanTextView, gununMenuTextView;
    private Button bakiyeButton, puanButton;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int bakiye = 630;
    private int puan = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        bakiyeTextView = findViewById(R.id.bakiyeTextView);
        puanTextView = findViewById(R.id.puanTextView);
        gununMenuTextView = findViewById(R.id.gununMenuTextView);
        bakiyeButton = findViewById(R.id.bakiyeButton);
        puanButton = findViewById(R.id.puanButton);
        String userId = firebaseAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot ->{
                    if (documentSnapshot.exists()) {
                        puan = documentSnapshot.getLong("points").intValue();
                        puanTextView.setText("Puan: "+String.valueOf(puan));
                        Log.d(TAG, "Kullanıcının puanı: " + puan);
                    } else {
                        Log.d(TAG, "Kullanıcı belgesi bulunamadı.");
                    }


                });

        updateUI();

        gununMenuTextView.setText("Günün Menüsü:\n- Mercimek Çorbası\n- Pilav\n- Kuru Fasulye");

        // Bakiye ile ödeme butonuna tıklama işlemi
        bakiyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bakiye >= 30) {
                    bakiye -= 30;
                    bakiyeTextView.setText("Bakiye: " + bakiye + " TL");
                    Intent intent = new Intent(YemekActivity.this, RandomKodActivity.class);
                    startActivity(intent);
                    Toast.makeText(YemekActivity.this, "Ödeme başarılı", Toast.LENGTH_SHORT).show();
                } else {
                    // Yeterli bakiye yoksa kullanıcıyı uyar
                    Toast.makeText(YemekActivity.this, "Yetersiz bakiye!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Puan ile ödeme butonuna tıklama işlemi
        puanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (puan >= 15) {
                    puan -= 15;
                    updateUI();
                    // Rastgele 4 haneli bir kod oluştur

                    // Puanı kullanıcıya göster
                    Intent intent = new Intent(YemekActivity.this, RandomKodActivity.class);
                    startActivity(intent);
                } else {
                    // Yeterli puan yoksa kullanıcıyı uyar
                    Toast.makeText(YemekActivity.this, "Yetersiz puan!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Uygulama arka plana atıldığında bakiye ve puanı kaydet
        savePreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Uygulama tekrar ön plana geldiğinde bakiye ve puanı geri yükle
        loadPreferences();
        updateUI();
    }

    // Rastgele 4 haneli kod oluşturma


    // Kullanıcının tercihlerini kaydetme
    private void savePreferences() {
        // SharedPreferences veya başka bir veritabanı kullanarak bakiye ve puanı kaydedebilirsiniz.
        // Bu örnekte SharedPreferences kullanarak kaydedelim.
        getSharedPreferences("YemekPref", MODE_PRIVATE)
                .edit()
                .putInt("bakiye", bakiye)
                .putInt("puan", puan)
                .apply();
    }

    // Kaydedilen tercihleri yükleme
    private void loadPreferences() {
        // SharedPreferences'ten bakiye ve puanı yükleyelim.
        bakiye = getSharedPreferences("YemekPref", MODE_PRIVATE).getInt("bakiye", 630);
        puan = getSharedPreferences("YemekPref", MODE_PRIVATE).getInt("puan", 300);
    }

    // Arayüzü güncelleme
    private void updateUI() {
        bakiyeTextView.setText("Bakiye: " + bakiye + " TL");
        puanTextView.setText("Puan: " + puan);
    }
}