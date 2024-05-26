package com.example.loginapp;
import android.text.InputType;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;
    Button pin;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    private Handler handler = new Handler();

    private Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "30 saniye boyunca giriş yapılamadı. Tekrar deneyin.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        username = findViewById(R.id.username);
        username.setInputType(InputType.TYPE_CLASS_NUMBER); // Klavyenin sadece sayısal giriş modunu açar
        username.setRawInputType(InputType.TYPE_CLASS_NUMBER); // Klavyenin sayısal klavye modunda açılmasını sağlar

        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        pin = findViewById(R.id.pin);
        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference usersRef = database.getReference("users");

                String enteredUsername = username.getText().toString()+"@ogr.dpu.edu.tr";
                String enteredPassword = password.getText().toString();
                if (TextUtils.isEmpty(enteredUsername)) {
                    Toast.makeText(getApplicationContext(),
                                    "Öğrenci numarası boş bırakılamaz!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(enteredPassword)) {
                    Toast.makeText(getApplicationContext(),
                                    "Şifre boş bırakılamaz!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }


                mAuth.signInWithEmailAndPassword(enteredUsername, enteredPassword)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                            "Başarıyla giriş yaptın!",
                                                            Toast.LENGTH_LONG)
                                                    .show();

                                            Intent intent
                                                    = new Intent(MainActivity.this,
                                                    SecondActivity.class);
                                            startActivity(intent);
                                        } else {

                                            Toast.makeText(getApplicationContext(),
                                                            "Giriş Başarısız!",
                                                            Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });

            }
        });

        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingScreen(ThirdActivity.class); // Loading ekranını göster ve ThirdActivity'e geç
            }
        });

        LinearLayout mainLayout = findViewById(R.id.main);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Klavyeyi kapat
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Uygulama arka planda olduğunda timeout durdurulsun
        handler.removeCallbacks(timeoutRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Loading ekranını gösteren ve hedef aktiviteye geçişi sağlayan metot
    protected void showLoadingScreen(Class<?> cls) {
        Intent loadingIntent = new Intent(MainActivity.this, LoadingActivity.class);
        loadingIntent.putExtra("target_class", cls); // Hedef aktivite sınıfını intent'e ekle
        startActivity(loadingIntent);
}
}