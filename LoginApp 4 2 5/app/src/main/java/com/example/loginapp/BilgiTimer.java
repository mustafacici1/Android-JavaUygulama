package com.example.loginapp;

import android.os.CountDownTimer;
import android.widget.TextView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.widget.TextView;

public class BilgiTimer {

    private CountDownTimer countDownTimer;
    private TextView textView;
    public static long remainingTimeInSeconds;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TimerPrefs";
    private static final String REMAINING_TIME_KEY = "remainingTime";

    public BilgiTimer(TextView textView, Context context) {
        this.textView = textView;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        remainingTimeInSeconds = sharedPreferences.getLong(REMAINING_TIME_KEY, 24 * 60 * 60); // Varsayılan olarak 24 saat
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(remainingTimeInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeInSeconds = millisUntilFinished / 1000;

                long hours = remainingTimeInSeconds / 3600;
                long minutes = (remainingTimeInSeconds % 3600) / 60;
                long seconds = remainingTimeInSeconds % 60;

                String timeLeftFormatted = String.format("%02d Saat %02d Dakika %02d Saniye", hours, minutes, seconds);
                textView.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                remainingTimeInSeconds = 0;
                textView.setText("00 Saat 00 Dakika 00 Saniye");
            }
        }.start();
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            saveRemainingTime();
        }
    }

    private void saveRemainingTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(REMAINING_TIME_KEY, remainingTimeInSeconds);
        editor.apply();
}
}