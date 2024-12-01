package com.example.shopinglist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView logo = findViewById(R.id.ivlogo);

        // Load the animation from XML
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);

        // Start the animation
        logo.startAnimation(logoAnimation);

        // Delay navigation to LoginActivity after the animation ends
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, Login.class));
            finish();
        }, 1500); // Match the duration of the animation
    }

}