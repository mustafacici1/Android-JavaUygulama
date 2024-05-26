package com.example.loginapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RandomKodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomkod);


        TextView messageTextView = findViewById(R.id.messageTextView);

        String kod = getIntent().getStringExtra("kod");

    }
}