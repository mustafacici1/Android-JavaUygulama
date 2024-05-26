package com.example.loginapp;
import android.text.InputType;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ThirdActivity extends AppCompatActivity {

    private EditText ogrenciNo, sifre, reSifre;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        ogrenciNo = findViewById(R.id.ogrenci_no_gir);
        sifre = findViewById(R.id.sifre_olustur);
        reSifre = findViewById(R.id.tekrar_sifre_gir);
        registerButton = findViewById(R.id.registerButton);

        // Öğrenci numarası EditText'ine sayısal klavye modunu açar
        ogrenciNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        ogrenciNo.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String ogrenciNoText = ogrenciNo.getText().toString().trim();
        String sifreText = sifre.getText().toString().trim();
        String reSifreText = reSifre.getText().toString().trim();

        if (ogrenciNoText.isEmpty() || sifreText.isEmpty() || reSifreText.isEmpty()) {
            Toast.makeText(ThirdActivity.this, "Hata! Değerler boş bırakılamaz.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ogrenciNoText.length() != 12 || !ogrenciNoText.matches("[0-9]+")) {
            Toast.makeText(ThirdActivity.this, "Hata! Öğrenci numarası 12 haneli sayıdan oluşmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sifreText.length() < 6 || reSifreText.length() < 6) {
            Toast.makeText(ThirdActivity.this, "Hata! Şifre minimum 6 karakterden oluşmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!sifreText.equals(reSifreText)) {
            Toast.makeText(ThirdActivity.this, "Hata! Şifreler aynı değil.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(sifreText.equals(reSifreText) && ogrenciNoText.length() == 12 && ogrenciNoText.matches("[0-9]+") ) {
            String ogrenciNoTextFinal = ogrenciNoText + "@ogr.dpu.edu.tr";
            firebaseAuth.createUserWithEmailAndPassword(ogrenciNoTextFinal, sifreText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("username", ogrenciNoTextFinal);
                                userData.put("password", sifreText);
                                userData.put("bakiye", 120);
                                userData.put("points", 0); // Başlangıçta kullanıcının puanı 0 olabilir

                                // Firestore'daki "users" koleksiyonuna kullanıcı verilerini ekle
                                db.collection("users")
                                        .document(user.getUid())
                                        .set(userData)
                                        .addOnSuccessListener(aVoid -> {
                                            // Firestore'a veri başarıyla eklendi
                                            Log.d(TAG, "Kullanıcı verileri Firestore'a eklendi.");
                                        })
                                        .addOnFailureListener(e -> {
                                            // Firestore'a veri eklerken hata oluştu
                                            Log.e(TAG, "Kullanıcı verilerini Firestore'a eklerken hata oluştu: " + e.getMessage());
                                        });
                            }
                        } else {
                            // Kullanıcı oluşturma işlemi başarısız oldu
                            Log.e(TAG, "Kullanıcı oluşturma işlemi başarısız oldu: " + task.getException().getMessage());
                        }
                    });
            Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(ThirdActivity.this, "Kullanıcı başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();

        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideKeyboard();
        }
        return super.onTouchEvent(event);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
}
}