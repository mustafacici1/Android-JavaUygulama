 package com.example.loginapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class PuanActivity extends AppCompatActivity {
    private RadioButton radiButton1, radiButton2, radiButton3, radiButton4;

    private Button button;
    private TextView questionTextView;
    private TextView timerview;
    private ArrayList<String> sorular;
    private ArrayList<String> cevaplar;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puan);
        initializeTools();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        completeQuestions();
        chooseQuestion();
        int dogruCevaplar[] = {1, 4, 11};
        button.setOnClickListener(v -> {
            Intent intent = new Intent(PuanActivity.this, SecondActivity.class);
            RadioGroup optionRadioGroup = findViewById(R.id.optionRadioGroup);
            RadioButton selectedRadioButton = findViewById(optionRadioGroup.getCheckedRadioButtonId());
            String selectedText = selectedRadioButton.getText().toString();
            if (selectedText.contains(cevaplar.get(1)) || selectedText.contains(cevaplar.get(4)) || selectedText.contains(cevaplar.get(11))) {
                Toast.makeText(getApplicationContext(), "Doğru Cevap! 1 Yemekhane Puanı kazandın.", Toast.LENGTH_SHORT).show();

                BilgiTimer bilgiTimer = new BilgiTimer(questionTextView, this);
                bilgiTimer.startTimer();
                setUserPuan();


                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Yanlış Cevap! Maalesef Yemekhane puanı kazanamadın.", Toast.LENGTH_SHORT).show();
                BilgiTimer bilgiTimer = new BilgiTimer(questionTextView, this);
                bilgiTimer.startTimer();
                startActivity(intent);
            }
        });


    }


    public void initializeTools() {
        radiButton1 = findViewById(R.id.option1RadioButton);
        radiButton2 = findViewById(R.id.option2RadioButton);
        button = findViewById(R.id.submitBtn);
        radiButton3 = findViewById(R.id.option3RadioButton);
        radiButton4 = findViewById(R.id.option4RadioButton);
        questionTextView = findViewById(R.id.questionTextView);
    }


    public void completeQuestions() {
        sorular = new ArrayList<>();
        cevaplar = new ArrayList<>();
        sorular.add("Kütahya Dumlupınar Üniversitesi rektörünün ismi nedir?");
        cevaplar.add("Durmuş Özdemir");
        cevaplar.add("Süleyman Kızıltoprak");
        cevaplar.add("Soydan Serttaş");
        cevaplar.add("Ahmet Tahsin");
        sorular.add("Kütahya Dumlupınar Üniversitesi kaç yılında kurulmuştur?");
        cevaplar.add("1992");
        cevaplar.add("1991");
        cevaplar.add("1986");
        cevaplar.add("1985");
        sorular.add("Aşağıdakilerden hangisi Kütahya'nın gazoz markasıdır?");
        cevaplar.add("Zafer Gazoz");
        cevaplar.add("Niğde Gazoz");
        cevaplar.add("Sprite");
        cevaplar.add("Vazoz");
    }

    public void chooseQuestion() {
        Random random = new Random();
        int randomSayi = random.nextInt(3);
        switch (randomSayi) {
            case 0:
                questionTextView.setText(sorular.get(0));
                radiButton1.setText(cevaplar.get(0));
                radiButton2.setText(cevaplar.get(1));
                radiButton3.setText(cevaplar.get(2));
                radiButton4.setText(cevaplar.get(3));
                break;
            case 1:
                questionTextView.setText(sorular.get(1));
                radiButton2.setText(cevaplar.get(5));
                radiButton3.setText(cevaplar.get(6));
                radiButton4.setText(cevaplar.get(7));
                radiButton1.setText(cevaplar.get(4));
                break;
            case 2:
                questionTextView.setText(sorular.get(2));
                radiButton1.setText(cevaplar.get(8));
                radiButton2.setText(cevaplar.get(9));
                radiButton3.setText(cevaplar.get(10));
                radiButton4.setText(cevaplar.get(11));
                break;

        }


    }



    private void setUserPuan() {
        String userId = firebaseAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        int puan = documentSnapshot.getLong("points").intValue();
                        db.collection("users").document(userId).update("points", puan+1)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Kullanıcı puanı başarıyla güncellendi.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Kullanıcı puanı güncelleme başarısız: " + e.getMessage(), e);
                                });

                        Log.d(TAG, "Kullanıcının puanı: " + puan);
                    } else {
                        Log.d(TAG, "Kullanıcı belgesi bulunamadı.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Kullanıcı puanı okuma başarısız: " + e.getMessage(), e);
            });
}

}