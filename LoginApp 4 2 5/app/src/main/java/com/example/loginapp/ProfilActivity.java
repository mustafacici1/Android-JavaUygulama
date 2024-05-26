package com.example.loginapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class ProfilActivity extends AppCompatActivity implements SensorEventListener{

    private TextView stepCountTextView;
    private TextView distanceTextView;
    private TextView timeTextView;

    private Button pauseButton;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount=0;

    private ProgressBar progressBar;
    private boolean isPaused = false;

    private long timePaused = 0;
    private float stepLenghtInMeters=0.762f;
    private long starTime;

    private int stepCountTargget=5000;

    private Handler timeHandler=new Handler();
    private Runnable timerRunnable=new Runnable() {
        @Override
        public void run() {
            long milis=System.currentTimeMillis()-starTime;
            int seconds=(int)(milis/1000);
            int min=seconds/60;
            seconds=seconds%60;

        }
    };

    private TextView stepCountTargetTextView;



    @Override
    protected void onStop() {
        super.onStop();
        if(stepCounterSensor!=null){

            sensorManager.unregisterListener(this);
            timeHandler.removeCallbacks(timerRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(stepCounterSensor!=null){
            sensorManager.registerListener(this,stepCounterSensor,SensorManager.SENSOR_DELAY_NORMAL);
            timeHandler.postDelayed(timerRunnable,0);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        stepCountTextView= findViewById(R.id.stepCountTextView);
        distanceTextView= findViewById(R.id.distanceTextView);
        pauseButton=findViewById(R.id.pauseButton);
        stepCountTargetTextView=findViewById(R.id.stepCountTargetTextView);
        progressBar=findViewById(R.id.progressBar);

        starTime= System.currentTimeMillis();

        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        progressBar.setMax(stepCountTargget);
        stepCountTargetTextView.setText("Adım Hedefi:"+stepCountTargget);
        if (stepCounterSensor==null){
            stepCountTextView.setText("Adım sayacı aktif değil.");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_STEP_COUNTER){
            stepCount=(int) sensorEvent.values[0];
            stepCountTextView.setText("Adım Sayacı "+stepCount);
            progressBar.setProgress(stepCount);
            if(stepCount>=stepCountTargget){
                stepCountTargetTextView.setText("Hedefe ulaşıldı.");
                String userId = firebaseAuth.getCurrentUser().getUid();

                db.collection("users").document(userId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                int puan = documentSnapshot.getLong("points").intValue();
                                Toast.makeText(getApplicationContext(), "Tebrikler Sporcu. 1 Yemekhane puanı kazandın.", Toast.LENGTH_SHORT).show();
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
            float distanceInKm=stepCount*stepLenghtInMeters/1000;
            distanceTextView.setText(String.format(Locale.getDefault(),"Mesafe: %.2f km",distanceInKm));


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void onPauseButtonClicked(View view){
        if(isPaused){
            isPaused=false;
            pauseButton.setText("Durdur");
            starTime=System.currentTimeMillis()-timePaused;
            timeHandler.postDelayed(timerRunnable,0);
        }
        else {
            isPaused=true;
            pauseButton.setText("Devam ettir");
            timeHandler.removeCallbacks(timerRunnable);
            timePaused=System.currentTimeMillis()-starTime;
 }
}
}